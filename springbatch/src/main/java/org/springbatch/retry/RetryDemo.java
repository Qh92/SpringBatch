package org.springbatch.retry;

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
public class RetryDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> retryItemWriter;
	
	@Autowired
	private ItemProcessor<String,String> retryItemProcessor;
	
	@Bean
	public Job retryDemoJob(){
		return jobBuilderFactory.get("retryDemoJob")
				.start(retryDemoStep())
				.build();
	}

	@Bean
	public Step retryDemoStep() {
		return stepBuilderFactory.get("retryDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(retryItemProcessor)
				.writer(retryItemWriter)
				.faultTolerant() //容错
				.retry(CustomRetryException.class) //发生CustomRetryException异常，触发重试
				.retryLimit(5) //尝试重试的次数  ，超过次数就不会重试，任务就会停止。 重试的次数：reader、processor、writer 总计的异常次数。没有超过5次就会重试，超过就不会重试了，任务就会终止
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
