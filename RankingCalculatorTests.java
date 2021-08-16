package test.test.ds.ranking;

import ds.ranking.Game;
import ds.ranking.Match;
import ds.ranking.Player;
import ds.ranking.RankingCalculator;
import ds.ranking.RankingType;
import ds.ranking.Record;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RankingCalculatorTests {
    private RankingCalculator calculator = new RankingCalculator();
    
    final Player PLAYER_A = new Player("Player A");
    final Player PLAYER_B = new Player("Player B");
    final Player PLAYER_C = new Player("Player C");

    @Test
    public void calculateRecords_noMatches_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                calculator.calculateRecords(
                        List.of())
        );
    }

    @Test
    public void calculateRecords_oneMatchOneGame_success() {
        Match match = new Match()
                .setPlayer1(PLAYER_A)
                .setPlayer2(PLAYER_B)
                .setGames(List.of(
                        new Game()
                            .setScore1(4)
                            .setScore2(2)
                ));

        Record expectedRecordA = new Record()
                .setPlayer(PLAYER_A)
                .setMatchWins(1L)
                .setMatchLosses(0L)
                .setGameWins(1L)
                .setGameLosses(0L)
                .setTotalPoints(4L);
        Record expectedRecordB = new Record()
                .setPlayer(PLAYER_B)
                .setMatchWins(0L)
                .setMatchLosses(1L)
                .setGameWins(0L)
                .setGameLosses(1L)
                .setTotalPoints(2L);
        assertListEqualsIgnoringOrder(
                List.of(expectedRecordA, expectedRecordB),
                calculator.calculateRecords(List.of(match)));
    }

    @Test
    public void calculateRecords_twoMatchesDifferentCompetitors_success() {
        Match match1 = new Match()
                .setPlayer1(PLAYER_A)
                .setPlayer2(PLAYER_B)
                .setGames(List.of(
                        new Game()
                                .setScore1(4)
                                .setScore2(2)
                ));
        Match match2 = new Match()
                .setPlayer1(PLAYER_A)
                .setPlayer2(PLAYER_C)
                .setGames(List.of(
                        new Game()
                                .setScore1(1)
                                .setScore2(5)
                ));

        Record expectedRecordA = new Record()
                .setPlayer(PLAYER_A)
                .setMatchWins(1L)
                .setMatchLosses(1L)
                .setGameWins(1L)
                .setGameLosses(1L)
                .setTotalPoints(5L);
        Record expectedRecordB = new Record()
                .setPlayer(PLAYER_B)
                .setMatchWins(0L)
                .setMatchLosses(1L)
                .setGameWins(0L)
                .setGameLosses(1L)
                .setTotalPoints(2L);
        Record expectedRecordC = new Record()
                .setPlayer(PLAYER_C)
                .setMatchWins(1L)
                .setMatchLosses(0L)
                .setGameWins(1L)
                .setGameLosses(0L)
                .setTotalPoints(5L);
        assertListEqualsIgnoringOrder(
                List.of(expectedRecordA, expectedRecordB, expectedRecordC),
                calculator.calculateRecords(List.of(match1, match2)));
    }

    @Test
    public void calculateRecords_multipleGamesDifferentCompetitors_success() {
        Match match1 = new Match()
                .setPlayer1(PLAYER_A)
                .setPlayer2(PLAYER_B)
                .setGames(List.of(
                        new Game()
                                .setScore1(4)
                                .setScore2(2),
                        new Game()
                                .setScore1(3)
                                .setScore2(0)

                ));
        Match match2 = new Match()
                .setPlayer1(PLAYER_A)
                .setPlayer2(PLAYER_C)
                .setGames(List.of(
                        new Game()
                                .setScore1(1)
                                .setScore2(5),
                        new Game()
                                .setScore1(8)
                                .setScore2(0),
                        new Game()
                                .setScore1(3)
                                .setScore2(2)
                ));

        Record expectedRecordA = new Record()
                .setPlayer(PLAYER_A)
                .setMatchWins(2L)
                .setMatchLosses(0L)
                .setGameWins(4L)
                .setGameLosses(1L)
                .setTotalPoints(19L);
        Record expectedRecordB = new Record()
                .setPlayer(PLAYER_B)
                .setMatchWins(0L)
                .setMatchLosses(1L)
                .setGameWins(0L)
                .setGameLosses(2L)
                .setTotalPoints(2L);
        Record expectedRecordC = new Record()
                .setPlayer(PLAYER_C)
                .setMatchWins(0L)
                .setMatchLosses(1L)
                .setGameWins(1L)
                .setGameLosses(2L)
                .setTotalPoints(7L);
        assertListEqualsIgnoringOrder(
                List.of(expectedRecordA, expectedRecordB, expectedRecordC),
                calculator.calculateRecords(List.of(match1, match2)));
    }

    @Test
    public void calculateRankings_noRankingType_exception() {
        assertThrows(IllegalArgumentException.class, () ->
            calculator.calculateRankings(
                    List.of(new Record().setPlayer(PLAYER_A)),
                    List.of())
        );
    }

    @Test
    public void calculateRankings_noMatches_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                calculator.calculateRankings(
                        List.of(),
                        List.of(RankingType.MOST_MATCH_WINS))
        );
    }

    @Test
    public void calculateRankings_mostMatchWins_success() {
        Record RECORD_A = new Record().setPlayer(PLAYER_A).setMatchWins(1L);
        Record RECORD_B = new Record().setPlayer(PLAYER_B).setMatchWins(2L);
        Record RECORD_C = new Record().setPlayer(PLAYER_C).setMatchWins(3L);

        List<Record> results = calculator.calculateRankings(
                List.of(RECORD_A, RECORD_B, RECORD_C),
                List.of(RankingType.MOST_MATCH_WINS));
        assertIterableEquals(
                List.of(RECORD_C, RECORD_B, RECORD_A),
                results);
    }

    @Test
    public void calculateRankings_leastMatchLossess_success() {
        Record RECORD_A = new Record().setPlayer(PLAYER_A).setMatchLosses(1L);
        Record RECORD_B = new Record().setPlayer(PLAYER_B).setMatchLosses(2L);
        Record RECORD_C = new Record().setPlayer(PLAYER_C).setMatchLosses(3L);

        List<Record> results = calculator.calculateRankings(
                List.of(RECORD_A, RECORD_B, RECORD_C),
                List.of(RankingType.FEWEST_MATCH_LOSSES));
        assertIterableEquals(
                List.of(RECORD_A, RECORD_B, RECORD_C),
                results);
    }

    @Test
    public void calculateRankings_matchWinLossDifferential_success() {
        Record RECORD_A = new Record().setPlayer(PLAYER_A).setMatchWins(1L).setMatchLosses(4L);
        Record RECORD_B = new Record().setPlayer(PLAYER_B).setMatchWins(4L).setMatchLosses(0L);
        Record RECORD_C = new Record().setPlayer(PLAYER_C).setMatchWins(10L).setMatchLosses(5L);

        List<Record> results = calculator.calculateRankings(
                List.of(RECORD_A, RECORD_B, RECORD_C),
                List.of(RankingType.MATCH_WIN_LOSS_DIFFERENTIAL));
        assertIterableEquals(
                List.of(RECORD_C, RECORD_B, RECORD_A),
                results);
    }

    @Test
    public void calculateRankings_gameWinLossDifferential_success() {
        Record RECORD_A = new Record().setPlayer(PLAYER_A).setGameWins(1L).setGameLosses(4L);
        Record RECORD_B = new Record().setPlayer(PLAYER_B).setGameWins(4L).setGameLosses(0L);
        Record RECORD_C = new Record().setPlayer(PLAYER_C).setGameWins(10L).setGameLosses(5L);

        List<Record> results = calculator.calculateRankings(
                List.of(RECORD_A, RECORD_B, RECORD_C),
                List.of(RankingType.GAME_WIN_LOSS_DIFFERENTIAL));
        assertIterableEquals(
                List.of(RECORD_C, RECORD_B, RECORD_A),
                results);
    }

    @Test
    public void calculateRankings_totalPoints_success() {
        Record RECORD_A = new Record().setPlayer(PLAYER_A).setTotalPoints(10);
        Record RECORD_B = new Record().setPlayer(PLAYER_B).setTotalPoints(12);
        Record RECORD_C = new Record().setPlayer(PLAYER_C).setTotalPoints(17);

        List<Record> results = calculator.calculateRankings(
                List.of(RECORD_A, RECORD_B, RECORD_C),
                List.of(RankingType.TOTAL_POINTS));
        assertIterableEquals(
                List.of(RECORD_C, RECORD_B, RECORD_A),
                results);
    }

    @Test
    public void calculateRankings_multipleRankers_tiesOnFirstSecondUsed() {
        Record RECORD_A = new Record().setPlayer(PLAYER_A).setMatchWins(2L).setMatchLosses(4L);
        Record RECORD_B = new Record().setPlayer(PLAYER_B).setMatchWins(2L).setMatchLosses(1L);
        Record RECORD_C = new Record().setPlayer(PLAYER_C).setMatchWins(2L).setMatchLosses(2L);

        List<Record> results = calculator.calculateRankings(
                List.of(RECORD_A, RECORD_B, RECORD_C),
                List.of(RankingType.MOST_MATCH_WINS, RankingType.FEWEST_MATCH_LOSSES));
        assertIterableEquals(
                List.of(RECORD_B, RECORD_C, RECORD_A),
                results);
    }

    /**
     * Helper method to check that two lists contain the same elements in any order.
     * Note: this does not account for duplicate entries, assuming each is unique.
     * To account for this, a proper assertion helper or collections library should probably be used.
     */
    private static <T> void assertListEqualsIgnoringOrder(List<T> expected, List<T> actual) {
        HashSet<T> expectedSet = new HashSet<>(expected);
        HashSet<T> actualSet = new HashSet<>(actual);
        assertEquals(expectedSet, actualSet);
    }
}
