package fr.kissy.q3logparser;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import fr.kissy.q3logparser.dto.Game;
import fr.kissy.q3logparser.dto.enums.GameType;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import fr.kissy.q3logparser.dto.enums.Team;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Main {

    private static final Pattern LINE_PATTERN = Pattern.compile("^([0-9]{1,2}:[0-9]{2}) ([A-Za-z]+): ?(.*)$");
    private static final Pattern CLIENT_USER_INFO_CHANGED_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) n\\\\([^\\\\]+)\\\\t\\\\(\\d)(.*)$");
    private static final Pattern KILL_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) ([0-9]{1,2}) ([0-9]{1,2}): (.*) killed (.*) by [^ ]*$");
    private static final Pattern FLAG_RESULT_DATA_PATTERN = Pattern.compile("^([0-9]{1,2})  blue:([0-9]{1,2})$");
    private static final Pattern SCORE_DATA_PATTERN = Pattern.compile("^([0-9]{1,3})  ping: [0-9]{1,3}  client: ([0-9]{1,2}) (.*)$");
    private static final Pattern ITEM_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) team_CTF_(red|blue)flag$");

    private Long currentTime;
    private Game currentGame;

    public static void main(String[] args) throws IOException, IllegalAccessException, InvocationTargetException, ParseException {
        new Main().run();
    }

    private void run() throws IOException, IllegalAccessException, InvocationTargetException, ParseException {
        List lines = FileUtils.readLines(new File("games.log"));
        for (Object object : lines) {
            String line = StringUtils.trim((String) object);
            Matcher matcher = LINE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                continue;
            }

            currentTime = currentTime == null ? DateUtils.parseDate(matcher.group(1), "m:s").getTime() : currentTime;
            Integer time = Long.valueOf(DateUtils.parseDate(matcher.group(1), "m:s").getTime() - currentTime).intValue();
            try {
                MethodUtils.invokeExactMethod(this, "process" + matcher.group(2), new Object[] {time, matcher.group(3)});
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            }
        }
    }

    public void processInitGame(Integer time, String data) {
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

    public void processShutdownGame(Integer time, String data) {
        if (currentGame.getPlayers().size() < 2) {
            return;
        }

        currentGame.processShutdownGame(time);
    }
}
