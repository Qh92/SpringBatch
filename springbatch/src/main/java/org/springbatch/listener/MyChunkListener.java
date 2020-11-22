package org.springbatch.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

import lombok.extern.slf4j.Slf4j;
/**
 * chunkListener
 * @author Qh
 *
 */
@Slf4j
public class MyChunkListener {
	
	//chunk是step使用的
	@BeforeChunk
	public void beforeChunk(ChunkContext context){ //添加chunk的上下文
		log.info("before chunk ..."+context.getStepContext().getStepName());
	}
	
	@AfterChunk
	public void afterChunk(ChunkContext context){
		log.info("after chunk ..."+context.getStepContext().getStepName());
	}

}
