package fr.kissy.q3logparser.config;

import fr.kissy.q3logparser.generator.impl.IndexGenerator;
import fr.kissy.q3logparser.generator.impl.StatsGenerator;
import fr.kissy.q3logparser.processor.match.impl.BinaryWriterProcessor;
import fr.kissy.q3logparser.processor.match.impl.HtmlWriterProcessor;
import fr.kissy.q3logparser.processor.match.impl.LogWriterProcessor;
import fr.kissy.q3logparser.processor.match.impl.PropertiesWriterProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@Configuration("matchGeneratorConfig")
public class MatchesGeneratorConfig {
    @Bean
    public IndexGenerator indexGenerator() {
        return new IndexGenerator();
    }

    @Bean
    public StatsGenerator statsGenerator() {
        return new StatsGenerator();
    }
}
