package fr.kissy.q3logparser.generator.impl;

import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.generator.MatchesGenerator;
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
import java.util.Map;

/**
 * @author Guillaume Le Biller (<i>lebiller@ekino.com</i>)
 * @version $Id$
 */
public class IndexGenerator implements MatchesGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexGenerator.class);
    private static final String INDEX_TEMPLATE_PATH = "/fr/kissy/q3logparser/index.ftl";
    private static final String TEMPLATE_MATCHES_KEY = "matches";
    private static final String FILE_NAME = "index.html";

    @Value("${data-directory}")
    private String dataDirectory;
    @Value("${matches-directory}")
    private String matchesDirectory;
    @Value("${generate-index}")
    private String generateIndex;
    @Autowired
    private Configuration configuration;

    /**
     * @inheritDoc
     */
    @Override
    public void generate(Map<String, Match> matchesData) {
        if (!Boolean.parseBoolean(generateIndex)) {
            return;
        }

        Object templateData = Collections.singletonMap(TEMPLATE_MATCHES_KEY, matchesData);

        try {
            LOGGER.info("Generating index page");
            File file = new File(dataDirectory + File.separator + matchesDirectory + File.separator + FILE_NAME);
            if (file.exists()) {
                FileUtils.forceDelete(file);
            } else {
                FileUtils.forceMkdir(file.getParentFile());
            }

            FileWriter fileWriter = new FileWriter(file);
            configuration.getTemplate(INDEX_TEMPLATE_PATH).process(templateData, fileWriter);
            fileWriter.close();
        } catch (TemplateException e) {
            LOGGER.error("Error while generating index page");
        } catch (IOException e) {
            LOGGER.error("Error while writing index page");
        }
    }
}
