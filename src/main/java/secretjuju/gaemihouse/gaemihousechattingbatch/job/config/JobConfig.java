package secretjuju.gaemihouse.gaemihousechattingbatch.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class JobConfig {

    // JOB : 하나의 배치 작업 단위
    // STEP : JOB을 이루는 묶음. Tasklet OR Reader & Processor & Writer으로 구성
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    private final String BATCH_NAME = "TEST_JOB";

    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get(BATCH_NAME)
                .start(step())
                .next(nextStep())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(BATCH_NAME + "step")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("첫 번째 스탭 동작 완료");
                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean
    public Step nextStep() {
        return stepBuilderFactory.get(BATCH_NAME + "nextStep")
                .tasklet((StepContribution, chunkContext) -> {
                    System.out.println("동작 완료");
                    return RepeatStatus.FINISHED;
                })
                .build();

    }
}
