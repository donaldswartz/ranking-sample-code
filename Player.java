package ds.ranking;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
public class Player {
    @ToString.Include
    public final String name;
}
