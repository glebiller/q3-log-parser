package fr.kissy.q3logparser.service;

import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.processor.line.LineProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class GamesLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GamesLogService.class);
    private static final Pattern LINE_PATTERN = Pattern.compile("^([0-9]{1,2}:[0-9]{2}) ([A-Za-z]+): ?(.*)$");
    private static final String BEAN_NAME_PREFIX = "lineProcessor";
    private static final String TIME_MATCHER = "m:s";

    @Autowired
    private Map<String, LineProcessor> lineProcessors;

    /**
     * Process a games log file and extract game data.
     *
     * @param gamesLogFile The game log file to process.
     * @throws java.io.IOException Any exception that could occurs.
     * @throws ParseException Any exception that could occurs.
     */
    public List<Match> process(File gamesLogFile) throws IOException, ParseException {
        Processing processing = new Processing();

        Long startingTime = null;
        List lines = FileUtils.readLines(gamesLogFile);
        for (Object object : lines) {
            String currentLine = StringUtils.trim((String) object);
            Matcher matcher = LINE_PATTERN.matcher(currentLine);
            if (!matcher.matches()) {
                continue;
            }

            startingTime = startingTime == null ? DateUtils.parseDate(matcher.group(1), TIME_MATCHER).getTime() : startingTime;
            processing.setCurrentLine(currentLine);
            processing.setCurrentTime((DateUtils.parseDate(matcher.group(1), TIME_MATCHER).getTime() - startingTime) / 1000L);
            processing.setData(matcher.group(3));

            String beanName = BEAN_NAME_PREFIX + matcher.group(2);
            if (lineProcessors.containsKey(beanName)) {
                lineProcessors.get(beanName).process(processing);
            }
        }

        LOGGER.info("{} matches found", processing.getMatches().size());
        return processing.getMatches();
    }
}
