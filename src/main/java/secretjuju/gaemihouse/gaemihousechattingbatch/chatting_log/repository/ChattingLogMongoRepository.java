package secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.model.ChattingLog;

import java.util.List;

public interface ChattingLogMongoRepository extends MongoRepository<ChattingLog, String> {
    List<ChattingLog> findAll();
}
