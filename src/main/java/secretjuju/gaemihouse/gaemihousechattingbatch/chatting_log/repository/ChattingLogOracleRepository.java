package secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.model.Keyword;

public interface ChattingLogOracleRepository extends JpaRepository<Keyword, Integer> {
}
