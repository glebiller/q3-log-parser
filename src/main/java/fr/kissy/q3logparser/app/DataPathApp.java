package fr.kissy.q3logparser.app;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.service.GamesLogService;
import fr.kissy.q3logparser.service.MatchesService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Queue;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class DataPathApp {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataPathApp.class);

    @Value("${data-path}")
    private String dataPath;
    @Autowired
    private GamesLogService gamesLogService;
    @Autowired
    private MatchesService matchesService;

    public void walkAndProcess() throws IOException, ParseException {
        File dataPathFile = new File(dataPath);
        if (!dataPathFile.exists() || !dataPathFile.canRead()) {
            return;
        }

        List<Match> matches = Lists.newArrayList();
        Queue<File> filesToProcess = Queues.newArrayDeque();
        filesToProcess.add(dataPathFile);
        while (!filesToProcess.isEmpty()) {
            File file = filesToProcess.poll();

            if (FilenameUtils.isExtension(file.getPath(), "log")) {
                LOGGER.info("Processing file {}", file.getAbsolutePath());
                matches.addAll(gamesLogService.process(file));
            } else if (file.isDirectory()) {
                filesToProcess.addAll(Lists.newArrayList(file.listFiles()));
            }
        }

        LOGGER.info("Processing a total of {} games", matches.size());
        matchesService.process(matches);

        matchesService.generate();
    }
}
