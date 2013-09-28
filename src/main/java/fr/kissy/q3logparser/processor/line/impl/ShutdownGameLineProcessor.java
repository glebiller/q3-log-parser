package fr.kissy.q3logparser.processor.line.impl;

import com.google.common.hash.Hashing;
import fr.kissy.q3logparser.dto.Player;
import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.funnel.MatchFunnel;
import fr.kissy.q3logparser.processor.line.LineProcessor;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Properties;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class ShutdownGameLineProcessor implements LineProcessor {
    private static final String DATE_FORMAT = "dd/MM/yy";

    @Autowired
    private Properties matchProperties;

    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        processing.getCurrentMatch().setDuration(processing.getCurrentTime());
        for (Player player : processing.getCurrentMatch().getPlayers().values()) {
            player.processShutdownGame(processing.getCurrentTime());
        }

        String gameHash = Hashing.md5().hashObject(processing.getCurrentMatch(), MatchFunnel.INSTANCE).toString();
        processing.getCurrentMatch().setHash(gameHash);
        if (matchProperties.containsKey(gameHash)) {
            processing.getCurrentMatch().setDate((String) matchProperties.get(gameHash));
        } else {
            processing.getCurrentMatch().setDate(FastDateFormat.getInstance(DATE_FORMAT).format(new Date()));
        }

        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
        processing.finishCurrentMatch();
    }
}
