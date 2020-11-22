package org.springbatch.itemreaderxml;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class XmlItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("xmlFileWriter")
	private ItemWriter<Customer> xmlFileWriter;
	
	
	@Bean
	public Job xmlItemReaderDemoJob(){
		return jobBuilderFactory.get("xmlItemReaderDemoJob")
				.start(xmlItemReaderDemoStep())
				.build();
	}


	@Bean
	public Step xmlItemReaderDemoStep() {
		return stepBuilderFactory.get("xmlItemReaderDemoStep")
				.<Customer,Customer>chunk(2)
				.reader(xmlFileReader())
				.writer(xmlFileWriter)
				.build();
	}


	@Bean
	@StepScope
	public ItemReader<Customer> xmlFileReader() {
		StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
		System.out.println("<<<<<<<<<<<  11111111111111");
		reader.setResource(new ClassPathResource("customer.xml"));
		//指定需要处理的根标签
		reader.setFragmentRootElementName("customer");
		//把xml转成对象
		XStreamMarshaller unmarshaller = new XStreamMarshaller();
		System.out.println("<<<<<<<<<<  2222222222222222");
		Map<String,Class> map = new HashMap<>();
		map.put("customer", Customer.class);
		System.out.println("<<<<<<<<<<<  3333333333333333");
		unmarshaller.setAliases(map);
		reader.setUnmarshaller(unmarshaller);
		System.out.println(reader);
		return reader;
	}
	
}
