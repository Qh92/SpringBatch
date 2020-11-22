package org.springbatch.firstproject;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
 * springbatch 入门程序
 * @author Qh
 *
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class JobConfiguration {
	
	//注入创建任务对象的对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//任务的执行由step决定,一个job可以创建多个step
	//注入创建step对象的对象
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//创建任务
	@Bean
	public Job helloWorldJob(){
		return jobBuilderFactory.get("helloWorldJob").start(step()).build();
	}
	
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step").tasklet(new Tasklet(){

			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("first job&step:hello world");
				return RepeatStatus.FINISHED;//这个状态决定是否正常执行后续的step
			}
			
		}).build();
	}
	

}
