package fr.kissy.q3logparser;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import fr.kissy.q3logparser.dto.Game;
import fr.kissy.q3logparser.dto.Stats;
import fr.kissy.q3logparser.dto.enums.GameType;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import fr.kissy.q3logparser.dto.enums.Team;
import fr.kissy.q3logparser.function.GamesPropertyTransformer;
import fr.kissy.q3logparser.funnel.GameFunnel;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@SuppressWarnings("UnusedDeclaration")
public class Main {

    private static final String ARCHIVE_DIRECTORY = "_logs" + File.separator;
    private static final String OUTPUT_DIRECTORY = "_data" + File.separator;
    private static final String OUTPUT_GAMES_DIRECTORY = OUTPUT_DIRECTORY + "games" + File.separator;
    private static final String OUTPUT_STATS_DIRECTORY = OUTPUT_DIRECTORY + "stats" + File.separator;
    private static final String OUTPUT_DATA_PROPERTY_FILE = OUTPUT_DIRECTORY + "data.properties";
    private static final String OUTPUT_ALIAS_PROPERTY_FILE = OUTPUT_DIRECTORY + "alias.properties";
    private static final String OUTPUT_CURRENT_GAME_FILE = OUTPUT_DIRECTORY + "games.log.tmp";

    private static final String TITLE_TEMPLATE_PATH = "src/main/resources/fr/kissy/q3logparser/includes/title.ftl";
    private static final String GAME_TEMPLATE_PATH = "src/main/resources/fr/kissy/q3logparser/results.ftl";
    private static final String INDEX_TEMPLATE_PATH = "src/main/resources/fr/kissy/q3logparser/index.ftl";

    private static final Kryo KRYO = new Kryo();
    private static final Configuration FREEMARKER_CONFIGURATION = new Configuration();

    private static final Pattern LINE_PATTERN = Pattern.compile("^([0-9]{1,2}:[0-9]{2}) ([A-Za-z]+): ?(.*)$");
    private static final Pattern CLIENT_USER_INFO_CHANGED_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) n\\\\([^\\\\]+)\\\\t\\\\(\\d)(.*)$");
    private static final Pattern KILL_DATA_PATTERN = Pattern.compile("^([0-9]{1,4}) ([0-9]{1,2}) ([0-9]{1,2}): (.*) killed (.*) by [^ ]*$");
    private static final Pattern FLAG_RESULT_DATA_PATTERN = Pattern.compile("^([0-9]{1,2})  blue:([0-9]{1,2})$");
    private static final Pattern SCORE_DATA_PATTERN = Pattern.compile("^([0-9]{1,3})  ping: [0-9]{1,3}  client: ([0-9]{1,2}) (.*)$");
    private static final Pattern ITEM_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) team_CTF_(red|blue)flag$");

    private static final String DATE_FORMAT = "dd/MM/yy";
    private static final Integer MIN_GAME_PLAYERS = 2;
    private static final Integer MIN_GAME_DURATION = 300;

    // Parameters
    private File gamesLogFile;
    private Boolean archive;
    private Boolean regenerate;

    // Properties
    private Map<String, Stats> stats = Maps.newHashMap();
    private File aliasPropertiesFile;
    private Properties aliasProperties = new Properties();
    private File dataPropertiesFile;
    private Properties dataProperties = new Properties();
    private File currentGameFile;
    private Long currentTime;
    private Game currentGame;

    public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException, ParseException, TemplateException {
        if (args.length < 1) {
            System.out.println("Usage \"java -jar q3-log-parser.jar path/of/games/log [archive:true|false] [regenerate:true|false]\"");
            return;
        }

        Main main = new Main(args);
        main.processGames();
        main.generateGameIndex();
        main.processStats();
        main.saveAndArchive();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Main(String[] args) throws IOException {
        // Parameters
        gamesLogFile = new File(args[0]);
        if (!gamesLogFile.exists()) {
            throw new IllegalArgumentException("No log files found with path " + args[0]);
        }
        if (args.length >= 2) {
            archive = Boolean.valueOf(args[1]);
        } else {
            archive = true;
        }
        if (args.length >= 3) {
            regenerate = Boolean.valueOf(args[2]);
        } else {
            regenerate = false;
        }

        // Properties
        aliasPropertiesFile = new File(OUTPUT_ALIAS_PROPERTY_FILE);
        if (!aliasPropertiesFile.exists()) {
            aliasPropertiesFile.createNewFile();
        }
        aliasProperties.load(new FileInputStream(aliasPropertiesFile));

        dataPropertiesFile = new File(OUTPUT_DATA_PROPERTY_FILE);
        if (!dataPropertiesFile.exists()) {
            dataPropertiesFile.createNewFile();
        }
        dataProperties.load(new FileInputStream(dataPropertiesFile));

        currentGameFile = new File(OUTPUT_CURRENT_GAME_FILE);
        currentGameFile.createNewFile();
        currentGameFile.deleteOnExit();

        for (String directories : Lists.newArrayList(OUTPUT_GAMES_DIRECTORY, OUTPUT_STATS_DIRECTORY)) {
            File gameOutputDirectory = new File(directories);
            if (!gameOutputDirectory.exists()) {
                gameOutputDirectory.mkdirs();
            }
        }
    }

    private void processGames() throws IOException, IllegalAccessException, InvocationTargetException, ParseException {
        System.out.println("Processing games log");
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
                Object result = MethodUtils.invokeExactMethod(this, "process" + matcher.group(2), new Object[]{time, matcher.group(3)});
                if (BooleanUtils.isTrue((Boolean) result)) {
                    Files.append(line + "\n", currentGameFile, Charset.defaultCharset());
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateGameIndex() throws IOException, TemplateException {
        System.out.println("Generating game index");
        Object templateData = Collections.singletonMap("games", Maps.transformEntries(
            Maps.fromProperties(dataProperties), new GamesPropertyTransformer(OUTPUT_GAMES_DIRECTORY, KRYO)
        ));
        FileWriter fileWriter = new FileWriter(new File(OUTPUT_GAMES_DIRECTORY + "index.html"));
        FREEMARKER_CONFIGURATION.getTemplate(INDEX_TEMPLATE_PATH).process(templateData, fileWriter);
        fileWriter.close();
    }

    private void processStats() {
        System.out.println("Processing stats");
        Map<String, Game> games = Maps.transformEntries(
                Maps.fromProperties(dataProperties), new GamesPropertyTransformer(OUTPUT_GAMES_DIRECTORY, KRYO)
        );
        for (Map.Entry<String, Game> entry : games.entrySet()) {
            entry.getValue().processStats(stats);
        }
        System.out.println(stats);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveAndArchive() throws IOException {
        System.out.println("Saving data file & archiving log file");
        dataProperties.store(new FileOutputStream(dataPropertiesFile), "Auto generated, do not modify");

        if (archive) {
            int i = 0;
            File file;
            do {
                ++i;
                file = new File(ARCHIVE_DIRECTORY + FastDateFormat.getInstance(DATE_FORMAT.replace("/", "-")).format(new Date()) + "-games-" + i + ".log");
            } while (file.exists());
            gamesLogFile.renameTo(file);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Boolean processInitGame(Integer time, String data) throws IOException {
        //System.out.println("InitGame: " + data);
        Queue<String> rawParams = Queues.newArrayDeque(Lists.newArrayList(data.split("\\\\")));
        Map<String, String> params = Maps.newHashMap();

        // Remove the first empty occurrence
        rawParams.remove();
        while (!rawParams.isEmpty()) {
            params.put(rawParams.remove(), rawParams.remove());
        }

        GameType gameType = GameType.values()[Integer.valueOf(params.get("g_gametype"))];
        String mapName = params.get("mapname");
        currentGame = new Game(gameType, mapName);
        return true;
    }

    public Boolean processClientConnect(Integer time, String data) {
        //System.out.println("ClientConnect: " + data);
        Integer playerNumber = Integer.valueOf(data);
        currentGame.processClientConnect(playerNumber);
        return true;
    }

    public Boolean processClientBegin(Integer time, String data) {
        //System.out.println("ClientConnect: " + data);
        Integer playerNumber = Integer.valueOf(data);
        currentGame.processClientBegin(playerNumber, time);
        return true;
    }

    public Boolean processClientDisconnect(Integer time, String data) {
        return false;
    }

    public Boolean processClientUserinfoChanged(Integer time, String data) {
        //System.out.println("ClientUserinfoChanged: " + data);
        Matcher matcher = CLIENT_USER_INFO_CHANGED_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        String name = matcher.group(2);
        Integer teamNumber = Integer.valueOf(matcher.group(3));
        // Get alias if needed
        if (aliasProperties.containsKey(name)) {
            name = aliasProperties.getProperty(name);
        }
        return currentGame.processClientUserInfoChanged(playerNumber, name, teamNumber);
    }

    public Boolean processKill(Integer time, String data) {
        //System.out.println("Kill: " + data);
        Matcher matcher = KILL_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        Integer targetNumber = Integer.valueOf(matcher.group(2));
        MeanOfDeath meanOfDeath = MeanOfDeath.values()[Integer.valueOf(matcher.group(3))];
        currentGame.processKill(time, playerNumber, targetNumber, meanOfDeath);
        return true;
    }

    public Boolean processItem(Integer time, String data) {
        //System.out.println("Item: " + data);
        Matcher matcher = ITEM_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        Team flagTeam = Team.fromFlagColor(matcher.group(2));
        currentGame.processItemFlag(playerNumber, flagTeam);
        return true;
    }

    public Boolean processsay(Integer time, String data) {
        return true;
    }

    public Boolean processsayteam(Integer time, String data) {
        return true;
    }

    public Boolean processred(Integer time, String data) {
        //System.out.println("Red: " + data);
        Matcher matcher = FLAG_RESULT_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Integer redScore = Integer.valueOf(matcher.group(1));
        Integer blueScore = Integer.valueOf(matcher.group(2));
        currentGame.processTeamScore(redScore, blueScore);
        return true;
    }

    public Boolean processscore(Integer time, String data) {
        //System.out.println("Score: " + data);
        Matcher matcher = SCORE_DATA_PATTERN.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Integer score = Integer.valueOf(matcher.group(1));
        Integer playerNumber = Integer.valueOf(matcher.group(2));
        currentGame.processScore(playerNumber, score);
        return true;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Boolean processShutdownGame(Integer time, String data) throws IOException, TemplateException {
        //System.out.println(currentGame);
        currentGame.processShutdownGame(time);
        currentTime = null;

        if (currentGame.getPlayers().size() < MIN_GAME_PLAYERS || currentGame.getDuration() < MIN_GAME_DURATION) {
            currentGameFile.delete();
            currentGameFile.createNewFile();
            return false;
        }

        String gameHash = Hashing.md5().hashObject(currentGame, GameFunnel.INSTANCE).toString();
        if (dataProperties.containsKey(gameHash)) {
            if (regenerate) {
                currentGame.setDate((String) dataProperties.get(gameHash));
            } else {
                // Do not process games twice.
                currentGameFile.delete();
                currentGameFile.createNewFile();
                return false;
            }
        }

        System.out.println("Generating game " + gameHash);
        Object templateData = Collections.singletonMap("game", currentGame);

        currentGame.setDate(FastDateFormat.getInstance(DATE_FORMAT).format(new Date()));
        dataProperties.put(gameHash, currentGame.getDate());

        // Create directory
        new File(OUTPUT_GAMES_DIRECTORY + gameHash + File.separator).mkdirs();

        // Write index.html
        FileWriter fileWriter = new FileWriter(OUTPUT_GAMES_DIRECTORY + gameHash + File.separator + "index.html");
        FREEMARKER_CONFIGURATION.getTemplate(GAME_TEMPLATE_PATH).process(templateData, fileWriter);
        fileWriter.close();

        // Write game.bin
        Output output = new Output(new FileOutputStream(OUTPUT_GAMES_DIRECTORY + gameHash + File.separator + "game.bin"));
        KRYO.writeObject(output, currentGame);
        output.close();

        // Write games.log
        File outputGamesLog = new File(OUTPUT_GAMES_DIRECTORY + gameHash + File.separator + "game.log");
        outputGamesLog.delete();
        currentGameFile.renameTo(outputGamesLog);
        currentGameFile.createNewFile();
        return true;
    }

    public Boolean processExit(Integer time, String data) {
        return false;
    }

}
