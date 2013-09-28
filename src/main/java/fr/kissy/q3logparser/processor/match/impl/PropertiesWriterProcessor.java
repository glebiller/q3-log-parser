package fr.kissy.q3logparser.processor.match.impl;

import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.processor.match.MatchProcessor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class PropertiesWriterProcessor implements MatchProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesWriterProcessor.class);

    @Autowired
    private Properties matchProperties;

    /**
     * @inheritDoc
     */
    @Override
    public void process(Match match) {
        if (matchProperties.containsKey(match.getHash())) {
            return;
        }

        matchProperties.put(match.getHash(), match.getDate());
        LOGGER.info("Property added for match {}", match.getHash());
    }
}
