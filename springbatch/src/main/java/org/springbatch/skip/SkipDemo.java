package org.springbatch.skip;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkipDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> skipItemWriter;
	
	@Autowired
	private ItemProcessor<String,String> skipItemProcessor;
	
	@Bean
	public Job skipDemoJob(){
		return jobBuilderFactory.get("skipDemoJob")
				.start(skipDemoStep())
				.build();
	}

	@Bean
	public Step skipDemoStep() {
		return stepBuilderFactory.get("skipDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipItemProcessor)
				.writer(skipItemWriter)
				.faultTolerant() //容错
				.skip(CustomRetryException.class) //指明发生当前异常，就跳过
				.skipLimit(5)
				.build();
	}

	@Bean
	@StepScope
	public ItemReader<String> reader() {
		List<String> items = new ArrayList<>();
		for(int i=0;i<60;i++){
			items.add(String.valueOf(i));
		}
		ListItemReader<String> reader = new ListItemReader<>(items);
		return reader;
	}

}
