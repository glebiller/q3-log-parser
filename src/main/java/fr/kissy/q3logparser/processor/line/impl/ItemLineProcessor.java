package fr.kissy.q3logparser.processor.line.impl;

import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.dto.Player;
import fr.kissy.q3logparser.dto.Processing;
import fr.kissy.q3logparser.enums.EnumTeam;
import fr.kissy.q3logparser.processor.line.LineProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Guillaume <lebiller@fullsix.com>
 */
public class ItemLineProcessor implements LineProcessor {
    private static final Pattern ITEM_DATA_PATTERN = Pattern.compile("^([0-9]{1,2}) team_CTF_(red|blue)flag$");

    /**
     * @inheritDoc
     */
    @Override
    public void process(Processing processing) {
        Matcher matcher = ITEM_DATA_PATTERN.matcher(processing.getData());
        if (!matcher.matches()) {
            return;
        }

        Integer playerNumber = Integer.valueOf(matcher.group(1));
        EnumTeam flagTeam = EnumTeam.fromFlagColor(matcher.group(2));

        Player player = processing.getCurrentMatch().getPlayers().get(playerNumber);
        if (player.getTeam().getOpposite() == flagTeam) {
            if (processing.getCurrentMatch().getCarriedFlags().contains(flagTeam)) {
                handleLostFlag(processing.getCurrentMatch(), player.getTeam().getOpposite());
            }
            if (!player.getHasFlag()) {
                // Pickup
                player.pickupFlag();
                processing.getCurrentMatch().getCarriedFlags().add(flagTeam);
                processing.getCurrentMatch().getPickedUpFlags().add(flagTeam);
            } else {
                // ERROR
                throw new IllegalStateException();
            }
        } else {
            if (processing.getCurrentMatch().getPickedUpFlags().contains(flagTeam)) {
                // Return
                //System.out.println("Returned: " + player.getName() + " " + player.getTeam());
                player.returnFlag();
                processing.getCurrentMatch().getPickedUpFlags().remove(flagTeam);
            } else {
                // Capture
                player.captureFlag();
                processing.getCurrentMatch().getCarriedFlags().clear();
                processing.getCurrentMatch().getPickedUpFlags().clear();
                // Add one point
                processing.getCurrentMatch().getTeamScore().put(flagTeam, processing.getCurrentMatch().getTeamScore().get(flagTeam) + 1);
            }
        }

        processing.getCurrentMatch().getRawData().append(processing.getCurrentLine()).append('\n');
    }

    private void handleLostFlag(Match game, EnumTeam ignore) {
        for (Player player : game.getPlayers().values()) {
            if (!player.getHasFlag()) {
                continue;
            }
            // Ignore opposite team players
            if (player.getTeam() == ignore) {
                continue;
            }
            player.setHasFlag(false);
            player.getFlag().setReturned(player.getFlag().getReturned() - 1);
            player.getFlag().addCaptured();
        }
    }

}
