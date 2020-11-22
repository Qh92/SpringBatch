package org.springbatch.error;

import java.util.Map;

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

@Configuration
public class ErrorDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job errorsDemoJob(){
		return jobBuilderFactory.get("errorsDemoJob")
				.start(errorStep1())
				.next(errorStep2())
				.build();
	}
	
	@Bean
	public Step errorStep1(){
		return stepBuilderFactory.get("errorStep1")
				.tasklet(errorHandling())
				.build();
	}
	
	@Bean
	public Step errorStep2(){
		return stepBuilderFactory.get("errorStep2")
				.tasklet(errorHandling())
				.build();
	}
	
	@Bean
	public Tasklet errorHandling() {
		return new Tasklet(){

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				//获取step执行的上下文
				Map<String,Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
				if(stepExecutionContext.containsKey("qinhao")){
					System.out.println("The second run will success");
					return RepeatStatus.FINISHED;
				}else{
					System.out.println("The first run will fail");
					chunkContext.getStepContext().getStepExecution().getExecutionContext().put("qinhao", "test");
					throw new RuntimeException("error....");
				}
			}
			
		};
	}

}
