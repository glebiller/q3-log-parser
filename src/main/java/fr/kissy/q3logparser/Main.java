package fr.kissy.q3logparser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.hash.Hashing;
import fr.kissy.q3logparser.dto.Game;
import fr.kissy.q3logparser.dto.enums.GameType;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import fr.kissy.q3logparser.dto.enums.Team;
import fr.kissy.q3logparser.funnel.GameFunnel;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Main {

    private static final String OUTPUT_DIRECTORY = "_data/";
    private static final String OUTPUT_GAMES_DIRECTORY = OUTPUT_DIRECTORY + "games/";
    private static final String OUTPUT_STATS_DIRECTORY = OUTPUT_DIRECTORY + "stats/";
    private static final Configuration FREEMARKER_CONFIGURATION = new Configuration();
    private static final String TITLE_TEMPLATE_PATH = "src/main/resources/fr/kissy/q3logparser/includes/title.ftl";
    private static final String GAME_TEMPLATE_PATH = "src/main/resources/fr/kissy/q3logparser/results.ftl";
    private static final String INDEX_TEMPLATE_PATH = "src/main/resources/fr/kissy/q3logparser/index.ftl";

    private static final Pattern LINE_PATTERN = Pattern.compile("^([0-9]{1,2}:[0-9]{2}) ([A-Za-z]+): ?(.*)$");
    private static final Pattern CLIENT_USER_INFO_CHANGED_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) n\\\\([^\\\\]+)\\\\t\\\\(\\d)(.*)$");
    private static final Pattern KILL_DATA_PATTERN = Pattern.compile("^([0-9]{1,4}) ([0-9]{1,2}) ([0-9]{1,2}): (.*) killed (.*) by [^ ]*$");
    private static final Pattern FLAG_RESULT_DATA_PATTERN = Pattern.compile("^([0-9]{1,2})  blue:([0-9]{1,2})$");
    private static final Pattern SCORE_DATA_PATTERN = Pattern.compile("^([0-9]{1,3})  ping: [0-9]{1,3}  client: ([0-9]{1,2}) (.*)$");
    private static final Pattern ITEM_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) team_CTF_(red|blue)flag$");

    private File gamesFile;
    private Properties gamesProperties;
    private Long currentTime;
    private Game currentGame;

    public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException, ParseException, TemplateException {
        Main main = new Main();
        main.processGames();
        main.generateIndex();
        main.saveGamesProperties();
    }

    public Main() throws IOException {
        gamesFile = new File(OUTPUT_DIRECTORY + "config.properties");
        gamesProperties = new Properties();
        if (gamesFile.exists()) {
            gamesProperties.load(new FileInputStream(gamesFile));
        } else {
            gamesFile.getParentFile().mkdirs();
            gamesFile.createNewFile();
        }
        File gameOutputDirectory = new File(OUTPUT_GAMES_DIRECTORY);
        if (!gameOutputDirectory.exists()) {
            gameOutputDirectory.mkdirs();
        }
        File statsOutputDirectory = new File(OUTPUT_STATS_DIRECTORY);
        if (!statsOutputDirectory.exists()) {
            statsOutputDirectory.mkdirs();
        }
    }

    private void processGames() throws IOException, IllegalAccessException, InvocationTargetException, ParseException {
        List lines = FileUtils.readLines(new File("games.log"));
        for (Object object : lines) {
            String line = StringUtils.trim((String) object);
            Matcher matcher = LINE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                continue;
            }

            currentTime = currentTime == null ? DateUtils.parseDate(matcher.group(1), "m:s").getTime() : currentTime;
            Integer time = Long.valueOf(DateUtils.parseDate(matcher.group(1), "m:s").getTime() - currentTime).intValue() / 1000;
            try {
                MethodUtils.invokeExactMethod(this, "process" + matcher.group(2), new Object[] {time, matcher.group(3)});
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            }
        }
    }

    private void generateIndex() throws IOException, TemplateException {
        Object templateData = Collections.singletonMap("games", gamesProperties);
        FileWriter fileWriter = new FileWriter(new File(OUTPUT_GAMES_DIRECTORY + "index.html"));
        FREEMARKER_CONFIGURATION.getTemplate(INDEX_TEMPLATE_PATH).process(templateData, fileWriter);
        fileWriter.close();
    }

    private void saveGamesProperties() throws IOException {
        gamesProperties.store(new FileOutputStream(gamesFile), "Auto generated, do not modify");
    }

    public void processInitGame(Integer time, String data) throws IOException {
        //System.out.println("InitGame: " + data);
        Queue<String> rawParams = Queues.newArrayDeque(Lists.newArrayList(data.split("\\\\")));
        Map<String, String> params = Maps.newHashMap();

        // Remove the first empty occurrence
        rawParams.remove();
        while (!rawParams.isEmpty()) {
            params.put(rawParams.remove(), rawParams.remove());
        }

        GameType gameType = GameType.values()[Integer.valueOf(params.get("g_gametype"))];
        String mapname = params.get("mapname");
        currentGame = new Game(gameType, mapname);
    }

    public void processClientConnect(Integer time, String data) {
        //System.out.println("ClientConnect: " + data);
        Integer playerNumber = Integer.valueOf(data);
        currentGame.processClientConnect(playerNumber);
    }

    public void processClientUserinfoChanged(Integer time, String data) {
        //System.out.println("ClientUserinfoChanged: " + data);
        Matcher matcher = CLIENT_USER_INFO_CHANGED_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        String name = matcher.group(2);
        Integer teamNumber = Integer.valueOf(matcher.group(3));
        currentGame.processClientUserInfoChanged(playerNumber, name, teamNumber);
    }

    public void processKill(Integer time, String data) {
        //System.out.println("Kill: " + data);
        Matcher matcher = KILL_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        Integer targetNumber = Integer.valueOf(matcher.group(2));
        MeanOfDeath meanOfDeath = MeanOfDeath.values()[Integer.valueOf(matcher.group(3))];
        currentGame.processKill(time, playerNumber, targetNumber, meanOfDeath);
    }

    public void processItem(Integer time, String data) {
        //System.out.println("Item: " + data);
        Matcher matcher = ITEM_DATA_PATTERN.matcher(data);
        if (matcher.matches()) {
            Integer playerNumber = Integer.valueOf(matcher.group(1));
            Team flagTeam = Team.fromFlagColor(matcher.group(2));
            currentGame.processItemFlag(playerNumber, flagTeam);
        }
    }

    public void processred(Integer time, String data) {
        //System.out.println("Red: " + data);
        Matcher matcher = FLAG_RESULT_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return;
        }

        Integer redScore = Integer.valueOf(matcher.group(1));
        Integer blueScore = Integer.valueOf(matcher.group(2));
        currentGame.processTeamScore(redScore, blueScore);
    }

    public void processscore(Integer time, String data) {
        //System.out.println("Score: " + data);
        Matcher matcher = SCORE_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return;
        }

        Integer score = Integer.valueOf(matcher.group(1));
        Integer playerNumber = Integer.valueOf(matcher.group(2));
        currentGame.processScore(playerNumber, score);
    }

    public void processShutdownGame(Integer time, String data) throws IOException, TemplateException {
        if (currentGame.getPlayers().size() < 2) {
            return;
        }

        //System.out.println(currentGame);
        currentGame.processShutdownGame(time);
        currentTime = null;

        Object templateData = Collections.singletonMap("game", currentGame);
        String gameHash = Hashing.md5().hashObject(currentGame, GameFunnel.INSTANCE).toString();

        StringWriter stringWriter = new StringWriter();
        FREEMARKER_CONFIGURATION.getTemplate(TITLE_TEMPLATE_PATH).process(templateData, stringWriter);
        gamesProperties.put(gameHash, stringWriter.toString());

        FileWriter fileWriter = new FileWriter(new File(OUTPUT_GAMES_DIRECTORY + gameHash + ".html"));
        FREEMARKER_CONFIGURATION.getTemplate(GAME_TEMPLATE_PATH).process(templateData, fileWriter);
        fileWriter.close();
    }
}
