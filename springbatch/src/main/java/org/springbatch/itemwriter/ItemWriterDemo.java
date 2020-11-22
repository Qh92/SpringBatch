package org.springbatch.itemwriter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemWriterDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("myWriter")
	private ItemWriter<? super String> myWriter;
	
	//创建job
	@Bean
	public Job itemWriterDemoJob2(){
		return jobBuilderFactory.get("itemWriterDemoJob2")
				.start(itemWriterDemoStep2())
				.build();
	}

	//创建step
	@Bean
	public Step itemWriterDemoStep2() {
		return stepBuilderFactory.get("itemWriterDemoStep2")
				.<String,String>chunk(5)
				.reader(myRead())
				.writer(myWriter)
				.build();
	}

	@Bean
	public ItemReader<String> myRead() {
		List<String> items = new ArrayList<>();
		for(int i=0;i<=50;i++){
			items.add("java"+i);
		}
		return new ListItemReader<>(items);
	}
}
