package org.springbatch.itemreaderdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class ItemReaderDbDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	//@Qualifier("dbJdbcWriter")
	private ItemWriter<? super User> dbJdbcWriter;
	
	//创建job
	@Bean
	public Job itemReaderDbJob(){
		return jobBuilderFactory.get("itemReaderDbJob3")
				.start(itemReaderDbStep())
				.build();
	}
	
	//创建step
	@Bean
	public  Step itemReaderDbStep() {
		// TODO Auto-generated method stub
		return stepBuilderFactory.get("itemReaderDbStep3")
				.<User,User>chunk(2)
				.reader(dbJdbcReader())
				.writer(dbJdbcWriter)
				.build();
	}

	//使用JdbcPagingItemReader对象从数据库中读取数据
	@Bean
	@StepScope
	public JdbcPagingItemReader<User> dbJdbcReader() {
		JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<User>();
		reader.setDataSource(dataSource);//指明数据源
		reader.setFetchSize(2);//一次从数据库取多少条数据
		//把读取到的记录转换为User对象
		reader.setRowMapper(new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setAge(rs.getInt(4));
				return user;
			}
		});
		//指定sql语句
		MySqlPagingQueryProvider query = new MySqlPagingQueryProvider();
		query.setSelectClause("id,username,password,age");
		query.setFromClause("from USER");
		//根据哪个字段来进行排序
		Map<String,Order> sort = new HashMap<String,Order>(1);
		sort.put("id", Order.ASCENDING);
		query.setSortKeys(sort);
		reader.setQueryProvider(query);
		return reader;
	}

}
