package fr.kissy.q3logparser.processor.line;

import fr.kissy.q3logparser.dto.Processing;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public interface LineProcessor {
    void process(Processing processing);
}
