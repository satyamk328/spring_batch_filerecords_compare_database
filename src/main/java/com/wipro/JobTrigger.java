package com.wipro;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTrigger {
	
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job employeeJob;

    @PostConstruct
    public void runJob1() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startTime", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(employeeJob, jobParameters);
    }
    
    @Scheduled(cron = "0 0 */6 * * *")
    public void runJob() {
        try {
            System.out.println("Starting the batch job...");
            jobLauncher.run(employeeJob, new JobParameters());
            System.out.println("Batch job completed successfully.");
        } catch (Exception e) {
            System.out.println("Batch job failed: " + e.getMessage());
        }
    }
}
