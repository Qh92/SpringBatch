package org.springbatch.skiplistener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
public class MySkipListener implements SkipListener<String, String>{

	@Override
	public void onSkipInRead(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSkipInWrite(String item, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSkipInProcess(String item, Throwable t) {
		System.out.println(item+" occur exception "+ t);
	}

}
