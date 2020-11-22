package org.springbatch.listener;


import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ListenerDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job listenerJob(){
		log.debug("listenerJob going into..........");
		return jobBuilderFactory.get("listenerJob")
				.start(listenerStep1())
				.listener(new MyJobListener())
				.build();
	}

	@Bean
	public Step listenerStep1() {
		log.debug("listenerStep1 going into..........");
		return stepBuilderFactory.get("step11")
				.<String,String>chunk(2) //读，写数据，每读完2个数据后进行数据输出
				.faultTolerant() //容错
				.listener(new MyChunkListener())
				.reader(listenerRead())
				.writer(listenerWriter())
				.build();
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

}
