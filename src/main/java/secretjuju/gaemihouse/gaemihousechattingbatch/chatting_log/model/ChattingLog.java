package secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logs")
public class ChattingLog {

    @Id
    private String id;
    private String nickname;
    private String message;
    private String time;

    public ChattingLog() {
    }

    public ChattingLog(String id, String nickname, String message, String time) {
        this.id = id;
        this.nickname = nickname;
        this.message = message;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChattingLog{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
