package fr.kissy.q3logparser.generator.impl;

import com.google.common.collect.Maps;
import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.dto.Player;
import fr.kissy.q3logparser.dto.Stats;
import fr.kissy.q3logparser.generator.MatchesGenerator;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
public class StatsGenerator implements MatchesGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsGenerator.class);
    private static final String STATS_TEMPLATE_PATH = "/fr/kissy/q3logparser/stats.ftl";
    private static final String TEMPLATE_MATCHES_COUNT_KEY = "matchesCount";
    private static final String TEMPLATE_STATS_KEY = "stats";
    private static final String FILE_NAME = "index.html";

    @Value("${data-directory}")
    private String dataDirectory;
    @Value("${stats-directory}")
    private String statsDirectory;
    @Value("${generate-stats}")
    private String generateStats;
    @Autowired
    private Configuration configuration;

    /**
     * @inheritDoc
     */
    @Override
    public void generate(Map<String, Match> matchesData) {
        if (!Boolean.parseBoolean(generateStats)) {
            return;
        }

        Map<String, Stats> stats = Maps.newHashMap();
        for (Map.Entry<String, Match> entry : matchesData.entrySet()) {
            entry.getValue().processStats(stats);
        }
        Map<String, Object> templateData = Maps.newHashMap();
        templateData.put(TEMPLATE_STATS_KEY, stats);
        templateData.put(TEMPLATE_MATCHES_COUNT_KEY, matchesData.size());

        try {
            LOGGER.info("Generating stats page");
            File file = new File(dataDirectory + File.separator + statsDirectory + File.separator + FILE_NAME);
            if (file.exists()) {
                FileUtils.forceDelete(file);
            } else {
                FileUtils.forceMkdir(file.getParentFile());
            }

            FileWriter fileWriter = new FileWriter(file);
            configuration.getTemplate(STATS_TEMPLATE_PATH).process(templateData, fileWriter);
            fileWriter.close();
        } catch (TemplateException e) {
            LOGGER.error("Error while generating stats page");
        } catch (IOException e) {
            LOGGER.error("Error while writing stats page");
        }
    }
}
