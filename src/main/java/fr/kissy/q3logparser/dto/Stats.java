package fr.kissy.q3logparser.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Stats {
    private String player;
    private Integer matches = 0;
    private Integer score = 0;
    private Integer frags = 0;
    private Integer deaths = 0;
    private Integer suicides = 0;

    public Stats(String player) {
        this.player = player;
    }

    private Map<String, List<Integer>> dailyScore = Maps.newHashMap();
    private Map<String, List<Integer>> dailyFrags = Maps.newHashMap();
    private Map<String, List<Integer>> dailyDeaths = Maps.newHashMap();
    private Map<String, List<Integer>> dailySuicides = Maps.newHashMap();

    public void addGameStats(String date, Player player) {
        if (!dailyScore.containsKey(date)) {
            dailyScore.put(date, Lists.<Integer>newArrayList());
        }
        if (!dailyFrags.containsKey(date)) {
            dailyFrags.put(date, Lists.<Integer>newArrayList());
        }
        if (!dailyDeaths.containsKey(date)) {
            dailyDeaths.put(date, Lists.<Integer>newArrayList());
        }
        if (!dailySuicides.containsKey(date)) {
            dailySuicides.put(date, Lists.<Integer>newArrayList());
        }

        ++matches;
        score += player.getScore();
        dailyScore.get(date).add(player.getScore());
        frags += player.getFrags().size();
        dailyFrags.get(date).add(player.getFrags().size());
        deaths += player.getDeaths().size();
        dailyDeaths.get(date).add(player.getDeaths().size());
        suicides += player.getSuicides().size();
        dailySuicides.get(date).add(player.getSuicides().size());
    }

    public void addScore(Integer score) {
        this.score += score;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Integer getMatches() {
        return matches;
    }

    public void setMatches(Integer matches) {
        this.matches = matches;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getFrags() {
        return frags;
    }

    public void setFrags(Integer frags) {
        this.frags = frags;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getSuicides() {
        return suicides;
    }

    public void setSuicides(Integer suicides) {
        this.suicides = suicides;
    }

    public Map<String, List<Integer>> getDailyScore() {
        return dailyScore;
    }

    public void setDailyScore(Map<String, List<Integer>> dailyScore) {
        this.dailyScore = dailyScore;
    }

    public Map<String, List<Integer>> getDailyFrags() {
        return dailyFrags;
    }

    public void setDailyFrags(Map<String, List<Integer>> dailyFrags) {
        this.dailyFrags = dailyFrags;
    }

    public Map<String, List<Integer>> getDailyDeaths() {
        return dailyDeaths;
    }

    public void setDailyDeaths(Map<String, List<Integer>> dailyDeaths) {
        this.dailyDeaths = dailyDeaths;
    }

    public Map<String, List<Integer>> getDailySuicides() {
        return dailySuicides;
    }

    public void setDailySuicides(Map<String, List<Integer>> dailySuicides) {
        this.dailySuicides = dailySuicides;
    }
}
