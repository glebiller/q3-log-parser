package fr.kissy.q3logparser.generator;

import fr.kissy.q3logparser.dto.Match;

import java.util.Map;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public interface MatchesGenerator {
    void generate(Map<String, Match> matchesData);
}
