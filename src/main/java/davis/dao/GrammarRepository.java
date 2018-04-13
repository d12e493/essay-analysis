package davis.dao;

import davis.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarRepository extends JpaRepository<Grammar, String> {

}
