package org.springbatch.itemreader;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//创建job
	@Bean
	public Job ItemReaderDemoJob(){
		return jobBuilderFactory.get("ItemReaderDemoJob")
				.start(itemReaderDemoStep())
				.build();
	}

	//创建step
	@Bean
	public Step itemReaderDemoStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("itemReaderDemoStep")
				.<String,String>chunk(2)
				.reader(itemReaderDemoRead())
				.writer(list->{
					for(String item : list){
						log.error(item+".............");
					}
				})
				.build();
	}

	//自定义reader对象
	@Bean
	public MyReader itemReaderDemoRead() {
		List<String> data = Arrays.asList("cat","dog","pig","duck");
		return new MyReader(data);
	}

}
