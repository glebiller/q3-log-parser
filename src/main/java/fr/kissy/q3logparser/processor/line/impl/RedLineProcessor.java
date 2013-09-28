package fr.kissy.q3logparser.processor.line.impl;

import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.enums.EnumTeam;
import fr.kissy.q3logparser.processor.line.LineProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class RedLineProcessor implements LineProcessor {
    private static final Pattern FLAG_RESULT_DATA_PATTERN = Pattern.compile("^([0-9]{1,2})  blue:([0-9]{1,2})$");

    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        Matcher matcher = FLAG_RESULT_DATA_PATTERN.matcher(processing.getData());
        if (!matcher.matches()) {
            return;
        }

        Integer redScore = Integer.valueOf(matcher.group(1));
        Integer blueScore = Integer.valueOf(matcher.group(2));
        processing.getCurrentMatch().getTeamScore().put(EnumTeam.TEAM_RED, redScore);
        processing.getCurrentMatch().getTeamScore().put(EnumTeam.TEAM_BLUE, blueScore);
        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }
}
