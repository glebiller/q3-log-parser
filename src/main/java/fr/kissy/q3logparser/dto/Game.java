package fr.kissy.q3logparser.dto;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import fr.kissy.q3logparser.dto.enums.GameType;
import fr.kissy.q3logparser.dto.enums.MeanOfDeath;
import fr.kissy.q3logparser.dto.enums.Team;
import fr.kissy.q3logparser.funnel.GameFunnel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Game implements KryoSerializable {
    private static final Integer WORLD_NUMBER = 1022;

    private String date;
    private GameType type;
    private String map;
    private Integer duration;
    private Map<Integer, Player> players = Maps.newHashMap();
    private Map<Team, Integer> teamScore = Maps.newHashMap();

    transient private Set<Team> carriedFlags = Sets.newHashSet();
    transient private Set<Team> pickedUpFlags = Sets.newHashSet();

    public Game() {
    }

    public Game(GameType gameType, String mapName) throws IOException {
        this.type = gameType;
        this.map = mapName;
        this.teamScore.put(Team.TEAM_RED, 0);
        this.teamScore.put(Team.TEAM_BLUE, 0);
    }

    public void processClientConnect(Integer playerNumber) {
        players.put(playerNumber, new Player());
    }

    public Boolean processClientUserInfoChanged(Integer playerNumber, String name, Integer teamNumber) {
        Player player = players.get(playerNumber);
        Team team = Team.values()[teamNumber];
        if (StringUtils.equals(player.getName(), name) && player.getTeam() == team) {
            return false;
        }
        player.update(playerNumber, name, team);
        return true;
    }

    public void processKill(Integer time, Integer playerNumber, Integer targetNumber, MeanOfDeath meanOfDeath) {
        if (Objects.equal(WORLD_NUMBER, playerNumber)) {
            playerNumber = targetNumber;
        }

        Player player = players.get(playerNumber);
        Player target = players.get(targetNumber);

        boolean targetHadFlag = target.getHasFlag();

        // Kill
        player.addKill(new Kill(player, target, time, meanOfDeath));
        target.addKill(new Kill(player, target, time, meanOfDeath));

        // Remove pickedUpFlags if needed
        if (targetHadFlag) {
            Team oppositeTeam = target.getTeam().getOpposite();
            carriedFlags.remove(oppositeTeam);
            if (meanOfDeath == MeanOfDeath.MOD_FALLING) {
                pickedUpFlags.remove(oppositeTeam);
            }
        }
    }

    public void processItemFlag(Integer playerNumber, Team team) {
        Player player = players.get(playerNumber);
        if (player.getTeam().getOpposite() == team) {
            if (carriedFlags.contains(team)) {
                handleLostFlag(player.getTeam().getOpposite());
            }
            if (!player.getHasFlag()) {
                // Pickup
                //System.out.println("Pickedup: " + player.getName() + " " + player.getTeam());
                player.pickupFlag();
                carriedFlags.add(team);
                pickedUpFlags.add(team);
            } else {
                // ERROR
                throw new IllegalStateException();
            }
        } else {
            if (pickedUpFlags.contains(team)) {
                // Return
                //System.out.println("Returned: " + player.getName() + " " + player.getTeam());
                player.returnFlag();
                pickedUpFlags.remove(team);
            } else {
                // Capture
                //System.out.println("Captured: " + player.getName() + " " + player.getTeam());
                player.captureFlag();
                carriedFlags.clear();
                pickedUpFlags.clear();
                // Add one point
                teamScore.put(team, teamScore.get(team) + 1);
            }
        }
    }

    private void handleLostFlag(Team ignore) {
        for (Player player : players.values()) {
            if (!player.getHasFlag()) {
                 continue;
            }
            // Ignore opposite team players
            if (player.getTeam() == ignore) {
                continue;
            }
            player.setHasFlag(false);
            player.getFlag().setReturned(player.getFlag().getReturned() - 1);
            player.getFlag().addCaptured();
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
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public GameType getType() {
        return type;
    }

    public String getTypeName() {
        return type.getName();
    }

    public Boolean getDisplayFlagInfos() {
        return type == GameType.CAPTURE_THE_FLAG;
    }

    public Boolean getDisplayTeamInfos() {
        return type == GameType.CAPTURE_THE_FLAG || type == GameType.TEAM_DEATHMATCH;
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

    public String getFormattedDuration() {
        long minutes = TimeUnit.SECONDS.toMinutes(duration);
        return String.format("%dm %ds",minutes, TimeUnit.SECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes));
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

    public Set<Team> getCarriedFlags() {
        return carriedFlags;
    }

    public void setCarriedFlags(Set<Team> carriedFlags) {
        this.carriedFlags = carriedFlags;
    }

    public Set<Team> getPickedUpFlags() {
        return pickedUpFlags;
    }

    public void setPickedUpFlags(Set<Team> pickedUpFlags) {
        this.pickedUpFlags = pickedUpFlags;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(date);
        kryo.writeObject(output, type);
        output.writeString(map);
        output.writeInt(duration);
        kryo.writeObject(output, players);
        kryo.writeObject(output, teamScore);
        kryo.writeObject(output, carriedFlags);
        kryo.writeObject(output, pickedUpFlags);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(Kryo kryo, Input input) {
        date = input.readString();
        type = kryo.readObject(input, GameType.class);
        map = input.readString();
        duration = input.readInt();
        players = kryo.readObject(input, HashMap.class);
        teamScore = kryo.readObject(input, HashMap.class);
        carriedFlags = kryo.readObject(input, HashSet.class);
        pickedUpFlags = kryo.readObject(input, HashSet.class);
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
