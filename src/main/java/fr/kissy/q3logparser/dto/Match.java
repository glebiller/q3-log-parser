package fr.kissy.q3logparser.dto;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fr.kissy.q3logparser.enums.EnumGameType;
import fr.kissy.q3logparser.enums.EnumTeam;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Match implements KryoSerializable {
    private String hash;
    private String date;
    private EnumGameType type;
    private String map;
    private Long duration;
    private Map<Integer, Player> players = Maps.newHashMap();
    private Map<EnumTeam, Integer> teamScore = Maps.newHashMap();

    private transient Set<EnumTeam> carriedFlags = Sets.newHashSet();
    private transient Set<EnumTeam> pickedUpFlags = Sets.newHashSet();
    private transient StringBuilder rawData = new StringBuilder();

    public Match() {
    }

    public Match(EnumGameType gameType, String mapName) {
        this.type = gameType;
        this.map = mapName;
        this.teamScore.put(EnumTeam.TEAM_RED, 0);
        this.teamScore.put(EnumTeam.TEAM_BLUE, 0);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EnumGameType getType() {
        return type;
    }

    public String getTypeName() {
        return type.getName();
    }

    public Boolean getDisplayFlagInfos() {
        return type == EnumGameType.CAPTURE_THE_FLAG;
    }

    public Boolean getDisplayTeamInfos() {
        return type == EnumGameType.CAPTURE_THE_FLAG || type == EnumGameType.TEAM_DEATHMATCH;
    }

    public void setType(EnumGameType type) {
        this.type = type;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Long getDuration() {
        return duration;
    }

    public String getFormattedDuration() {
        long minutes = TimeUnit.SECONDS.toMinutes(duration);
        return String.format("%dm %ds",minutes, TimeUnit.SECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes));
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public Map<EnumTeam, Integer> getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(Map<EnumTeam, Integer> teamScore) {
        this.teamScore = teamScore;
    }

    public Integer getRedTeamScore() {
        return this.teamScore.get(EnumTeam.TEAM_RED);
    }

    public Integer getBlueTeamScore() {
        return this.teamScore.get(EnumTeam.TEAM_BLUE);
    }

    public Set<EnumTeam> getCarriedFlags() {
        return carriedFlags;
    }

    public void setCarriedFlags(Set<EnumTeam> carriedFlags) {
        this.carriedFlags = carriedFlags;
    }

    public Set<EnumTeam> getPickedUpFlags() {
        return pickedUpFlags;
    }

    public void setPickedUpFlags(Set<EnumTeam> pickedUpFlags) {
        this.pickedUpFlags = pickedUpFlags;
    }

    public StringBuilder getRawData() {
        return rawData;
    }

    public void setRawData(StringBuilder rawData) {
        this.rawData = rawData;
    }

    public boolean isFinished() {
        return StringUtils.isNotBlank(hash);
    }

    public void processStats(Map<String, Stats> stats) {
        for (Player player : players.values()) {
            // Skip players that did not played
            if (player.getStartPlaying() == null) {
                continue;
            }
            // Skip "UnamedPlayer"
            if (StringUtils.equalsIgnoreCase(player.getName(), "UnnamedPlayer")) {
                continue;
            }
            if (!stats.containsKey(player.getName())) {
                stats.put(player.getName(), new Stats(player.getName()));
            }
            Stats playerStats = stats.get(player.getName());
            playerStats.addGameStats(date, player);
        }
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(hash);
        output.writeString(date);
        kryo.writeObject(output, type);
        output.writeString(map);
        output.writeLong(duration);
        kryo.writeObject(output, players);
        kryo.writeObject(output, teamScore);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(Kryo kryo, Input input) {
        hash = input.readString();
        date = input.readString();
        type = kryo.readObject(input, EnumGameType.class);
        map = input.readString();
        duration = input.readLong();
        players = kryo.readObject(input, HashMap.class);
        teamScore = kryo.readObject(input, HashMap.class);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Match){
            final Match match = (Match) o;
            return Objects.equal(type, match.type)
                    && Objects.equal(map, match.map)
                    && Objects.equal(duration, match.duration)
                    && Objects.equal(players, match.players);
        } else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, map, duration, players);
    }
}
