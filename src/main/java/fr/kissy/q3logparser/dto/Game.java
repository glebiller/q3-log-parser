package fr.kissy.q3logparser.dto;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import fr.kissy.q3logparser.dto.enums.GameType;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import fr.kissy.q3logparser.dto.enums.Team;
import fr.kissy.q3logparser.dto.funnel.GameFunnel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Game {
    private static final Integer WORLD_NUMBER = 1022;

    private GameType type;
    private String map;
    private Integer duration;
    private Map<Integer, Player> players = Maps.newHashMap();
    private Map<Team, Integer> teamScore = Maps.newHashMap();

    transient private Set<Team> pickedUpFlags = Sets.newHashSet();

    public Game(GameType gameType, String mapname) {
        this.type = gameType;
        this.map = mapname;
    }

    public void processClientConnect(Integer playerNumber) {
        players.put(playerNumber, new Player());
    }

    public void processClientUserInfoChanged(Integer playerNumber, String name, Integer teamNumber) {
        players.get(playerNumber).update(name, teamNumber);
    }

    public void processKill(Integer time, Integer playerNumber, Integer targetNumber, MeanOfDeath meanOfDeath) {
        if (Objects.equal(WORLD_NUMBER, playerNumber)) {
            playerNumber = targetNumber;
        }

        Player player = players.get(playerNumber);
        Player target = players.get(targetNumber);

        // Kill
        player.addKill(new Kill(player, target, time, meanOfDeath));
        target.addKill(new Kill(player, target, time, meanOfDeath));

        // Remove pickedUpFlags if needed
        if (target.getHasFlag()) {
            pickedUpFlags.remove(player.getTeam());
        }
    }

    public void processItemFlag(Integer playerNumber, Team team) {
        Player player = players.get(playerNumber);
        if (player.getTeam() != team) {
            if (!player.getHasFlag()) {
                // Pickup
                player.pickupFlag();
                pickedUpFlags.add(team);
            } else {
                // ERROR
                throw new IllegalStateException();
            }
        } else {
            if (pickedUpFlags.contains(team)) {
                // Return
                player.returnFlag();
                pickedUpFlags.remove(team);
            } else {
                // Capture
                player.captureFlag();
                pickedUpFlags.clear();
            }
        }
    }

    public void processTeamScore(Integer redScore, Integer blueScore) {
        teamScore.put(Team.TEAM_RED, redScore);
        teamScore.put(Team.TEAM_BLUE, blueScore);
    }

    public void processScore(Integer playerNumber, Integer score) {
        Player player = players.get(playerNumber);
        player.setScore(score);
    }

    public void processShutdownGame(Integer time) {
        this.duration = time;
        try {
            //System.out.println(this);
            Configuration configuration = new Configuration();
            Template template = configuration.getTemplate("src/main/resources/fr/kissy/q3logparser/results.ftl");
            Map<String, Object> data = Maps.newHashMap();
            data.put("game", this);
            Writer file = new FileWriter(new File("target/" + Hashing.md5().hashObject(this, GameFunnel.INSTANCE) + ".html"));
            template.process(data, file);
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameType getType() {
        return type;
    }

    public String getTypeName() {
        return type.getName();
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public Map<Team, Integer> getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(Map<Team, Integer> teamScore) {
        this.teamScore = teamScore;
    }

    public Integer getRedTeamScore() {
        return this.teamScore.get(Team.TEAM_RED);
    }

    public Integer getBlueTeamScore() {
        return this.teamScore.get(Team.TEAM_BLUE);
    }

    public Set<Team> getPickedUpFlags() {
        return pickedUpFlags;
    }

    public void setPickedUpFlags(Set<Team> pickedUpFlags) {
        this.pickedUpFlags = pickedUpFlags;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Game){
            final Game game = (Game) o;
            return Objects.equal(type, game.type)
                    && Objects.equal(map, game.map)
                    && Objects.equal(duration, game.duration)
                    && Objects.equal(players, game.players);
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, map, duration, players);
    }
}
