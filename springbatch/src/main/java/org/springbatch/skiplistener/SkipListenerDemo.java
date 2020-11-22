package org.springbatch.skiplistener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
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
public class SkipListenerDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> skipListenerItemWriter;
	
	@Autowired
	private ItemProcessor<String,String> skipListenerItemProcessor;

	@Autowired
	private SkipListener<String, String> mySkipListener;
	
	@Bean
	public Job skipListenerDemoJob(){
		return jobBuilderFactory.get("skipListenerDemoJob")
				.start(skipListenerDemoStep())
				.build();
	}

	@Bean
	public Step skipListenerDemoStep() {
		return stepBuilderFactory.get("skipListenerDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipListenerItemProcessor)
				.writer(skipListenerItemWriter)
				.faultTolerant() //容错
				.skip(CustomRetryException.class) //指明发生当前异常，就跳过
				.skipLimit(5)
				.listener(mySkipListener) //错误跳过监听器
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
