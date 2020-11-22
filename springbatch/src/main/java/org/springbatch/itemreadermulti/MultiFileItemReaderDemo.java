package org.springbatch.itemreadermulti;

import org.springbatch.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

@Configuration
public class MultiFileItemReaderDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Value("classpath:/file*.txt")
	private Resource[] fileResources;

	@Autowired
	@Qualifier("multiFileWriter")
	private ItemWriter<? super Customer> multiFileWriter;
	
	@Bean
	public Job multiFileItemReaderDemoJob(){
		return jobBuilderFactory.get("multiFileItemReaderDemoJob")
				.start(multiFileItemReaderDemoStep())
				.build();
	}

	@Bean
	public Step multiFileItemReaderDemoStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("multiFileItemReaderDemoStep")
				.<Customer,Customer>chunk(10)
				.reader(multiFileReader())
				.writer(multiFileWriter)
				.build();
	}

	@Bean
	@StepScope
	public MultiResourceItemReader<Customer> multiFileReader() {
		//还是依次从文件中读取
		MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<Customer>();
		reader.setDelegate(flatFileReader());
		reader.setResources(fileResources);
		return reader;
	}
	
	@Bean
	@StepScope
	public FlatFileItemReader<Customer> flatFileReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		//reader.setResource(new ClassPathResource("customer.txt"));
		//reader.setLinesToSkip(1);//跳过第一行
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
