package ds.ranking;

import lombok.Getter;

import java.util.function.Function;

public enum RankingType {
    FEWEST_MATCH_LOSSES(r -> r.getMatchLosses(), false),
    GAME_WIN_LOSS_DIFFERENTIAL(r -> r.getGameWins() - r.getGameLosses(), true),
    MATCH_WIN_LOSS_DIFFERENTIAL(r -> r.getMatchWins() - r.getMatchLosses(), true),
    MOST_MATCH_WINS(Record::getMatchWins, true),
    TOTAL_POINTS(Record::getTotalPoints, true);

    @Getter
    private final Function<Record, Long> scoreFunction;

    @Getter
    private final boolean descending;

    RankingType(Function<Record, Long> scoreFunction, boolean descending) {
        this.scoreFunction = scoreFunction;
        this.descending = descending;
    }
}
