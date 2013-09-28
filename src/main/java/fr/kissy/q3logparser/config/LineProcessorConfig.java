package fr.kissy.q3logparser.config;

import fr.kissy.q3logparser.processor.line.impl.ClientBeginLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.ClientConnectLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.ClientDisconnectLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.ClientUserinfoChangedLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.ExitLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.InitGameLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.ItemLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.KillLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.RedLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.SayLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.SayTeamLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.ScoreLineProcessor;
import fr.kissy.q3logparser.processor.line.impl.ShutdownGameLineProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
@Configuration("lineProcessorConfig")
public class LineProcessorConfig {
    @Bean(name = "lineProcessorClientBegin")
    public ClientBeginLineProcessor clientBeginLineProcessor() {
        return new ClientBeginLineProcessor();
    }

    @Bean(name = "lineProcessorClientConnect")
    public ClientConnectLineProcessor clientConnectLineProcessor() {
        return new ClientConnectLineProcessor();
    }

    @Bean(name = "lineProcessorClientDisconnect")
    public ClientDisconnectLineProcessor clientDisconnectLineProcessor() {
        return new ClientDisconnectLineProcessor();
    }

    @Bean(name = "lineProcessorClientUserinfoChanged")
    public ClientUserinfoChangedLineProcessor clientUserinfoChangedLineProcessor() {
        return new ClientUserinfoChangedLineProcessor();
    }

    @Bean(name = "lineProcessorExit")
    public ExitLineProcessor exitLineProcessor() {
        return new ExitLineProcessor();
    }

    @Bean(name = "lineProcessorInitGame")
    public InitGameLineProcessor initGameLineProcessor() {
        return new InitGameLineProcessor();
    }

    @Bean(name = "lineProcessorItem")
    public ItemLineProcessor itemLineProcessor() {
        return new ItemLineProcessor();
    }

    @Bean(name = "lineProcessorKill")
    public KillLineProcessor killLineProcessor() {
        return new KillLineProcessor();
    }

    @Bean(name = "lineProcessorred")
    public RedLineProcessor redLineProcessor() {
        return new RedLineProcessor();
    }

    @Bean(name = "lineProcessorsay")
    public SayLineProcessor sayLineProcessor() {
        return new SayLineProcessor();
    }

    @Bean(name = "lineProcessorsayteam")
    public SayTeamLineProcessor sayTeamLineProcessor() {
        return new SayTeamLineProcessor();
    }

    @Bean(name = "lineProcessorscore")
    public ScoreLineProcessor scoreLineProcessor() {
        return new ScoreLineProcessor();
    }

    @Bean(name = "lineProcessorShutdownGame")
    public ShutdownGameLineProcessor shutdownGameLineProcessor() {
        return new ShutdownGameLineProcessor();
    }
}
