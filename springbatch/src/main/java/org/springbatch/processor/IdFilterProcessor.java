package org.springbatch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
/**
 * 对id进行过滤
 * @author Qh
 *
 */
@Component
public class IdFilterProcessor implements ItemProcessor<Customer, Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		if(item.getId() % 2 == 0){
			return item;
		}else{
			return null;
		}
		
	}
}
