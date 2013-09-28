package fr.kissy.q3logparser.processor.line.impl;

import com.google.common.base.Objects;
import fr.kissy.q3logparser.dto.Kill;
import fr.kissy.q3logparser.dto.Player;
import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.enums.EnumMeanOfDeath;
import fr.kissy.q3logparser.enums.EnumTeam;
import fr.kissy.q3logparser.processor.line.LineProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class KillLineProcessor implements LineProcessor {
    private static final Integer WORLD_NUMBER = 1022;
    private static final Pattern KILL_DATA_PATTERN = Pattern.compile("^([0-9]{1,4}) ([0-9]{1,2}) ([0-9]{1,2}): (.*) killed (.*) by [^ ]*$");

    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        Matcher matcher = KILL_DATA_PATTERN.matcher(processing.getData());
        if (!matcher.matches()) {
            return;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        Integer targetNumber = Integer.valueOf(matcher.group(2));
        EnumMeanOfDeath meanOfDeath = EnumMeanOfDeath.values()[Integer.valueOf(matcher.group(3))];

        if (Objects.equal(WORLD_NUMBER, playerNumber)) {
            playerNumber = targetNumber;
        }

        Player player = processing.getCurrentMatch().getPlayers().get(playerNumber);
        Player target = processing.getCurrentMatch().getPlayers().get(targetNumber);

        boolean targetHadFlag = target.getHasFlag();

        // Kill
        player.addKill(new Kill(player, target, processing.getCurrentTime(), meanOfDeath));
        target.addKill(new Kill(player, target, processing.getCurrentTime(), meanOfDeath));

        // Remove pickedUpFlags if needed
        if (targetHadFlag) {
            EnumTeam oppositeTeam = target.getTeam().getOpposite();
            processing.getCurrentMatch().getCarriedFlags().remove(oppositeTeam);
            if (meanOfDeath == EnumMeanOfDeath.MOD_FALLING) {
                processing.getCurrentMatch().getPickedUpFlags().remove(oppositeTeam);
            }
        }

        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }
}
