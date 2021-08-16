package ds.ranking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
public class Record {
    private Player player;
    private long matchWins;
    private long matchLosses;
    private long gameWins;
    private long gameLosses;
    private long totalPoints;
}
