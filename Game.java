package ds.ranking;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Game {
    private long score1;
    private long score2;
}
