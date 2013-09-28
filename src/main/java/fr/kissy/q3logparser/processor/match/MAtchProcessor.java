package fr.kissy.q3logparser.processor.match;

import fr.kissy.q3logparser.dto.Match;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public interface MatchProcessor {
    void process(Match match);
}
