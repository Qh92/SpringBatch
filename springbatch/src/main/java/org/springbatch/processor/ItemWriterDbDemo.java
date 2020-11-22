package org.springbatch.processor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
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

	@Autowired
	private ItemProcessor<Customer,Customer> firstNameUpperProcessor;
	
	@Autowired
	private ItemProcessor<Customer,Customer> idFilterProcessor;
	
	@Bean
	public Job itemWriterDbDemoJob2(){
		return jobBuilderFactory.get("itemWriterDbDemoJob2")
				.start(itemWriterDbDemoStep2())
				.build();
	}

	@Bean
	public Step itemWriterDbDemoStep2() {
		return stepBuilderFactory.get("itemWriterDbDemoStep2")
				.<Customer,Customer>chunk(10)
				.reader(flatFileReader)
				//.processor(firstNameUpperProcessor) //单个数据处理的方式
				.processor(process())
				.writer(itemWriterDb)
				.build();
	}
	
	//有多种处理数据的方法,将多种处理方式组合在一起
	@Bean
	public CompositeItemProcessor<Customer,Customer> process(){
		CompositeItemProcessor<Customer,Customer> process = new CompositeItemProcessor<Customer,Customer>();
		List<ItemProcessor<Customer,Customer>> delegates = new ArrayList<>();
		delegates.add(firstNameUpperProcessor);
		delegates.add(idFilterProcessor);
		process.setDelegates(delegates);
		return process;
	}

}
