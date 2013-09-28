package fr.kissy.q3logparser.function;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.google.common.collect.Maps;
import fr.kissy.q3logparser.dto.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class MatchesPropertyTransformer implements Maps.EntryTransformer<String, String, Match> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchesPropertyTransformer.class);
    private static final Object FILE_NAME = "match.bin";

    @Autowired
    private Kryo kryo;
    @Value("${data-directory}")
    private String dataDirectory;
    @Value("${matches-directory}")
    private String matchesDirectory;

    @Override
    public Match transformEntry(String key, String value) {
        try {
            File file = new File(dataDirectory + File.separator + matchesDirectory + File.separator + key + File.separator + FILE_NAME);
            Match match = kryo.readObject(new Input(new FileInputStream(file)), Match.class);
            match.setDate(value);
            return match;
        } catch (FileNotFoundException e) {
            LOGGER.error("Error while reading the binary file for match {}", key);
            return null;
        }
    }
}
