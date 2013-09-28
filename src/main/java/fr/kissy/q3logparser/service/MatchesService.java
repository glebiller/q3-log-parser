package fr.kissy.q3logparser.service;

import com.google.common.collect.Maps;
import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.function.MatchesPropertyTransformer;
import fr.kissy.q3logparser.generator.MatchesGenerator;
import fr.kissy.q3logparser.processor.match.MatchProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class MatchesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchesService.class);

    @Autowired
    private Properties matchProperties;
    @Autowired
    private MatchesPropertyTransformer matchesPropertyTransformer;
    @Autowired
    private Map<String, MatchProcessor> matchProcessorMap;
    @Autowired
    private Map<String, MatchesGenerator> matchesGeneratorMap;

    public void process(List<Match> matches) {
        for (Match match : matches) {
            LOGGER.info("Processing match {}", match.getHash());
            for (MatchProcessor matchProcessor : matchProcessorMap.values()) {
                matchProcessor.process(match);
            }
        }
    }

    public void generate() {
        Map<String, Match> matchesData = Maps.transformEntries(Maps.fromProperties(matchProperties), matchesPropertyTransformer);
        for (MatchesGenerator matchesGenerator : matchesGeneratorMap.values()) {
            matchesGenerator.generate(matchesData);
        }
    }
}
