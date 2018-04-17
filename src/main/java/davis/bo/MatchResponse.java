package davis.bo;

import davis.entity.Grammar;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatchResponse {
    private String sentence;
    private List<Grammar> matchList = new ArrayList<>();
}
