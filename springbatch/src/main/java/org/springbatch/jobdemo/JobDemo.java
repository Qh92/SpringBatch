package org.springbatch.jobdemo;

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
 * job的创建
 * @author Qh
 *
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class JobDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//方式一：创建多个Step,按照默认的执行顺序分别执行 step1-->step2-->step3
	/*@Bean
	public Job jobDemoJob(){
		return jobBuilderFactory.get("jobDemoJob")
				.start(step1())
				.next(step2())
				.next(step3())
				.build();
	}*/
	
	//方式二：
	@Bean
	public Job jobDemoJob(){
		
		return jobBuilderFactory.get("jobDemoJob")
				.start(step1()) //开始执行step1
				.on("COMPLIETED").to(step2()) //step1满足条件 COMPLIETED(成功结束后) 执行 step2
				.from(step2()).on("COMPLIETED").to(step3()) // step2满足条件 COMPLIETED(成功结束后) 执行 step3 fail() stopAndRestart()
				.from(step3()).end()  //结束 end
				.build();
	}
	
	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("正常进行step1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("正常进行step2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step step3(){
		return stepBuilderFactory.get("step3").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("正常进行step3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

}
