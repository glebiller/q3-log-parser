package fr.kissy.q3logparser.config;

import com.google.common.collect.Maps;
import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.function.MatchesPropertyTransformer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@Configuration("propertiesConfig")
public class PropertiesConfig implements InitializingBean, DisposableBean {
    private static final Object MATCH_PROPERTIES_FILENAME = "matches.properties";

    @Value("${data-directory}")
    private String dataDirectory;
    private Properties matchProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        matchProperties = new Properties();
        File matchPropertiesFile = matchPropertiesFile();
        if (matchPropertiesFile.exists()) {
            matchProperties.load(new FileInputStream(matchPropertiesFile));
        }
    }

    @Override
    public void destroy() throws Exception {
        if (matchProperties != null) {
            matchProperties.store(new FileOutputStream(matchPropertiesFile()), "Auto generated, do not modify");
        }
    }

    @Bean
    public MatchesPropertyTransformer matchesPropertyTransformer() {
        return new MatchesPropertyTransformer();
    }

    @Bean(autowire = Autowire.BY_NAME)
    public Properties matchProperties() {
        return matchProperties;
    }

    private File matchPropertiesFile() {
        return new File(dataDirectory + File.separator + MATCH_PROPERTIES_FILENAME);
    }
}
