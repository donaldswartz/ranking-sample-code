package ds.ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Represents the main calculator that calculates an ordered ranking of Players, based on
 * their performance in a set of matches.
 * This is typically done by composing two operations: calculateRecords and calculateRankings,
 * although calculateRecords can be done without calculateRankings if desired.
 */
public class RankingCalculator {
    /**
     * Calculates the records of a set of players based on their performance in a set of matches.
     * @param matches the matches to consider for rankings. Must be non-empty.
     * @return A list of records, one for each competitor found in the matches.
     */
    public List<Record> calculateRecords(List<Match> matches) {
        if (matches == null || matches.size() == 0) {
            throw new IllegalArgumentException("At least one match must be provided.");
        }

        HashMap<Player, Record> recordsList = new HashMap<>();
        for (Match match : matches) {
            Player player1 = match.getPlayer1();
            Player player2 = match.getPlayer2();

            Record record1 = getOrAddRecord(recordsList, player1);
            Record record2 = getOrAddRecord(recordsList, player2);

            // Add points
            record1.setTotalPoints(record1.getTotalPoints() + match.getPlayer1TotalScore());
            record2.setTotalPoints(record2.getTotalPoints() + match.getPlayer2TotalScore());

            // Add won and lost games
            record1.setGameWins(record1.getGameWins() + match.getPlayer1GameScore());
            record2.setGameLosses(record2.getGameLosses() + match.getPlayer1GameScore());
            record2.setGameWins(record2.getGameWins() + match.getPlayer2GameScore());
            record1.setGameLosses(record1.getGameLosses() + match.getPlayer2GameScore());

            // Add won and lost matches
            Player winner = match.getWinner();
            if (winner != null) {
                if (winner.equals(player1)) {
                    record1.setMatchWins(record1.getMatchWins() + 1);
                    record2.setMatchLosses(record2.getMatchLosses() + 1);
                } else {
                    record1.setMatchLosses(record1.getMatchLosses() + 1);
                    record2.setMatchWins(record2.getMatchWins() + 1);
                }
            }
        }

        return new ArrayList<>(recordsList.values());
    }

    /**
     * Helper method to get an existing player from the map, if they exist, or add a new one.
     */
    private static Record getOrAddRecord(HashMap<Player, Record> recordHashMap, Player player) {
        if (recordHashMap.containsKey(player)) {
            return recordHashMap.get(player);
        } else {
            Record record = new Record().setPlayer(player);
            recordHashMap.put(player, record);
            return record;
        }
    }

    /**
     * Calculates the ranking list by applying the ranking types provided, in order.
     * If any players are tied according to all provided ranking types, they may be returned in an arbitrary order.
     *
     * @param records the set of players' records to consider when ranking. Must be non-empty.
     * @param rankingTypes the ranking types to consider. Must be non-empty.
     * @return a new list of records, based on the applied rankings. Each record corresponds to one player's results.
     */
    public List<Record> calculateRankings(List<Record> records, List<RankingType> rankingTypes) {
        if (records == null || records.size() == 0) {
            throw new IllegalArgumentException("At least one record much be provided.");
        }

        if (rankingTypes == null || rankingTypes.size() == 0) {
            throw new IllegalArgumentException("At least one ranking type must be provided.");
        }

        List<List<Record>> currentSortGroups = List.of(records);
        for (RankingType rankingType : rankingTypes) {
            currentSortGroups = sortEachBucketAndFlatten(currentSortGroups, rankingType);
        }

        // Flatten tied groups one more time back into a single list.
        return currentSortGroups.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Sorts within each bucket of tied players, and then flattens the two-level hierarchy of tied buckets
     * into a single level list of tied buckets.
     * @param tiedGroups The groups of players which are still tied after applying previous tiebreakers
     * @param rankingType The next tiebreaker to consider
     * @return A list of buckets, each containing a list of players who are tied at the current comparison level
     */
    private List<List<Record>> sortEachBucketAndFlatten(List<List<Record>> tiedGroups, RankingType rankingType) {
        return tiedGroups.stream()
                .map(tiedGroup -> sortRecordsBy(tiedGroup, rankingType))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Sorts the provided records by a single criterion.
     * The outer list returned represents the ordered groups, while the inner lists contain records which are still tied.
     *
     * @param unsortedRecords the set of records to sort
     * @param rankingType the ranking type to use for this single sort
     * @return A list of groups, which are tied according to this ranking type.
     */
    private List<List<Record>> sortRecordsBy(List<Record> unsortedRecords, RankingType rankingType) {
        Comparator<Long> comparator = rankingType.isDescending()
                ? Comparator.reverseOrder()
                : Comparator.naturalOrder();
        SortedMap<Long, List<Record>> scoreGroups = new TreeMap<>(comparator);
        for (Record record : unsortedRecords) {
            Long score = rankingType.getScoreFunction().apply(record);
            if (scoreGroups.containsKey(score)) {
                scoreGroups.get(score).add(record);
            } else {
                scoreGroups.put(score, new LinkedList<>());
                scoreGroups.get(score).add(record);
            }
        }

        return new ArrayList<>(scoreGroups.values());
    }
}
