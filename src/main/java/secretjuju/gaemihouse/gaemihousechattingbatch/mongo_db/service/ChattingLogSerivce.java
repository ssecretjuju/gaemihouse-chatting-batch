package secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.repository.ChattingLogRepository;

@Service
public class ChattingLogSerivce {

    private final ChattingLogRepository chattingLogRepository;

    public ChattingLogSerivce(ChattingLogRepository chattingLogRepository) {
        this.chattingLogRepository = chattingLogRepository;
    }

    public String selectChattingLogs() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if(chattingLogRepository.findAll() == null) {
                return "There are no logs.";
            } else {
                return objectMapper.writeValueAsString(chattingLogRepository.findAll());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
