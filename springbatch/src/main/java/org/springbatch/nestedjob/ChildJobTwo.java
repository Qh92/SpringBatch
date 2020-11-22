package org.springbatch.nestedjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
/**
 * 子job2
 * @author Qh
 *
 */
@Configuration
@Slf4j
public class ChildJobTwo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step childJobTwoStep1(){
		return stepBuilderFactory.get("childJobTwoStep1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("childJobTwoStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Step childJobTwoStep2(){
		return stepBuilderFactory.get("childJobTwoStep2").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("childJobTwoStep2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Job childJob2(){
		return jobBuilderFactory.get("childJobTwo")
				.start(childJobTwoStep1())
				.next(childJobTwoStep2())
				.build();
	}
}
