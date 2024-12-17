package com.wipro.listener;

import java.io.File;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HealthListener implements JobExecutionListener {

    private static final String FILE_PATH = "D:\\temp\\employee.csv";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.isDirectory()) {
            jobExecution.setStatus(BatchStatus.FAILED);
            jobExecution.addFailureException(new IllegalStateException("CSV file not found in the folder."));
            throw new IllegalStateException("CSV file not found. Job terminated.");
        }
        System.out.println("Job started at: "+ jobExecution.getStartTime());
        System.out.println("Status of the Job: "+jobExecution.getStatus());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job Ended at: "+ jobExecution.getEndTime());
        System.out.println("Status of the Job: "+jobExecution.getStatus());
    }
}
