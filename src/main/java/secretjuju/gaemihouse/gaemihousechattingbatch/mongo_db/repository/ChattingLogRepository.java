package secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.model.ChattingLog;

import java.util.List;

public interface ChattingLogRepository extends MongoRepository<ChattingLog, String> {
    List<ChattingLog> findAll();
}
