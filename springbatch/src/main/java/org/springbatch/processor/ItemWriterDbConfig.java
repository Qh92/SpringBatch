package org.springbatch.processor;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemWriterDbConfig {
	
	@Autowired
	private DataSource dataSource;
	@Bean
	public JdbcBatchItemWriter<Customer> itemWriterDb(){
		JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<Customer>();
		writer.setDataSource(dataSource);
		writer.setSql("insert into customer( id,firstName,lastName,birthday) values "+
					"(:id,:firstName,:lastName,:birthday)");
		//BeanPropertyItemSqlParameterSourceProvider这个对象可以将customer对象属性的值映射到对应到占位符上
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
		return writer;
	}
}
