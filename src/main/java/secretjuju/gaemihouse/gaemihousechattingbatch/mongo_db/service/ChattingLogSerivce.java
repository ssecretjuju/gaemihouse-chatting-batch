package secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.model.ChattingLog;
import secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.repository.ChattingLogRepository;

import java.util.List;

@Service
public class ChattingLogSerivce {

    private final ChattingLogRepository chattingLogRepository;

    public ChattingLogSerivce(ChattingLogRepository chattingLogRepository) {
        this.chattingLogRepository = chattingLogRepository;
    }

    public List<ChattingLog> selectChattingLogs() {

        if(chattingLogRepository.findAll() == null) {
            System.out.println("There are no logs.");
            return null;
        } else {
            return chattingLogRepository.findAll();
        }
    }
}
