package org.springbatch.processor;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

@Configuration
public class FlatFileReaderConfig {
	
	@Bean
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
				customer.setFirstName(fieldSet.readString("firstname"));
				customer.setLastName(fieldSet.readString("lastname"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);
		return reader;
	}

}
