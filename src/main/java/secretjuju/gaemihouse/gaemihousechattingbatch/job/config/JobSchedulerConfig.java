package secretjuju.gaemihouse.gaemihousechattingbatch.job.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JobSchedulerConfig {
    private final JobLauncher jobLauncher;
    private final JobConfig jobConfig;

    public JobSchedulerConfig(JobLauncher jobLauncher, JobConfig jobConfig) {
        this.jobLauncher = jobLauncher;
        this.jobConfig = jobConfig;
    }

    // 10초에 한 번씩 실행
    @Scheduled(cron = "0/10 * * * * *")
    public void testJobSchedule() {
        // 파라미터를 매번 다르게 설정하여 다른 Job Instance로 지정
        Map<String, JobParameter> config = new HashMap<>();
        config.put("time", new JobParameter(System.currentTimeMillis()));

        JobParameters jobParameters = new JobParameters(config);
        try {
            JobExecution jobExecution = jobLauncher.run(jobConfig.firstJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException
                | JobRestartException
                | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException e) {
            e.getMessage();
        }
    }
}
