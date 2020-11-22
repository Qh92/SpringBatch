package org.springbatch.itemreader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
/**
 * 自定义的reader对象
 * @author Qh
 *
 */
public class MyReader implements ItemReader<String>{
	

	private Iterator<String> iterator;

	//将数据传给构造器
	public MyReader(List<String> list) {
		this.iterator = list.iterator();
	}

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		//一个数据一个数据的读
		if(iterator.hasNext()){
			return this.iterator.next();
		}else{
			return null;
		}
		
	}

}
