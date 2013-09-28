package fr.kissy.q3logparser.processor.line.impl;

import fr.kissy.q3logparser.dto.Player;
import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.enums.EnumTeam;
import fr.kissy.q3logparser.processor.line.LineProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class ClientUserinfoChangedLineProcessor implements LineProcessor {
    private static final Pattern CLIENT_USER_INFO_CHANGED_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) n\\\\([^\\\\]+)\\\\t\\\\(\\d)(.*)$");

    @Resource
    private Map<String, String> aliases;

    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        Matcher matcher = CLIENT_USER_INFO_CHANGED_DATA_PATTERN.matcher(processing.getData());
        if (!matcher.matches()) {
            return;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        String name = matcher.group(2);
        Integer teamNumber = Integer.valueOf(matcher.group(3));
        // Get alias if needed
        if (aliases.containsKey(name)) {
            name = aliases.get(name);
        }

        Player player = processing.getCurrentMatch().getPlayers().get(playerNumber);
        EnumTeam team = EnumTeam.values()[teamNumber];
        if (StringUtils.equals(player.getName(), name) && player.getTeam() == team) {
            return;
        }

        player.update(playerNumber, name, team);
        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }
}
