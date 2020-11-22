package org.springbatch.itemwriter;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("myWriter")
public class MyWriter implements ItemWriter<String>{

	@Override
	public void write(List<? extends String> items) throws Exception {
		System.out.println(items.size());
		for(String item : items){
			System.out.println(item);
		}
	}

}
