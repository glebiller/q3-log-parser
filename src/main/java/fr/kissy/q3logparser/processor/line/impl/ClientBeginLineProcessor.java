package fr.kissy.q3logparser.processor.line.impl;

import fr.kissy.q3logparser.dto.Player;
import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.processor.line.LineProcessor;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class ClientBeginLineProcessor implements LineProcessor {
    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        Integer playerNumber = Integer.valueOf(processing.getData());
        Player player = processing.getCurrentMatch().getPlayers().get(playerNumber);
        player.setStartPlaying(processing.getCurrentTime());
        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }
}
