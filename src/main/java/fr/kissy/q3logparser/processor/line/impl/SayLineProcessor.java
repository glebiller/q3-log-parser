package fr.kissy.q3logparser.processor.line.impl;

import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.processor.line.LineProcessor;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class SayLineProcessor implements LineProcessor {
    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }
}
