package fr.kissy.q3logparser.config;

import fr.kissy.q3logparser.processor.match.impl.BinaryWriterProcessor;
import fr.kissy.q3logparser.processor.match.impl.HtmlWriterProcessor;
import fr.kissy.q3logparser.processor.match.impl.LogWriterProcessor;
import fr.kissy.q3logparser.processor.match.impl.PropertiesWriterProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@Configuration("matchProcessorConfig")
public class MatchProcessorConfig {
    @Bean
    public BinaryWriterProcessor binaryWriterProcessor() {
        return new BinaryWriterProcessor();
    }
    @Bean
    public HtmlWriterProcessor htmlWriterProcessor() {
        return new HtmlWriterProcessor();
    }
    @Bean
    public LogWriterProcessor gameLogWriterProcessor() {
        return new LogWriterProcessor();
    }
    @Bean
    public PropertiesWriterProcessor propertiesWriterProcessor() {
        return new PropertiesWriterProcessor();
    }
}
