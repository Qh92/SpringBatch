package org.springbatch.flowdemo;

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

import lombok.extern.slf4j.Slf4j;
/**
 * flow的使用
 * @author Qh
 *
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class FlowDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	/*
	 * 创建若干个Step
	 */
	@Bean
	public Step flowDemoStep1(){
		return stepBuilderFactory.get("flowDemoStep1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step flowDemoStep2(){
		return stepBuilderFactory.get("flowDemoStep2").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step flowDemoStep3(){
		return stepBuilderFactory.get("flowDemoStep3").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	//创建flow对象:指定flow对象包含哪些step,并不会执行step
	@Bean
	public Flow flowDemoFlow(){
		return new FlowBuilder<Flow>("flowDemoFlow")
				.start(flowDemoStep1())
				.next(flowDemoStep3())
				.build();
	}
	
	//创建job对象
	@Bean
	public Job flowDemoJob(){
		return jobBuilderFactory.get("flowDemoJob")
				.start(flowDemoFlow()) //指明这个flow,即指明两个step(step1,step3)
				.next(flowDemoStep2()) //先执行step1-->step3-->step2
				.end()
				.build();
	}

}
