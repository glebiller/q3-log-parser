package fr.kissy.q3logparser.config;

import fr.kissy.q3logparser.service.GamesLogService;
import fr.kissy.q3logparser.service.MatchesService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@Configuration("serviceConfig")
public class ServiceConfig {
    @Bean
    public GamesLogService gamesLogService() {
        return new GamesLogService();
    }

    @Bean
    public MatchesService matchesService() {
        return new MatchesService();
    }
}
