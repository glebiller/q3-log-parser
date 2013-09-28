package fr.kissy.q3logparser.config;

import fr.kissy.q3logparser.app.DataPathApp;
import fr.kissy.q3logparser.service.GamesLogService;
import fr.kissy.q3logparser.service.MatchesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@Configuration("appConfig")
public class AppConfig {
    @Bean
    public DataPathApp dataPathService() {
        return new DataPathApp();
    }
}
