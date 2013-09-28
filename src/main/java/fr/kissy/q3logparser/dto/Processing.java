package fr.kissy.q3logparser.dto;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class Processing {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processing.class);

    private List<Match> matches = Lists.newArrayList();
    private Match currentMatch;
    private String currentLine;
    private Long startingTime;
    private Long currentTime;
    private String data;

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public void setCurrentMatch(Match currentMatch) {
        this.currentMatch = currentMatch;
    }

    public String getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(String currentLine) {
        this.currentLine = currentLine;
    }

    public Long getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Long startingTime) {
        this.startingTime = startingTime;
    }

    public Long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void finishCurrentMatch() {
        // Only process matches longer than 2 minutes
        if (currentMatch.getDuration() < 120) {
            LOGGER.warn("Match {} is too short, ignoring it", currentMatch.getHash());
        } else {
            matches.add(currentMatch);
        }
        currentMatch = null;
    }
}
