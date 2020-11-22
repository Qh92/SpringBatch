package org.springbatch.nestedjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * job的嵌套
 * @author Qh
 *
 */
@Configuration
@Slf4j
public class NestedDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private Job childJob2;
	
	@Autowired
	private Job childJob1;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	//启动父job需要在配置文件中添加 spring.batch.job.names=parentJob
	@Bean
	public Job parentJob(JobRepository jobRepository,PlatformTransactionManager transactionManager){
		return jobBuilderFactory.get("parentJob")
				.start(childJob1(jobRepository,transactionManager))
				.next(childJob2(jobRepository,transactionManager))
				.build();
	}

	//返回的是Job类型的Step,特殊的Step
	private Step childJob2(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		/*
		 * 需要使用 JobStepBuilder
		 */
		return new JobStepBuilder(new StepBuilder("childJobTwo"))
				.job(childJob2)
				.launcher(jobLauncher)//启动父Job来启动子job
				.repository(jobRepository)
				.transactionManager(transactionManager)
				.build();
	}

	private Step childJob1(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		
		return new JobStepBuilder(new StepBuilder("childJobOne"))
				.job(childJob1)
				.launcher(jobLauncher)//启动父Job来启动子job
				.repository(jobRepository)
				.transactionManager(transactionManager)
				.build();
	}
}
