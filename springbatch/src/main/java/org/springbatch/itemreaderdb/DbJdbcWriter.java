package org.springbatch.itemreaderdb;

import java.io.Serializable;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.util.SocketUtils;


@Component
//@Component("dbJdbcWriter")
public class DbJdbcWriter implements ItemWriter<User>{
	@Override
	public void write(List<? extends User> items) throws Exception {
		for(User user : items){
			System.out.println(user);
		}
	}
}
