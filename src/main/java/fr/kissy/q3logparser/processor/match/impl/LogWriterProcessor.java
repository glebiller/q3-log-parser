package fr.kissy.q3logparser.processor.match.impl;

import com.esotericsoftware.kryo.io.Output;
import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.processor.match.MatchProcessor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class LogWriterProcessor implements MatchProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogWriterProcessor.class);
    private static final Object FILE_NAME = "match.log";

    @Value("${data-directory}")
    private String dataDirectory;
    @Value("${matches-directory}")
    private String matchesDirectory;

    /**
     * @inheritDoc
     */
    @Override
    public void process(Match match) {
        try {
            File file = new File(dataDirectory + File.separator + matchesDirectory + File.separator + match.getHash() + File.separator + FILE_NAME);
            if (file.exists()) {
                return;
            }

            FileUtils.forceMkdir(file.getParentFile());
            FileUtils.writeStringToFile(file, match.getRawData().toString());

            LOGGER.info("Log file written for match {}", match.getHash());
        } catch (IOException e) {
            LOGGER.error("Error while creating log file for match {}", match.getHash());
        }
    }
}
