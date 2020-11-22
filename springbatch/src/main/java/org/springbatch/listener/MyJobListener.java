package org.springbatch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;
/**
 * jobListener
 * @author Qh
 *
 */
@Slf4j
public class MyJobListener implements JobExecutionListener{

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info(jobExecution.getJobInstance().getJobName()+" before job ....");
		
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info(jobExecution.getJobInstance().getJobName()+" after job ....");
		
	}

}
