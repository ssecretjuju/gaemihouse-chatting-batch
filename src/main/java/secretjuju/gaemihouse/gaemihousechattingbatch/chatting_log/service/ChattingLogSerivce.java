package secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.service;

import org.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.model.ChattingLog;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.model.Keyword;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.repository.ChattingLogMongoRepository;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.repository.ChattingLogOracleRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChattingLogSerivce {

    private final ChattingLogMongoRepository chattingLogRepository;
    private final ChattingLogOracleRepository chattingLogOracleRepository;

    public ChattingLogSerivce(ChattingLogMongoRepository chattingLogRepository, ChattingLogOracleRepository chattingLogOracleRepository) {
        this.chattingLogRepository = chattingLogRepository;
        this.chattingLogOracleRepository = chattingLogOracleRepository;
    }

    public List<ChattingLog> selectChattingLogs() {

        if(chattingLogRepository.findAll() == null) {
            System.out.println("There are no logs.");
            return null;
        } else {
            return chattingLogRepository.findAll();
        }
    }

    @Transactional
    public void save(JSONArray jsonArray) {

        List<Keyword> keywords = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            String keyword = jsonArray.getString(i);
            keywords.add(new Keyword(keyword, new Date()));
        }

        chattingLogOracleRepository.saveAll(keywords);
    }
}
