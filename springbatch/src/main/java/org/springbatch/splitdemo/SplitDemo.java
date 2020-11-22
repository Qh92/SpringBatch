package org.springbatch.splitdemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * 将多个flow设置并发执行
 * @author Qh
 *
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class SplitDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	/*
	 * 创建若干个step
	 */
	@Bean
	public Step splitDemoStep1(){
		return stepBuilderFactory.get("splitDemoStep1").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step splitDemoStep2(){
		return stepBuilderFactory.get("splitDemoStep2").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step splitDemoStep3(){
		return stepBuilderFactory.get("splitDemoStep3").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	//创建flow对象
	@Bean
	public Flow splitDemoFlow1(){
		log.info("开始执行splitDemoFlow1");
		return new FlowBuilder<Flow>("splitDemoFlow1")
				.start(splitDemoStep3())
				.build();
	}
	
	@Bean
	public Flow splitDemoFlow2(){
		log.info("开始执行splitDemoFlow2");
		return new FlowBuilder<Flow>("splitDemoFlow2")
				.start(splitDemoStep2())
				.next(splitDemoStep1())
				.build();
	}
	
	//创建任务job
	@Bean
	public Job splitDemoJob(){
		return jobBuilderFactory.get("splitDemoJob")
				.start(splitDemoFlow1())
				.split(new SimpleAsyncTaskExecutor())
				.add(splitDemoFlow2())
				.end()
				.build();
	}

}
