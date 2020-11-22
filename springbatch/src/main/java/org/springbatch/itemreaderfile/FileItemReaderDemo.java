package org.springbatch.itemreaderfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;
/**
 * 从普通文件中读取数据
 * @author Qh
 *
 */
@Configuration
public class FileItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ItemWriter<Customer> flatFileWriter;
	
	//创建job
	@Bean
	public Job fileItemReaderDemoJob(){
		return jobBuilderFactory.get("fileItemReaderDemoJob")
				.start(fileItemReaderDemoStep())
				.build();
	}
	//创建step
	@Bean
	public Step fileItemReaderDemoStep() {
		return stepBuilderFactory.get("fileItemReaderDemoStep")
				.<Customer,Customer>chunk(5)
				.reader(flatFileReader())
				.writer(flatFileWriter)
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> flatFileReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		reader.setResource(new ClassPathResource("customer.txt"));
		reader.setLinesToSkip(1);//跳过第一行
		//数据解析
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"id","firstname","lastname","birthday"});
		//把解析出的数据映射为customer对象
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstname(fieldSet.readString("firstname"));
				customer.setLastname(fieldSet.readString("lastname"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);
		return reader;
	}
}
