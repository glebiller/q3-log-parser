package fr.kissy.q3logparser.processor.match.impl;

import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.processor.match.MatchProcessor;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class HtmlWriterProcessor implements MatchProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlWriterProcessor.class);
    private static final String HTML_TEMPLATE_PATH = "/fr/kissy/q3logparser/match.ftl";
    private static final Object FILE_NAME = "index.html";

    @Value("${data-directory}")
    private String dataDirectory;
    @Value("${matches-directory}")
    private String matchesDirectory;
    @Value("${generate-matches}")
    private String generateMatches;

    @Autowired
    private Configuration configuration;

    /**
     * @inheritDoc
     */
    @Override
    public void process(Match match) {
        if (!Boolean.parseBoolean(generateMatches)) {
            return;
        }

        try {
            File file = new File(dataDirectory + File.separator + matchesDirectory + File.separator + match.getHash() + File.separator + FILE_NAME);
            if (file.exists()) {
                FileUtils.forceDelete(file);
            } else {
                FileUtils.forceMkdir(file.getParentFile());
            }

            FileWriter fileWriter = new FileWriter(file);
            Object templateData = Collections.singletonMap("match", match);
            configuration.getTemplate(HTML_TEMPLATE_PATH).process(templateData, fileWriter);
            fileWriter.close();

            LOGGER.info("Log file written for match {}", match.getHash());
        } catch (IOException e) {
            LOGGER.error("Error while creating html file for match {}", match.getHash());
        } catch (TemplateException e) {
            LOGGER.error("Error while parsing template file for match {}", match.getHash());
        }
    }
}
