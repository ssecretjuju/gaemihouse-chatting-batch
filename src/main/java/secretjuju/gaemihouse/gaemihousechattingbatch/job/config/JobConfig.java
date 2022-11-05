package secretjuju.gaemihouse.gaemihousechattingbatch.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import secretjuju.gaemihouse.gaemihousechattingbatch.mongo_db.service.ChattingLogSerivce;

import javax.persistence.EntityManagerFactory;

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

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get(BATCH_NAME)
                .start(firstStep())
                .next(secondStep())
                .next(thirdStep())
                .build();
    }

    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get(BATCH_NAME + "firstStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("1. MongoDB에서 채팅 로그 조회");
                    System.out.println(chattingService.selectChattingLogs());

                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step secondStep() {
        return stepBuilderFactory.get(BATCH_NAME + "secondStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("2. AI 모델에 POST 요청");

                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step thirdStep() {
        return stepBuilderFactory.get(BATCH_NAME + "thirdStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("3. 응답 데아터 Oracle DB에 저장");

                    return RepeatStatus.FINISHED;
                })
                .build();

    }
}
