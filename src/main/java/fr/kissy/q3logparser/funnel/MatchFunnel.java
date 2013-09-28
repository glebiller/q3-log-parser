package fr.kissy.q3logparser.funnel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import fr.kissy.q3logparser.dto.Match;
import fr.kissy.q3logparser.dto.Player;

/**

 */
public class MatchFunnel implements Funnel<Match> {
    public static final Funnel<Match> INSTANCE = new MatchFunnel();

    @Override
    public void funnel(Match match, PrimitiveSink into) {
        into.putString(match.getType().name())
            .putString(match.getMap())
            .putLong(match.getDuration());

        ImmutableList<Player> sortedPlayers = Ordering.natural().immutableSortedCopy(match.getPlayers().values());
        for (Player player : sortedPlayers) {
            into.putInt(player.getId())
                .putString(player.getTeam().name())
                .putString(player.getName())
                .putInt(player.getScore())
                .putInt(player.getStreak().getFrag())
                .putInt(player.getStreak().getDeath())
                .putInt(player.getFlag().getCaptured())
                .putInt(player.getFlag().getPickedUp())
                .putInt(player.getFlag().getReturned())
                .putLong(player.getStartPlaying() == null ? -1L : player.getStartPlaying())
                .putInt(player.getFrags().size())
                .putInt(player.getDeaths().size())
                .putInt(player.getSuicides().size());
        }
    }
}
