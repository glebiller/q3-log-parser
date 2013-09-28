package fr.kissy.q3logparser.processor.line.impl;

import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.processor.line.LineProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class ScoreLineProcessor implements LineProcessor {
    private static final Pattern SCORE_DATA_PATTERN = Pattern.compile("^([0-9]{1,3})  ping: [0-9]{1,3}  client: ([0-9]{1,2}) (.*)$");

    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        Matcher matcher = SCORE_DATA_PATTERN.matcher(processing.getData());
        if (!matcher.matches()) {
            return;
        }

        Integer score = Integer.valueOf(matcher.group(1));
        Integer playerNumber = Integer.valueOf(matcher.group(2));
        processing.getCurrentMatch().getPlayers().get(playerNumber).setScore(score);
        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }
}
