package org.springbatch.itemwriterdb;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemWriterDbDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ItemReader<Customer> flatFileReader;

	@Autowired
	private ItemWriter<Customer> itemWriterDb;
	
	@Bean
	public Job itemWriterDbDemoJob(){
		return jobBuilderFactory.get("itemWriterDbDemoJob")
				.start(itemWriterDbDemoStep())
				.build();
	}

	@Bean
	public Step itemWriterDbDemoStep() {
		
		return stepBuilderFactory.get("itemWriterDbDemoStep")
				.<Customer,Customer>chunk(10)
				.reader(flatFileReader)
				.writer(itemWriterDb)
				.build();
	}

}
