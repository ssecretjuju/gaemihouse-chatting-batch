package secretjuju.gaemihouse.gaemihousechattingbatch.job.config;

import org.apache.commons.codec.Charsets;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.model.ChattingLog;
import secretjuju.gaemihouse.gaemihousechattingbatch.chatting_log.service.ChattingLogSerivce;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
public class JobConfig {

    // JOB : 하나의 배치 작업 단위
    // STEP : JOB을 이루는 묶음. Tasklet OR Reader & Processor & Writer으로 구성
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final ChattingLogSerivce chattingService;

    public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory, ChattingLogSerivce chattingService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.chattingService = chattingService;
    }

    private final String BATCH_NAME = "CHATTING_LOG_SERVING_JOB";
    private List<ChattingLog> chattingLogs;
    private JSONArray keywords;

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get(BATCH_NAME)
                .start(firstStep())
                .next(secondStep())
                .next(thirdStep())
                .next(fourthStep())
                .build();
    }

    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get(BATCH_NAME + "firstStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("1. MongoDB에서 채팅 로그 조회");
                    chattingLogs = chattingService.selectChattingLogs();
                    System.out.println(chattingLogs);
                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step secondStep() {

        return stepBuilderFactory.get(BATCH_NAME + "secondStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("2. AI 모델에 POST 요청");
                    System.out.println(chattingLogs);

                    String url = "http://localhost:8080/test";
                    JSONArray jsonArray = new JSONArray(chattingLogs);

                    try {
                        HttpClient client = HttpClientBuilder.create().build();
                        HttpPost postRequest = new HttpPost(url);

                        postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
                        postRequest.setEntity(new StringEntity(jsonArray.toString(), Charsets.UTF_8));

                        HttpResponse response = client.execute(postRequest);

                        if (response.getStatusLine().getStatusCode() == 200) {
                            ResponseHandler<String> handler = new BasicResponseHandler();
                            String body = handler.handleResponse(response);
//                            System.out.println(body);

                            keywords = new JSONArray(body);
                        } else {
                            System.out.println("응답 코드 : " + response.getStatusLine().getStatusCode());
                        }

                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step thirdStep() {
        return stepBuilderFactory.get(BATCH_NAME + "thirdStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("3. 응답 데이터 Oracle DB에 저장");
                    chattingService.save(keywords);
                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step fourthStep() {
        return stepBuilderFactory.get(BATCH_NAME + "thirdStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("4. Oracle DB 저장 성공 시 24시간 동안 발생한 채팅 로그 삭제");

                    return RepeatStatus.FINISHED;
                })
                .build();

    }
}
