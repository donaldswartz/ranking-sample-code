package ds.ranking;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Match {
    private Player player1;
    private Player player2;
    private List<Game> games;

    public Player getWinner() {
        long player1Wins = getPlayer1GameScore();
        long player2Wins = getPlayer2GameScore();
        if (player1Wins > player2Wins) {
            return player1;
        } else if (player2Wins > player1Wins) {
            return player2;
        }

        return null; // The match is tied.
    }

    public long getPlayer1GameScore() {
        return games.stream().filter(g -> g.getScore1() > g.getScore2()).count();
    }

    public long getPlayer2GameScore() {
        return games.stream().filter(g -> g.getScore2() > g.getScore1()).count();
    }

    public long getPlayer1TotalScore() {
        return games.stream().map(Game::getScore1).reduce(0L, Long::sum);
    }

    public long getPlayer2TotalScore() {
        return games.stream().map(Game::getScore2).reduce(0L, Long::sum);
    }
}
