package org.springbatch.deciderdemo;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
/**
 * 决策器的使用
 * @author Qh
 *
 */
@Configuration
@EnableBatchProcessing
@Slf4j
public class DeciderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	/*
	 * 创建step
	 */
	@Bean
	public Step deciderDemoStep1(){
		return stepBuilderFactory.get("deciderDemoStep1").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("deciderDemoStep1");
				return RepeatStatus.FINISHED;
			}
			
		}).build();
	}
	
	@Bean
	public Step deciderDemoStep2(){
		return stepBuilderFactory.get("deciderDemoStep2").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("even");
				return RepeatStatus.FINISHED;
			}
			
		}).build();
	}
	
	
	@Bean
	public Step deciderDemoStep3(){
		return stepBuilderFactory.get("deciderDemoStep3").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("odd");
				return RepeatStatus.FINISHED;
			}
			
		}).build();
	}
	
	//创建决策器
	@Bean
	public JobExecutionDecider myDecider(){
		return new MyDecider();
	}
	
	//创建任务
	@Bean
	public Job deciderDemoJob(){
		/*
		 * 整个流程为，开始count++后，值为奇数，则执行step3，step3执行完后，
		 * 再次执行决策器，现在count++后，值为偶数，则执行step2，step2执行完成后，就end结束
		 */
		return jobBuilderFactory.get("deciderDemoJob")
				.start(deciderDemoStep1())
				.next(myDecider()) //执行决策器
				.from(myDecider()).on("even").to(deciderDemoStep2()) //如果决策器返回偶数，则执行step2
				.from(myDecider()).on("odd").to(deciderDemoStep3())  //如果决策器返回奇数，则执行step3
				.from(deciderDemoStep3()).on("*").to(myDecider())    //step3无论返回什么，都再执行决策器
				.end()
				.build();
	}

}
