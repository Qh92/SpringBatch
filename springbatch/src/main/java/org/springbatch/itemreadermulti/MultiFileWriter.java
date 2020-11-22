package org.springbatch.itemreadermulti;

import java.util.List;

import org.springbatch.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("multiFileWriter")
public class MultiFileWriter implements ItemWriter<Customer>{

	@Override
	public void write(List<? extends Customer> items) throws Exception {
		for(Customer customer : items){
			System.out.println(customer);
		}
		
		
	}

}
