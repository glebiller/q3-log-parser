package fr.kissy.q3logparser.config;

import com.esotericsoftware.kryo.Kryo;
import com.google.common.collect.Maps;
import fr.kissy.q3logparser.Main;
import fr.kissy.q3logparser.app.DataPathApp;
import fr.kissy.q3logparser.dto.Match;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@Configuration("applicationConfig")
@Import({PropertiesConfig.class, LineProcessorConfig.class, MatchProcessorConfig.class, MatchesGeneratorConfig.class, AppConfig.class, ServiceConfig.class})
public class ApplicationConfig {
    @Value("${alias-file}")
    private String aliasFile;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Kryo kryo() {
        return new Kryo();
    }

    @Bean
    public freemarker.template.Configuration configuration() {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        configuration.setClassForTemplateLoading(Main.class, "/");
        return configuration;
    }

    @Bean(autowire = Autowire.BY_NAME)
    public Map<String, String> aliases() {
        File aliasPropertyFile = new File(aliasFile);
        Properties aliasProperties = new Properties();
        try {
            aliasProperties.load(new FileInputStream(aliasPropertyFile));
        } catch (IOException ignored) {}
        return Maps.fromProperties(aliasProperties);
    }
}
