package org.springbatch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 将firstName全部改为大写
 * @author Qh
 *
 */
@Component
public class FirstNameUpperProcessor implements ItemProcessor<Customer,Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		item.setFirstName(item.getFirstName().toUpperCase());
		return item;
	}

}
