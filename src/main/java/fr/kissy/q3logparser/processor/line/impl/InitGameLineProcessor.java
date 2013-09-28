package fr.kissy.q3logparser.processor.line.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.enums.EnumGameType;
import fr.kissy.q3logparser.processor.line.LineProcessor;

import java.util.Map;
import java.util.Queue;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class InitGameLineProcessor implements LineProcessor {
    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        Queue<String> rawParams = Queues.newArrayDeque(Lists.newArrayList(processing.getData().split("\\\\")));
        Map<String, String> params = Maps.newHashMap();

        // Remove the first empty occurrence
        rawParams.remove();
        while (!rawParams.isEmpty()) {
            params.put(rawParams.remove(), rawParams.remove());
        }

        EnumGameType gameType = EnumGameType.values()[Integer.valueOf(params.get("g_gametype"))];
        processing.setCurrentMatch(new Match(gameType, params.get("mapname")));
        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }
}
