package org.springbatch.parameterjob;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * job设置参数
 * @author Qh
 *
 */
@Configuration
@Slf4j
public class ParametersDemo implements StepExecutionListener{
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private Map<String,JobParameter> paramters;
	
	@Bean
	public Job paramterJob(){
		return jobBuilderFactory.get("paramterJob")
				.start(paramterStep())
				.build();
	}
	//job执行的是step,job使用的数据是在step中使用,只需给step传递数据
	/**
	 * 如何给step传递参数
	 * 使用监听，使用step级别的监听来传递数据
	 * @return
	 */
	@Bean
	public Step paramterStep() {
		/*
		 * 结论：step监听是整个step的监听，即使是chunk方式，也只会有一次before和after监听
		 * 输出结果如下：
		 * step开始执行..........
		 * java
		 * spring
		 * mybatis
		 * step执行完成..........
		 */
		return /*((SimpleStepBuilder<String, String>) stepBuilderFactory.get("paramterStep")
				.<String,String>chunk(2) //读，写数据，每读完2个数据后进行数据输出
				.faultTolerant() //容错
				.listener(this))
				.reader(listenerRead())
				.writer(listenerWriter())
				.build();*/
				stepBuilderFactory.get("paramterStep")
				.listener(this)
				.tasklet(new Tasklet(){

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
							throws Exception {
						//输出接收到的参数
						log.info("接收到的参数:"+paramters.get("info"));//springbatch
						return RepeatStatus.FINISHED;
					}
					
				}).build();
	}
	
	//写数据
	@Bean
	public ItemWriter<String> listenerWriter() {
		return new ItemWriter<String>(){
			@Override
			public void write(List<? extends String> items) throws Exception {
				for(String item : items){
					log.info(item);
				}
			}
		};
	}

	//读数据
	@Bean
	public ItemReader<String> listenerRead() {
		return new ListItemReader<>(Arrays.asList("java","spring","mybatis"));
	}
	
	//在before处获取参数
	@Override
	public void beforeStep(StepExecution stepExecution) {//通过StepExecution来获取参数
		log.error("step开始执行..........");
		paramters = stepExecution.getJobParameters().getParameters();
		
	}
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("step执行完成..........");
		return null;
	}
}
