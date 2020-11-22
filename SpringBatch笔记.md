## SpringBatch笔记

### 一、概述

Spring Batch是一个轻量级的，全面的批处理框架，旨在支持开发对企业系统的日常运营至关重要的强大的批处理应用程序。Spring Batch建立在人们期望的Spring框架特性（生产力，基于POJO的开发方法和普遍的易用性）的基础上，同时使开发人员在必要时可以轻松访问和利用更高级的企业服务。Spring Batch不是一个调度框架。商业空间和开放源代码空间中都有许多好的企业调度程序（例如Quartz，Tivoli，Control-M等）。它旨在与计划程序一起工作，而不是替换计划程序。

Spring Batch提供了可重用的功能，这些功能对于处理大量记录至关重要，包括日志记录/跟踪，事务管理，作业处理统计信息，作业重启，跳过和资源管理。它还提供了更高级的技术服务和功能，这些功能和功能通过优化和分区技术实现了极高容量和高性能的批处理作业。Spring Batch可用于简单的用例（例如，将文件读入数据库或运行存储过程），也可以用于复杂的大量用例（例如，在数据库之间移动大量数据，对其进行转换等））。大量批处理作业可以以高度可扩展的方式利用框架来处理大量信息。

### 二、使用场景

典型的批处理程序通常：

- 从数据库，文件或队列中读取大量记录。
- 以某种方式处理数据。
- 以修改后的形式写回数据。

Spring Batch自动执行此基本批处理迭代，从而提供了将一组类似的交易作为一组处理的能力，通常在脱机环境中无需任何用户交互即可。批处理作业是大多数IT项目的一部分，Spring Batch是唯一提供可靠的企业级解决方案的开源框架。

业务场景

- 定期提交批处理
- 并发批处理：作业的并行处理
- 分阶段的企业消息驱动的处理
- 大规模并行批处理
- 失败后手动或计划重启
- 顺序处理相关步骤（扩展了工作流程驱动的批次）
- 部分处理：跳过记录（例如，回滚时）
- 整批处理，适用于小批处理或现有存储过程/脚本的情况

技术目标

- 批处理开发人员使用Spring编程模型：专注于业务逻辑，并让框架处理基础结构。
- 在基础结构，批处理执行环境和批处理应用程序之间明确分离关注点。
- 提供通用的核心执行服务作为所有项目都可以实现的接口。
- 提供可以直接使用的核心执行接口的简单和默认实现。
- 通过在所有层中利用spring框架，轻松配置，定制和扩展服务。
- 所有现有的核心服务应易于替换或扩展，而不会影响基础架构层。
- 提供一个简单的部署模型，其架构JAR与使用Maven构建的应用程序完全分开。



### 三、处理流程

![image-20201121104623081](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201121104623081.png)





### 四、相关知识

#### 1.创建项目

方式一：官网下创建springboot+springbatch项目，右边选择需要的依赖

![image-20201121113608121](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201121113608121.png)

方式二：通过ide  eclipse -->spring-spring starter project 



#### 2.入门程序

springbatch是基于任务实现批处理

启动程序：

```java
@SpringBootApplication
public class SpringbatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchApplication.class, args);
	}

}
```



```java
@Configuration
@EnableBatchProcessing
@Slf4j
public class JobConfiguration {
	//注入创建任务对象的对象
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	//任务的执行由step决定,一个job可以创建多个step
	//注入创建step对象的对象
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	//创建任务
	@Bean
	public Job helloWorldJob(){
		return jobBuilderFactory.get("helloWorldJob").start(step()).build();
	}
	@Bean
	public Step step(){
		return stepBuilderFactory.get("step").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("first job&step:hello world");
				return RepeatStatus.FINISHED;//这个状态决定是否正常执行后续的step
			}
		}).build();
	}	
}
```

运行结果：

```
2020-11-21 12:02:52.267  INFO 6704 --- [           main] o.s.firstproject.SpringbatchApplication  : Starting SpringbatchApplication on DESKTOP-FCMFQP9 with PID 6704 (C:\Users\Qh\Desktop\springbatch\springbatch\target\classes started by Qh in C:\Users\Qh\Desktop\springbatch\springbatch)
2020-11-21 12:02:52.270  INFO 6704 --- [           main] o.s.firstproject.SpringbatchApplication  : No active profile set, falling back to default profiles: default
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
2020-11-21 12:02:54.287  INFO 6704 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2020-11-21 12:02:54.300  WARN 6704 --- [           main] com.zaxxer.hikari.util.DriverDataSource  : Registered driver with driverClassName=com.mysql.jdbc.Driver was not found, trying direct instantiation.
2020-11-21 12:02:55.767  INFO 6704 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2020-11-21 12:02:56.287  INFO 6704 --- [           main] o.s.b.c.r.s.JobRepositoryFactoryBean     : No database type set, using meta data indicating: MYSQL
2020-11-21 12:02:56.595  INFO 6704 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2020-11-21 12:02:56.786  INFO 6704 --- [           main] o.s.firstproject.SpringbatchApplication  : Started SpringbatchApplication in 6.857 seconds (JVM running for 8.936)
2020-11-21 12:02:56.792  INFO 6704 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 12:02:57.050  INFO 6704 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=helloWorldJob]] launched with the following parameters: [{}]
2020-11-21 12:02:57.167  INFO 6704 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step]
2020-11-21 12:02:57.200  INFO 6704 --- [           main] o.s.firstproject.JobConfiguration        : first job&step:hello world
2020-11-21 12:02:57.216  INFO 6704 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step] executed in 49ms
2020-11-21 12:02:57.283  INFO 6704 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=helloWorldJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 117ms
2020-11-21 12:02:57.289  INFO 6704 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2020-11-21 12:02:57.300  INFO 6704 --- [extShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

并将job相关信息入库

![image-20201121123620680](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201121123620680.png)

![image-20201121123641243](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201121123641243.png)

#### 3.springbatch相关表

mysql建表语句

```SQL
CREATE TABLE BATCH_JOB_INSTANCE  (
	JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT ,
	JOB_NAME VARCHAR(100) NOT NULL,
	JOB_KEY VARCHAR(32) NOT NULL,
	constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION  (
	JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT  ,
	JOB_INSTANCE_ID BIGINT NOT NULL,
	CREATE_TIME DATETIME NOT NULL,
	START_TIME DATETIME DEFAULT NULL ,
	END_TIME DATETIME DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED DATETIME,
	JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
	constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
	references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
	TYPE_CD VARCHAR(6) NOT NULL ,
	KEY_NAME VARCHAR(100) NOT NULL ,
	STRING_VAL VARCHAR(250) ,
	DATE_VAL DATETIME DEFAULT NULL ,
	LONG_VAL BIGINT ,
	DOUBLE_VAL DOUBLE PRECISION ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION  (
	STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT NOT NULL,
	STEP_NAME VARCHAR(100) NOT NULL,
	JOB_EXECUTION_ID BIGINT NOT NULL,
	START_TIME DATETIME NOT NULL ,
	END_TIME DATETIME DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	COMMIT_COUNT BIGINT ,
	READ_COUNT BIGINT ,
	FILTER_COUNT BIGINT ,
	WRITE_COUNT BIGINT ,
	READ_SKIP_COUNT BIGINT ,
	WRITE_SKIP_COUNT BIGINT ,
	PROCESS_SKIP_COUNT BIGINT ,
	ROLLBACK_COUNT BIGINT ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED DATETIME,
	constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
	STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT ,
	constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
	references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
	JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT ,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ (
	ID BIGINT NOT NULL,
	UNIQUE_KEY CHAR(1) NOT NULL,
	constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_EXECUTION_SEQ (
	ID BIGINT NOT NULL,
	UNIQUE_KEY CHAR(1) NOT NULL,
	constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_SEQ (
	ID BIGINT NOT NULL,
	UNIQUE_KEY CHAR(1) NOT NULL,
	constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_SEQ);

```



#### 4.核心API

框架一共有4个主要角色：JobLauncher是任务启动器，通过它来启动任务，可以看做是程序的入口。Job代表着一个具体的任务。Step代表着一个具体的步骤，一个Job可以包含多个Step。JobRepository是存储数据的地方，可以看做是一个数据库的接口，在任务执行的时候需要通过它来记录任务状态等等信息。

①JobInstance：该领域概念和Job的关系与java中实例和类的关系一样。Job定义了一个工作流程。JobInstance就是该工作流程的一个具体实例。一个Job可以有多个JobInstance。多个JobInstance之间的区分就要靠另外一个领域概念JobParameters了。

![image-20201121122628118](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201121122628118.png)





②JobParameters:是一组可以贯穿整个Job的运行时配置参数。不同的配置将产生不同的JobInstance。如果你是使用相同的JobParameters运行同一个Job.那么这次运行会重用上一次创建的JobInstance。另外，SpringBatch还非常贴心的提供了让JobParameters中的部分参数不参与JobInstance区分

![image-20201121132509912](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201121132509912.png)

③JobExecution：该领域概念表示JobInstance的一次运行。JobInstance运行时可能会成功或者失败。每一次JobInstance的运行都会产生一个JobExecution。同一个JobInstance（JobParameters相同）可以多次运行，这样该JobInstance将对应多个JobExecution。

④StepExecution:类似于JobExecution,该领域对象表示Step的一次运行。Step是Job的一部分。因此一个StepExecution会关联到一个JobExecution。另外，该对象还会存储很多与该次Step运行相关的所有数据。因此该对象也有很多属性，并且需要持久化以支持一些Spring batch的特性。

⑤ExecutionContext：从前面的JobExecution,StepExecution的属性介绍中已经提到了该领域概念。说穿了，该领域概念就是一个容器。该容器由Batch框架控制。框架会对该容器持久化。开发人员可以使用该容器保存一些数据。以支持在整个BatchJob或者整个Step中共享这些数据。



#### 5.Job的创建和使用

Job:作业，批处理中的核心概念，是Batch操作的基础单元。每个Job作业由一个或者多个Step组成。

```java
@Configuration
@EnableBatchProcessing
@Slf4j
public class JobDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//方式一：创建多个Step,按照默认的执行顺序分别执行 step1-->step2-->step3
	/*@Bean
	public Job jobDemoJob(){
		return jobBuilderFactory.get("jobDemoJob")
				.start(step1())
				.next(step2())
				.next(step3())
				.build();
	}*/
	//方式二：
	@Bean
	public Job jobDemoJob(){
		
		return jobBuilderFactory.get("jobDemoJob")
				.start(step1()) //开始执行step1
				.on("COMPLIETED").to(step2()) //step1满足条件 COMPLIETED(成功结束后) 执行 step2
				.from(step2()).on("COMPLIETED").to(step3()) // step2满足条件 COMPLIETED(成功结束后) 执行 step3 fail() stopAndRestart()
				.from(step3()).end()  //结束 end
				.build();
	}
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("正常进行step1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("正常进行step2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}	
	@Bean
	public Step step3(){
		return stepBuilderFactory.get("step3").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("正常进行step3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
}
```

执行结果：

```
2020-11-21 13:49:07.651  INFO 14632 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 13:49:07.773  INFO 14632 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=jobDemoJob]] launched with the following parameters: [{}]
2020-11-21 13:49:07.858  INFO 14632 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step1]
2020-11-21 13:49:07.882  INFO 14632 --- [           main] org.springbatch.firstjob.JobDemo         : 正常进行step1
2020-11-21 13:49:07.905  INFO 14632 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step1] executed in 46ms
2020-11-21 13:49:07.937  INFO 14632 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step2]
2020-11-21 13:49:07.949  INFO 14632 --- [           main] org.springbatch.firstjob.JobDemo         : 正常进行step2
2020-11-21 13:49:07.962  INFO 14632 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step2] executed in 25ms
2020-11-21 13:49:08.023  INFO 14632 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step3]
2020-11-21 13:49:08.039  INFO 14632 --- [           main] org.springbatch.firstjob.JobDemo         : 正常进行step3
2020-11-21 13:49:08.052  INFO 14632 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step3] executed in 28ms
2020-11-21 13:49:08.072  INFO 14632 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=jobDemoJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 252ms
```



#### 6.Flow的创建和使用

①Flow是多个Step的集合

②可以被多个Job复用

③使用FlowBuilder来创建

```java
@Configuration
@EnableBatchProcessing
@Slf4j
public class FlowDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	/*
	 * 创建若干个Step
	 */
	@Bean
	public Step flowDemoStep1(){
		return stepBuilderFactory.get("flowDemoStep1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Step flowDemoStep2(){
		return stepBuilderFactory.get("flowDemoStep2").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Step flowDemoStep3(){
		return stepBuilderFactory.get("flowDemoStep3").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	//创建flow对象:指定flow对象包含哪些step,并不会执行step
	@Bean
	public Flow flowDemoFlow(){
		return new FlowBuilder<Flow>("flowDemoFlow")
				.start(flowDemoStep1())
				.next(flowDemoStep3())
				.build();
	}
	//创建job对象
	@Bean
	public Job flowDemoJob(){
		return jobBuilderFactory.get("flowDemoJob")
				.start(flowDemoFlow()) //指明这个flow,即指明两个step(step1,step3)
				.next(flowDemoStep2()) //先执行step1-->step3-->step2
				.end()
				.build();
	}

}
```

执行结果：

```
2020-11-21 14:06:27.914  INFO 12048 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 14:06:28.060  INFO 12048 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=flowDemoJob]] launched with the following parameters: [{}]
2020-11-21 14:06:28.176  INFO 12048 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [flowDemoStep1]
2020-11-21 14:06:28.216  INFO 12048 --- [           main] org.springbatch.flowdemo.FlowDemo        : flowDemoStep1
2020-11-21 14:06:28.234  INFO 12048 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [flowDemoStep1] executed in 58ms
2020-11-21 14:06:28.291  INFO 12048 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [flowDemoStep3]
2020-11-21 14:06:28.304  INFO 12048 --- [           main] org.springbatch.flowdemo.FlowDemo        : flowDemoStep3
2020-11-21 14:06:28.323  INFO 12048 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [flowDemoStep3] executed in 32ms
2020-11-21 14:06:28.385  INFO 12048 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [flowDemoStep2]
2020-11-21 14:06:28.398  INFO 12048 --- [           main] org.springbatch.flowdemo.FlowDemo        : flowDemoStep2
2020-11-21 14:06:28.409  INFO 12048 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [flowDemoStep2] executed in 24ms
2020-11-21 14:06:28.440  INFO 12048 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=flowDemoJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 311ms
```



#### 7.split实现并发执行

实现任务中的多个step或多个flow并发执行

①创建若干个step

②创建两个flow

③创建一个任务包含以上两个flow,并让这两个flow并发执行

```java
@Configuration
@EnableBatchProcessing
@Slf4j
public class SplitDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	/*
	 * 创建若干个step
	 */
	@Bean
	public Step splitDemoStep1(){
		return stepBuilderFactory.get("splitDemoStep1").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Step splitDemoStep2(){
		return stepBuilderFactory.get("splitDemoStep2").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Step splitDemoStep3(){
		return stepBuilderFactory.get("splitDemoStep3").tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("flowDemoStep3");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	//创建flow对象
	@Bean
	public Flow splitDemoFlow1(){
		log.info("开始执行splitDemoFlow1");
		return new FlowBuilder<Flow>("splitDemoFlow1")
				.start(splitDemoStep3())
				.build();
	}
	@Bean
	public Flow splitDemoFlow2(){
		log.info("开始执行splitDemoFlow2");
		return new FlowBuilder<Flow>("splitDemoFlow2")
				.start(splitDemoStep2())
				.next(splitDemoStep1())
				.build();
	}
	//创建任务job
	@Bean
	public Job splitDemoJob(){
		return jobBuilderFactory.get("splitDemoJob")
				.start(splitDemoFlow1())
				.split(new SimpleAsyncTaskExecutor())
				.add(splitDemoFlow2())
				.end()
				.build();
	}
}
```

执行结果：因为是并发执行，并不能保证 step按照step3-->step2-->step1的执行顺序执行

打印结果也是如此，按照step2-->step3-->step1顺序执行

```
2020-11-21 14:39:22.946  INFO 18264 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 14:39:23.093  INFO 18264 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=splitDemoJob]] launched with the following parameters: [{}]
2020-11-21 14:39:23.275  INFO 18264 --- [cTaskExecutor-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [splitDemoStep2]
2020-11-21 14:39:23.286  INFO 18264 --- [cTaskExecutor-2] o.s.batch.core.job.SimpleStepHandler     : Executing step: [splitDemoStep3]
2020-11-21 14:39:23.308  INFO 18264 --- [cTaskExecutor-1] org.springbatch.splitdemo.SplitDemo      : flowDemoStep2
2020-11-21 14:39:23.308  INFO 18264 --- [cTaskExecutor-2] org.springbatch.splitdemo.SplitDemo      : flowDemoStep3
2020-11-21 14:39:23.336  INFO 18264 --- [cTaskExecutor-2] o.s.batch.core.step.AbstractStep         : Step: [splitDemoStep3] executed in 50ms
2020-11-21 14:39:23.336  INFO 18264 --- [cTaskExecutor-1] o.s.batch.core.step.AbstractStep         : Step: [splitDemoStep2] executed in 61ms
2020-11-21 14:39:23.382  INFO 18264 --- [cTaskExecutor-1] o.s.batch.core.job.SimpleStepHandler     : Executing step: [splitDemoStep1]
2020-11-21 14:39:23.410  INFO 18264 --- [cTaskExecutor-1] org.springbatch.splitdemo.SplitDemo      : flowDemoStep1
2020-11-21 14:39:23.423  INFO 18264 --- [cTaskExecutor-1] o.s.batch.core.step.AbstractStep         : Step: [splitDemoStep1] executed in 41ms
2020-11-21 14:39:23.473  INFO 18264 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=splitDemoJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 318ms
```



#### 8.决策器的使用

最原始的方式是根据on后面的条件来决定后续执行哪个step，如果on后面的条件比较复杂，则on这种方式可能就满足不了了。所以采用决策器的方式来处理这种比较复杂的条件。

接口：JobExecutionDecider

```
jobBuilderFactory.get("jobDemoJob")
				.start(step1()) //开始执行step1
				.on("COMPLIETED").to(step2()) //step1满足条件 COMPLIETED(成功结束后) 执行 step2
				.from(step2()).on("COMPLIETED").to(step3()) // step2满足条件 COMPLIETED(成功结束后) 执行 step3 fail() stopAndRestart()
				.from(step3()).end()  //结束 end
				.build();
```

```java
@Configuration
@EnableBatchProcessing
@Slf4j
public class DeciderDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	/*
	 * 创建step
	 */
	@Bean
	public Step deciderDemoStep1(){
		return stepBuilderFactory.get("deciderDemoStep1").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("deciderDemoStep1");
				return RepeatStatus.FINISHED;
			}
			
		}).build();
	}
	@Bean
	public Step deciderDemoStep2(){
		return stepBuilderFactory.get("deciderDemoStep2").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("even");
				return RepeatStatus.FINISHED;
			}
			
		}).build();
	}
	@Bean
	public Step deciderDemoStep3(){
		return stepBuilderFactory.get("deciderDemoStep3").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("odd");
				return RepeatStatus.FINISHED;
			}
			
		}).build();
	}
	//创建决策器
	@Bean
	public JobExecutionDecider myDecider(){
		return new MyDecider();
	}
	//创建任务
	@Bean
	public Job deciderDemoJob(){
		/*
		 * 整个流程为，开始count++后，值为奇数，则执行step3，step3执行完后，
		 * 再次执行决策器，现在count++后，值为偶数，则执行step2，step2执行完成后，就end结束
		 */
		return jobBuilderFactory.get("deciderDemoJob")
				.start(deciderDemoStep1())
				.next(myDecider()) //执行决策器
				.from(myDecider()).on("even").to(deciderDemoStep2()) //如果决策器返回偶数，则执行step2
				.from(myDecider()).on("odd").to(deciderDemoStep3())  //如果决策器返回奇数，则执行step3
				.from(deciderDemoStep3()).on("*").to(myDecider())    //step3无论返回什么，都再执行决策器
				.end()
				.build();
	}
}
```

执行结果：

```
2020-11-21 17:14:47.529  INFO 1648 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 17:14:47.685  INFO 1648 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=deciderDemoJob]] launched with the following parameters: [{}]
2020-11-21 17:14:47.768  INFO 1648 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [deciderDemoStep1]
2020-11-21 17:14:47.799  INFO 1648 --- [           main] org.springbatch.deciderdemo.DeciderDemo  : deciderDemoStep1
2020-11-21 17:14:47.816  INFO 1648 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [deciderDemoStep1] executed in 47ms
2020-11-21 17:14:47.875  INFO 1648 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [deciderDemoStep3]
2020-11-21 17:14:47.886  INFO 1648 --- [           main] org.springbatch.deciderdemo.DeciderDemo  : odd
2020-11-21 17:14:47.894  INFO 1648 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [deciderDemoStep3] executed in 19ms
2020-11-21 17:14:47.951  INFO 1648 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [deciderDemoStep2]
2020-11-21 17:14:47.963  INFO 1648 --- [           main] org.springbatch.deciderdemo.DeciderDemo  : even
2020-11-21 17:14:47.987  INFO 1648 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [deciderDemoStep2] executed in 36ms
2020-11-21 17:14:48.009  INFO 1648 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [FlowJob: [name=deciderDemoJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 282ms
```

决策器执行过程源码剖析：未完待续................

```java
//start:
public SimpleJobBuilder start(Step step) {
    return new SimpleJobBuilder(this).start(step);
}
public SimpleJobBuilder(JobBuilderHelper<?> parent) {
    super(parent);
}

private final CommonJobProperties properties;

protected JobBuilderHelper(JobBuilderHelper<?> parent) {
    this.properties = new CommonJobProperties(parent.properties);
}
//steps中添加了step
public SimpleJobBuilder start(Step step) {
    if (steps.isEmpty()) {
        steps.add(step);
    }
    else {
        steps.set(0, step);
    }
    return this;
}

//next:
//此时 steps值只有一个
public JobFlowBuilder next(JobExecutionDecider decider) {
    for (Step step : steps) {
        if (builder == null) {
            builder = new JobFlowBuilder(new FlowJobBuilder(this), step);//创建JobFlowBuilder对象
        }
        else {
            builder.next(step);
        }
    }
    if (builder == null) {
        builder = new JobFlowBuilder(new FlowJobBuilder(this), decider);
    }
    else {
        builder.next(decider);//执行的方法在后面
    }
    return builder;
}

public UnterminatedFlowBuilder<Q> next(JobExecutionDecider decider) {
    doNext(decider);
    return new UnterminatedFlowBuilder<>(this);
}

private void doNext(Object input) {
    if (this.currentState == null) {//此时 currentState != null
        doStart(input);
    }
    State next = createState(input);
    addTransition("COMPLETED", next);
    addTransition("*", failedState);
    this.currentState = next;
}

private State createState(Object input) {
    State result;
    if (input instanceof Step) {
        if (!states.containsKey(input)) {
            Step step = (Step) input;
            states.put(input, new StepState(prefix + step.getName(), step));
        }
        result = states.get(input);
    }
    else if (input instanceof JobExecutionDecider) {
        if (!states.containsKey(input)) {
            states.put(input, new DecisionState((JobExecutionDecider) input, prefix + "decision"
                                                + (decisionCounter++)));//创建了DecisionState对象
        }
        result = states.get(input);
    }
    else if (input instanceof Flow) {
        if (!states.containsKey(input)) {
            states.put(input, new FlowState((Flow) input, prefix + ((Flow) input).getName()));
        }
        result = states.get(input);
    }
    else {
        throw new FlowBuilderException("No state can be created for: " + input);
    }
    dirty = true;
    return result;
}
public DecisionState(JobExecutionDecider decider, String name) {
    super(name);
    this.decider = decider;
}
@Override
public FlowExecutionStatus handle(FlowExecutor executor) throws Exception { //重写了父类State的handle方法
    return decider.decide(executor.getJobExecution(), executor.getStepExecution());
}

//from:
public UnterminatedFlowBuilder<Q> from(JobExecutionDecider decider) {
    doFrom(decider);
    return new UnterminatedFlowBuilder<>(this);//创建UnterminatedFlowBuilder对象，this指FlowBuilder对象
}
private void doFrom(Object input) {
    if (currentState == null) {
        doStart(input);
    }
    State state = createState(input);
    tos.put(currentState.getName(), currentState);
    this.currentState = state;
}

public UnterminatedFlowBuilder(FlowBuilder<Q> parent) {
    this.parent = parent;
}

//on：
public TransitionBuilder<Q> on(String pattern) {
    return new TransitionBuilder<>(parent, pattern);
}

public TransitionBuilder(FlowBuilder<Q> parent, String pattern) {
    this.parent = parent;
    this.pattern = pattern;
}
//to：
public FlowBuilder<Q> to(Step step) {
    State next = parent.createState(step);
    parent.addTransition(pattern, next);
    parent.currentState = next;
    return parent;//返回 FlowBuilder对象
}
//build：
public Q build() {
    @SuppressWarnings("unchecked")
    Q result = (Q) flow();
    return result;
}
protected Flow flow() {
    if (!dirty) {
        // optimization in case this method is called consecutively
        return flow;
    }
    flow = new SimpleFlow(name);
    // optimization for flows that only have one state that itself is a flow:
    if (currentState instanceof FlowState && states.size() == 1) {
        return ((FlowState) currentState).getFlows().iterator().next();
    }
    addDanglingEndStates();
    flow.setStateTransitions(transitions);
    flow.setStateTransitionComparator(new DefaultStateTransitionComparator());
    dirty = false;
    return flow;
}

private void addDanglingEndStates() {
    Set<String> froms = new HashSet<>();
    for (StateTransition transition : transitions) {
        froms.add(transition.getState().getName());
    }
    if (tos.isEmpty() && currentState != null) {
        tos.put(currentState.getName(), currentState);
    }
    Map<String, State> copy = new HashMap<>(tos);
    // Find all the states that are really end states but not explicitly declared as such
    for (String to : copy.keySet()) {
        if (!froms.contains(to)) {
            currentState = copy.get(to);
            if (!currentState.isEndState()) {
                addTransition("COMPLETED", completedState);
                addTransition("*", failedState);
            }
        }
    }
    copy = new HashMap<>(tos);
    // Then find the states that do not have a default transition
    for (String from : copy.keySet()) {
        currentState = copy.get(from);
        if (!currentState.isEndState()) {
            if (!hasFail(from)) {
                addTransition("*", failedState);
            }
            if (!hasCompleted(from)) {
                addTransition("*", completedState);
            }
        }
    }
}
```



#### 9.Job的嵌套

一个job可以嵌套在另一个job中，被嵌套的job称为子job，子job不能单独执行，需要由父job来启动。

案例：创建三个job，两个作为子job，另一个作为父job

子job一：

```java
@Configuration
@Slf4j
public class ChildJobTwo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step childJobTwoStep1(){
		return stepBuilderFactory.get("childJobTwoStep1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("childJobTwoStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Step childJobTwoStep2(){
		return stepBuilderFactory.get("childJobTwoStep2").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("childJobTwoStep2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	@Bean
	public Job childJob2(){
		return jobBuilderFactory.get("childJobTwo")
				.start(childJobTwoStep1())
				.next(childJobTwoStep2())
				.build();
	}
}
```

子job二：

```java
@Configuration
@Slf4j
public class ChildJobOne {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step childJobOneStep1(){
		return stepBuilderFactory.get("childJobOneStep1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				log.info("childJobOneStep1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Job childJob1(){
		return jobBuilderFactory.get("childJobOne")
				.start(childJobOneStep1())
				.build();
	}
}
```

父job：

```java
@Configuration
@Slf4j
public class NestedDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private Job childJob2;
	
	@Autowired
	private Job childJob1;
	
	@Autowired
	private JobLauncher jobLauncher;
	
	//启动父job需要在配置文件中添加 spring.batch.job.names=parentJob
	@Bean
	public Job parentJob(JobRepository jobRepository,PlatformTransactionManager transactionManager){
		return jobBuilderFactory.get("parentJob")
				.start(childJob1(jobRepository,transactionManager))
				.next(childJob2(jobRepository,transactionManager))
				.build();
	}

	//返回的是Job类型的Step,特殊的Step
	private Step childJob2(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		/*
		 * 需要使用 JobStepBuilder
		 */
		return new JobStepBuilder(new StepBuilder("childJobTwo"))
				.job(childJob2)
				.launcher(jobLauncher)//启动父Job来启动子job
				.repository(jobRepository)
				.transactionManager(transactionManager)
				.build();
	}

	private Step childJob1(JobRepository jobRepository,PlatformTransactionManager transactionManager) {
		
		return new JobStepBuilder(new StepBuilder("childJobOne"))
				.job(childJob1)
				.launcher(jobLauncher)//启动父Job来启动子job
				.repository(jobRepository)
				.transactionManager(transactionManager)
				.build();
	}
}
```

配置文件：

```properties
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.116.129:3306/springbatch
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.schema=classpath:/org/springframework/batch/core/schema-mysql.sql
spring.batch.initialize-schema=always
spring.batch.job.names=parentJob
```



执行结果：

```
2020-11-21 18:12:51.931  INFO 1472 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 18:12:52.069  INFO 1472 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=parentJob]] launched with the following parameters: [{}]
2020-11-21 18:12:52.139  INFO 1472 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [childJobOne]
2020-11-21 18:12:52.189  INFO 1472 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=childJobOne]] launched with the following parameters: [{}]
2020-11-21 18:12:52.220  INFO 1472 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [childJobOneStep1]
2020-11-21 18:12:52.237  INFO 1472 --- [           main] org.springbatch.nestedjob.ChildJobOne    : childJobOneStep1
2020-11-21 18:12:52.254  INFO 1472 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [childJobOneStep1] executed in 34ms
2020-11-21 18:12:52.272  INFO 1472 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=childJobOne]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 73ms
2020-11-21 18:12:52.315  INFO 1472 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [childJobOne] executed in 176ms
2020-11-21 18:12:52.342  INFO 1472 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [childJobTwo]
2020-11-21 18:12:52.369  INFO 1472 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=childJobTwo]] launched with the following parameters: [{}]
2020-11-21 18:12:52.397  INFO 1472 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [childJobTwoStep1]
2020-11-21 18:12:52.410  INFO 1472 --- [           main] org.springbatch.nestedjob.ChildJobTwo    : childJobTwoStep1
2020-11-21 18:12:52.420  INFO 1472 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [childJobTwoStep1] executed in 23ms
2020-11-21 18:12:52.448  INFO 1472 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [childJobTwoStep2]
2020-11-21 18:12:52.460  INFO 1472 --- [           main] org.springbatch.nestedjob.ChildJobTwo    : childJobTwoStep2
2020-11-21 18:12:52.468  INFO 1472 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [childJobTwoStep2] executed in 19ms
2020-11-21 18:12:52.483  INFO 1472 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=childJobTwo]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 108ms
2020-11-21 18:12:52.487  INFO 1472 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [childJobTwo] executed in 145ms
2020-11-21 18:12:52.504  INFO 1472 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=parentJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 400ms
```



#### 10.监听器的使用

用来监听批处理作业的执行情况。创建监听可以通过实现接口或使用注解。

JobExecutionListener(before,after)

StepExecutionListener(before,after)

ChunkListener(before,after,error)

ItemReadListener,ItemProcessListener,ItemWriterListener(before,after,error)



job监听器：

```java
@Slf4j
public class MyJobListener implements JobExecutionListener{
	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info(jobExecution.getJobInstance().getJobName()+" before job ....");
	}
	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info(jobExecution.getJobInstance().getJobName()+" after job ....");
	}
}
```

chunk监听器：

```java
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
```



```java
@Configuration
@Slf4j
public class ListenerDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Bean
	public Job listenerJob(){
		log.debug("listenerJob going into..........");
		return jobBuilderFactory.get("listenerJob")
				.start(listenerStep1())
				.listener(new MyJobListener())
				.build();
	}
	@Bean
	public Step listenerStep1() {
		log.debug("listenerStep1 going into..........");
		return stepBuilderFactory.get("step11")
				.<String,String>chunk(2) //读，写数据，每读完2个数据后进行数据输出
				.faultTolerant() //容错
				.listener(new MyChunkListener())
				.reader(listenerRead())
				.writer(listenerWriter())
				.build();
	}
	//写数据
	@Bean
	public ItemWriter<String> listenerWriter() {
		return new ItemWriter<String>(){
			@Override
			public void write(List<? extends String> items) throws Exception {
				for(String item : items){
					log.info(item);
				}
			}
		};
	}
	//读数据
	@Bean
	public ItemReader<String> listenerRead() {
		return new ListItemReader<>(Arrays.asList("java","spring","mybatis"));
	}
}
```



执行结果：jobListener是在一个job的前后监听，job执行前监听一次和job执行完成后监听一次。chunkListener是在每一次chunk的时候执行监听，如下结果，chunk设为2，则在java,spring前后各监听一次，在mybatis前后各监听一次。

```
2020-11-21 22:32:31.957  INFO 5376 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 22:32:32.176  INFO 5376 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=listenerJob]] launched with the following parameters: [{}]
2020-11-21 22:32:32.212  INFO 5376 --- [           main] org.springbatch.listener.MyJobListener   : listenerJob before job ....
2020-11-21 22:32:32.265  INFO 5376 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [step11]
2020-11-21 22:32:32.284  INFO 5376 --- [           main] o.springbatch.listener.MyChunkListener   : before chunk ...step11
2020-11-21 22:32:32.293  INFO 5376 --- [           main] org.springbatch.listener.ListenerDemo    : java
2020-11-21 22:32:32.293  INFO 5376 --- [           main] org.springbatch.listener.ListenerDemo    : spring
2020-11-21 22:32:32.300  INFO 5376 --- [           main] o.springbatch.listener.MyChunkListener   : after chunk ...step11
2020-11-21 22:32:32.301  INFO 5376 --- [           main] o.springbatch.listener.MyChunkListener   : before chunk ...step11
2020-11-21 22:32:32.302  INFO 5376 --- [           main] org.springbatch.listener.ListenerDemo    : mybatis
2020-11-21 22:32:32.307  INFO 5376 --- [           main] o.springbatch.listener.MyChunkListener   : after chunk ...step11
2020-11-21 22:32:32.310  INFO 5376 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [step11] executed in 45ms
2020-11-21 22:32:32.334  INFO 5376 --- [           main] org.springbatch.listener.MyJobListener   : listenerJob after job ....
2020-11-21 22:32:32.342  INFO 5376 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=listenerJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 133ms
```



#### 11.job参数

任务在运行时可以以key=value形式传递参数

```java
@Configuration
@Slf4j
public class ParametersDemo implements StepExecutionListener{
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private Map<String,JobParameter> paramters;
	
	@Bean
	public Job paramterJob(){
		return jobBuilderFactory.get("paramterJob")
				.start(paramterStep())
				.build();
	}
	//job执行的是step,job使用的数据是在step中使用,只需给step传递数据
	/**
	 * 如何给step传递参数
	 * 使用监听，使用step级别的监听来传递数据
	 * @return
	 */
	@Bean
	public Step paramterStep() {
		/*
		 * 结论：step监听是整个step的监听，即使是chunk方式，也只会有一次before和after监听
		 * 输出结果如下：
		 * step开始执行..........
		 * java
		 * spring
		 * mybatis
		 * step执行完成..........
		 */
		return /*((SimpleStepBuilder<String, String>) stepBuilderFactory.get("paramterStep")
				.<String,String>chunk(2) //读，写数据，每读完2个数据后进行数据输出
				.faultTolerant() //容错
				.listener(this))
				.reader(listenerRead())
				.writer(listenerWriter())
				.build();*/
				stepBuilderFactory.get("paramterStep")
				.listener(this)
				.tasklet(new Tasklet(){

					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
							throws Exception {
						//输出接收到的参数
						log.info("接收到的参数:"+paramters.get("info"));//springbatch
						return RepeatStatus.FINISHED;
					}
					
				}).build();
	}
	
	//写数据
	@Bean
	public ItemWriter<String> listenerWriter() {
		return new ItemWriter<String>(){
			@Override
			public void write(List<? extends String> items) throws Exception {
				for(String item : items){
					log.info(item);
				}
			}
		};
	}

	//读数据
	@Bean
	public ItemReader<String> listenerRead() {
		return new ListItemReader<>(Arrays.asList("java","spring","mybatis"));
	}
	
	//在before处获取参数
	@Override
	public void beforeStep(StepExecution stepExecution) {//通过StepExecution来获取参数
		log.error("step开始执行..........");
		paramters = stepExecution.getJobParameters().getParameters();
		
	}
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("step执行完成..........");
		return null;
	}
}
```

执行的时候设置参数：info=springbatch

![image-20201121230656089](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201121230656089.png)

执行结果：

```
2020-11-21 23:04:43.604  INFO 6908 --- [           main] o.s.parameterjob.SpringbatchApplication  : Started SpringbatchApplication in 3.177 seconds (JVM running for 3.733)
2020-11-21 23:04:43.606  INFO 6908 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: [info=springbatch]
2020-11-21 23:04:43.763  INFO 6908 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=paramterJob]] launched with the following parameters: [{info=springbatch}]
2020-11-21 23:04:43.892  INFO 6908 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [paramterStep]
2020-11-21 23:04:43.902 ERROR 6908 --- [           main] o.s.parameterjob.ParametersDemo          : step开始执行..........
2020-11-21 23:04:43.925  INFO 6908 --- [           main] o.s.parameterjob.ParametersDemo          : 接收到的参数springbatch
2020-11-21 23:04:43.960  INFO 6908 --- [           main] o.s.parameterjob.ParametersDemo          : step执行完成..........
2020-11-21 23:04:43.970  INFO 6908 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [paramterStep] executed in 78ms
2020-11-21 23:04:43.990  INFO 6908 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=paramterJob]] completed with the following parameters: [{info=springbatch}] and the following status: [COMPLETED] in 175ms
```



#### 12.ItemReader

```java
@Configuration
@Slf4j
public class ItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//创建job
	@Bean
	public Job ItemReaderDemoJob(){
		return jobBuilderFactory.get("ItemReaderDemoJob")
				.start(itemReaderDemoStep())
				.build();
	}

	//创建step
	@Bean
	public Step itemReaderDemoStep() {
		return stepBuilderFactory.get("itemReaderDemoStep")
				.<String,String>chunk(2)
				.reader(itemReaderDemoRead())
				.writer(list->{
					for(String item : list){
						log.error(item+".............");
					}
				})
				.build();
	}

	//自定义reader对象
	@Bean
	public MyReader itemReaderDemoRead() {
		List<String> data = Arrays.asList("cat","dog","pig","duck");
		return new MyReader(data);
	}

}
```

```java
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
```

执行结果：

```
2020-11-21 23:23:37.431  INFO 11812 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 23:23:37.609  INFO 11812 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=ItemReaderDemoJob]] launched with the following parameters: [{}]
2020-11-21 23:23:37.691  INFO 11812 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [itemReaderDemoStep]
2020-11-21 23:23:37.728 ERROR 11812 --- [           main] o.springbatch.itemreader.ItemReaderDemo  : cat.............
2020-11-21 23:23:37.728 ERROR 11812 --- [           main] o.springbatch.itemreader.ItemReaderDemo  : dog.............
2020-11-21 23:23:37.748 ERROR 11812 --- [           main] o.springbatch.itemreader.ItemReaderDemo  : pig.............
2020-11-21 23:23:37.748 ERROR 11812 --- [           main] o.springbatch.itemreader.ItemReaderDemo  : duck.............
2020-11-21 23:23:37.768  INFO 11812 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [itemReaderDemoStep] executed in 77ms
2020-11-21 23:23:37.789  INFO 11812 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=ItemReaderDemoJob]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 141ms
```



##### ①.从数据库中读取数据

JdbcPagingItemReader：从数据库中分页读取数据

JdbcCursorItemReader：基于游标的技术的JDBC实现

HibernateCursorItemReader：游标技术的Hibernate实现

StoredProcedureItemReader：使用存储过程来获取游标数据

JpaPagingItemReader：给定一个JPQL语句，可以在各行之间进行分页，从而可以读取大型数据集而不会耗尽内存

...

基于JdbcPagingItemReader的实现

实体类：

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private int id;
	private String username;
	private String password;
	private int age;
}
```

```java
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
```

```java
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
```

执行结果：

```
2020-11-21 23:54:47.738  INFO 9372 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-21 23:54:47.889  INFO 9372 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=itemReaderDbJob3]] launched with the following parameters: [{}]
2020-11-21 23:54:47.968  INFO 9372 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [itemReaderDbStep3]
User(id=1, username=ALLEN, password=11, age=12)
User(id=2, username=WESTBROOK, password=22, age=18)
User(id=3, username=JAMES, password=33, age=20)
User(id=4, username=CURY, password=44, age=19)
User(id=5, username=DAVIS, password=55, age=21)
2020-11-21 23:54:48.159  INFO 9372 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [itemReaderDbStep3] executed in 190ms
2020-11-21 23:54:48.193  INFO 9372 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=itemReaderDbJob3]] completed with the following parameters: [{}] and the following status: [COMPLETED] in 263ms
```



##### ②.从普通文件中读取数据

FlatFileItemReader



实体类：

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	private Long id;
	private String firstname;
	private String lastname;
	private String birthday;
}
```

```java
@Configuration
public class FileItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ItemWriter<Customer> flatFileWriter;
	
	//创建job
	@Bean
	public Job fileItemReaderDemoJob(){
		return jobBuilderFactory.get("fileItemReaderDemoJob")
				.start(fileItemReaderDemoStep())
				.build();
	}
	//创建step
	@Bean
	public Step fileItemReaderDemoStep() {
		return stepBuilderFactory.get("fileItemReaderDemoStep")
				.<Customer,Customer>chunk(5)
				.reader(flatFileReader())
				.writer(flatFileWriter)
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> flatFileReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		reader.setResource(new ClassPathResource("customer.txt"));
		reader.setLinesToSkip(1);//跳过第一行
		//数据解析
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"id","firstname","lastname","birthday"});
		//把解析出的数据映射为customer对象
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstname(fieldSet.readString("firstname"));
				customer.setLastname(fieldSet.readString("lastname"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);
		return reader;
	}
}
```

```java
@Component
public class FlatFileWriter implements ItemWriter<Customer>{
	@Override
	public void write(List<? extends Customer> items) throws Exception {
		for(Customer customer : items){
			System.out.println(customer);
		}	
	}
}
```

执行结果：

```
Customer(id=1, firstname=Stone, lastname=Barrett, birthday=1964-10-19 14:11:03)
Customer(id=2, firstname=Stone, lastname=Barrett, birthday=1964-10-19 14:11:03)
Customer(id=3, firstname=Stone, lastname=Barrett, birthday=1964-10-19 14:11:03)
Customer(id=4, firstname=Stone, lastname=Barrett, birthday=1964-10-19 14:11:03)
```



##### ③.从xml文件中读取数据

StaxEventItemReader

...

暂时有问题，未完待续.......

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	private Long id;
	private String firstName;
	private String lastName;
	private String birthday;
}
```

```java
@Configuration
public class XmlItemReaderDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("xmlFileWriter")
	private ItemWriter<Customer> xmlFileWriter;
	
	@Bean
	public Job xmlItemReaderDemoJob(){
		return jobBuilderFactory.get("xmlItemReaderDemoJob")
				.start(xmlItemReaderDemoStep())
				.build();
	}
	@Bean
	public Step xmlItemReaderDemoStep() {
		return stepBuilderFactory.get("xmlItemReaderDemoStep")
				.<Customer,Customer>chunk(2)
				.reader(xmlFileReader())
				.writer(xmlFileWriter)
				.build();
	}
	@Bean
	@StepScope
	public ItemReader<Customer> xmlFileReader() {
		StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
		System.out.println("<<<<<<<<<<<  11111111111111");
		reader.setResource(new ClassPathResource("customer.xml"));
		//指定需要处理的根标签
		reader.setFragmentRootElementName("customer");
		//把xml转成对象
		XStreamMarshaller unmarshaller = new XStreamMarshaller();
		System.out.println("<<<<<<<<<<  2222222222222222");
		Map<String,Class> map = new HashMap<>();
		map.put("customer", Customer.class);
		System.out.println("<<<<<<<<<<<  3333333333333333");
		unmarshaller.setAliases(map);
		reader.setUnmarshaller(unmarshaller);
		System.out.println(reader);
		return reader;
	}
	
}
```

```java
@Component("xmlFileWriter")
public class XmlFileWriter implements ItemWriter<Customer>{
	@Override
	public void write(List<? extends Customer> items) throws Exception {
		System.out.println(items);
		for(Customer customer : items){
			System.out.println(customer);
		}
	}
}
```





##### ④.ItemReader异常处理及重启

![image-20201122221810863](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201122221810863.png)

```java
public interface ItemStream {

    /*
     *open方法是在step方法之前执行的
     */
	void open(ExecutionContext executionContext) throws ItemStreamException;

    /*
     *update方法是在chunk处理完一批数据后进行执行，每一批成功读取了多少数据后执行update
     */
	void update(ExecutionContext executionContext) throws ItemStreamException;
  	/*
     *整个step执行完后执行
     */
	void close() throws ItemStreamException;
}

@Component
public class RestartReader implements ItemStreamReader<Customer>{
	private FlatFileItemReader<Customer> customerFlatFileItemReader = new FlatFileItemReader<>();
	private Long curLine = 0L;//记录当前读取的行数
	private boolean restart = false;
	private ExecutionContext executionContext;
	
	public RestartReader(){
		customerFlatFileItemReader.setResource(new ClassPathResource("restart.txt"));
		//customerFlatFileItemReader.setLinesToSkip(1);//跳过第一行
		//如何解析数据
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		//制定四个表头字段
		tokenizer.setNames(new String[]{"id","firstname","lastname","birthday"});
		//把解析出的数据映射为customer对象
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstname(fieldSet.readString("firstname"));
				customer.setLastname(fieldSet.readString("lastname"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
		mapper.afterPropertiesSet();
		customerFlatFileItemReader.setLineMapper(mapper);
	}
	
	//读取数据
	@Override
	public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Customer customer = null;
		this.curLine++;
		if(restart){
			customerFlatFileItemReader.setLinesToSkip(this.curLine.intValue()-1);
			restart = false;
			System.out.println("Start reading from line: "+this.curLine);
		}
		customerFlatFileItemReader.open(this.executionContext);
		customer = customerFlatFileItemReader.read();
		if(customer != null && customer.getFirstname().equals("WrongName")){
			throw new RuntimeException("Something wrong. Customer id: "+customer.getId());
		}
		return customer;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.executionContext = executionContext;
		if(executionContext.containsKey("curLine")){
			this.curLine = executionContext.getLong("curLine");
			this.restart = true;
		}else{
			this.curLine = 0L;
			executionContext.put("curLine", this.curLine);
			System.out.println("Start reading from line: "+this.curLine+1);
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.put("curLine", this.curLine);
		System.out.println("currentLine: "+this.curLine);
	}

	@Override
	public void close() throws ItemStreamException {
		
	}

}


```





#### 13.ItemWriter

ItemReader是一个数据一个数据的读，而ItemWriter是一批一批的输出



```java
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
```



```java
@Configuration
public class ItemWriterDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("myWriter")
	private ItemWriter<? super String> myWriter;
	
	//创建job
	@Bean
	public Job itemWriterDemoJob2(){
		return jobBuilderFactory.get("itemWriterDemoJob2")
				.start(itemWriterDemoStep2())
				.build();
	}

	//创建step
	@Bean
	public Step itemWriterDemoStep2() {
		return stepBuilderFactory.get("itemWriterDemoStep2")
				.<String,String>chunk(5)
				.reader(myRead())
				.writer(myWriter)
				.build();
	}

	@Bean
	public ItemReader<String> myRead() {
		List<String> items = new ArrayList<>();
		for(int i=0;i<=50;i++){
			items.add("java"+i);
		}
		return new ListItemReader<>(items);
	}
}
```

执行结果：

```
2020-11-22 22:33:14.362  INFO 14280 --- [           main] o.s.b.a.b.JobLauncherApplicationRunner   : Running default command line with: []
2020-11-22 22:33:14.490  INFO 14280 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=itemWriterDemoJob2]] launched with the following parameters: [{}]
2020-11-22 22:33:14.571  INFO 14280 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [itemWriterDemoStep2]
5
java0
java1
java2
java3
java4
5
java5
java6
java7
java8
java9
5
java10
java11
java12
java13
java14
5
java15
java16
java17
java18
java19
5
...
```



##### ①.数据输出到数据库

Neo4jItemWriter

MongoItemWriter

RepositoryItemWriter

HibernateItemWriter

JdbcBatchItemWriter

JpaItemWriter

GemfileItemWriter

...



```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	private Long id;
	private String firstName;
	private String lastName;
	private String birthday;
}
```

```java
@Configuration
public class FlatFileReaderConfig {
	
	@Bean
	public FlatFileItemReader<Customer> flatFileReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		reader.setResource(new ClassPathResource("customer.txt"));
		reader.setLinesToSkip(1);//跳过第一行
		//数据解析
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"id","firstname","lastname","birthday"});
		//把解析出的数据映射为customer对象
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstName(fieldSet.readString("firstname"));
				customer.setLastName(fieldSet.readString("lastname"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);
		return reader;
	}

}
```

```java
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
```

```java
@Configuration
public class ItemWriterDbDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ItemReader<Customer> flatFileReader;

	@Autowired
	private ItemWriter<Customer> itemWriterDb;
	
	@Bean
	public Job itemWriterDbDemoJob(){
		return jobBuilderFactory.get("itemWriterDbDemoJob")
				.start(itemWriterDbDemoStep())
				.build();
	}

	@Bean
	public Step itemWriterDbDemoStep() {
		
		return stepBuilderFactory.get("itemWriterDbDemoStep")
				.<Customer,Customer>chunk(10)
				.reader(flatFileReader)
				.writer(itemWriterDb)
				.build();
	}

}
```

配置文件：

```properties
id,firstname.lastname,birthday
1,Stone,Barrett,1964-10-19 14:11:03
2,Stone,Barrett,1964-10-19 14:11:03
3,Stone,Barrett,1964-10-19 14:11:03
4,Stone,Barrett,1964-10-19 14:11:03
5,Stone,Barrett,1964-10-19 14:11:03
6,Stone,Barrett,1964-10-19 14:11:03
7,Stone,Barrett,1964-10-19 14:11:03
```

最终将所有数据成功插入数据库。



#### 14.ItemProcessor

ItemProcessor<I,O>用于处理业务逻辑，验证，过滤等功能

CompositeItemProcessor

案例：从普通文件中读取数据，并将名称改为大写，最终写入数据库

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	private Long id;
	private String firstName;
	private String lastName;
	private String birthday;
}
```

```java
/**
 * 将firstName全部改为大写
 * @author Qh
 */
@Component
public class FirstNameUpperProcessor implements ItemProcessor<Customer,Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		item.setFirstName(item.getFirstName().toUpperCase());
		return item;
	}

}
```

```java
@Configuration
public class FlatFileReaderConfig {
	
	@Bean
	public FlatFileItemReader<Customer> flatFileReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		reader.setResource(new ClassPathResource("customer.txt"));
		reader.setLinesToSkip(1);//跳过第一行
		//数据解析
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[]{"id","firstname","lastname","birthday"});
		//把解析出的数据映射为customer对象
		DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
		mapper.setLineTokenizer(tokenizer);
		mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstName(fieldSet.readString("firstname"));
				customer.setLastName(fieldSet.readString("lastname"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
		mapper.afterPropertiesSet();
		reader.setLineMapper(mapper);
		return reader;
	}

}
```

```java
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
```

```java
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
```

```java
@Configuration
public class ItemWriterDbDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ItemReader<Customer> flatFileReader;

	@Autowired
	private ItemWriter<Customer> itemWriterDb;

	@Autowired
	private ItemProcessor<Customer,Customer> firstNameUpperProcessor;
	
	@Autowired
	private ItemProcessor<Customer,Customer> idFilterProcessor;
	
	@Bean
	public Job itemWriterDbDemoJob2(){
		return jobBuilderFactory.get("itemWriterDbDemoJob2")
				.start(itemWriterDbDemoStep2())
				.build();
	}

	@Bean
	public Step itemWriterDbDemoStep2() {
		return stepBuilderFactory.get("itemWriterDbDemoStep2")
				.<Customer,Customer>chunk(10)
				.reader(flatFileReader)
				//.processor(firstNameUpperProcessor) //单个数据处理的方式
				.processor(process())
				.writer(itemWriterDb)
				.build();
	}
	
	//有多种处理数据的方法,将多种处理方式组合在一起
	@Bean
	public CompositeItemProcessor<Customer,Customer> process(){
		CompositeItemProcessor<Customer,Customer> process = new CompositeItemProcessor<Customer,Customer>();
		List<ItemProcessor<Customer,Customer>> delegates = new ArrayList<>();
		delegates.add(firstNameUpperProcessor);
		delegates.add(idFilterProcessor);
		process.setDelegates(delegates);
		return process;
	}
}
```

执行结果：

![image-20201122234558539](C:\Users\Qh\Desktop\springbatch\SpringBatch笔记.assets\image-20201122234558539.png)



#### 15.错误处理

默认情况下当任务出现异常时，SpringBatch会结束任务，当使用相同参数重启任务时，SpringBatch会去执行未执行的剩余任务。



```java
@Configuration
public class ErrorDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job errorsDemoJob(){
		return jobBuilderFactory.get("errorsDemoJob")
				.start(errorStep1())
				.next(errorStep2())
				.build();
	}
	
	@Bean
	public Step errorStep1(){
		return stepBuilderFactory.get("errorStep1")
				.tasklet(errorHandling())
				.build();
	}
	
	@Bean
	public Step errorStep2(){
		return stepBuilderFactory.get("errorStep2")
				.tasklet(errorHandling())
				.build();
	}
	
	@Bean
	public Tasklet errorHandling() {
		return new Tasklet(){

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				//获取step执行的上下文
				Map<String,Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
				if(stepExecutionContext.containsKey("qinhao")){
					System.out.println("The second run will success");
					return RepeatStatus.FINISHED;
				}else{
					System.out.println("The first run will fail");
					chunkContext.getStepContext().getStepExecution().getExecutionContext().put("qinhao", "test");
					throw new RuntimeException("error....");
				}
			}
			
		};
	}

}
```

第一次执行任务时，发生异常：

```
2020-11-23 00:05:53.625  INFO 15120 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [errorStep1]
The first run will fail
2020-11-23 00:05:53.660 ERROR 15120 --- [           main] o.s.batch.core.step.AbstractStep         : Encountered an error executing step errorStep1 in job errorsDemoJob

java.lang.RuntimeException: error....
```

重启springbatch后，再次执行step1,此时step1未发生异常，但step2发生了异常：

```
2020-11-23 00:07:11.670  INFO 8296 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [errorStep1]
The second run will success
2020-11-23 00:07:11.709  INFO 8296 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [errorStep1] executed in 38ms
2020-11-23 00:07:11.760  INFO 8296 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [errorStep2]
The first run will fail
2020-11-23 00:07:11.779 ERROR 8296 --- [           main] o.s.batch.core.step.AbstractStep         : Encountered an error executing step errorStep2 in job errorsDemoJob

java.lang.RuntimeException: error....
```

最后再重启springbatch,此次只执行了发生异常的step2,不会再执行step1,最后step1正常结束：

```
2020-11-23 00:08:36.847  INFO 12464 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [errorStep2]
The second run will success
2020-11-23 00:08:36.874  INFO 12464 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [errorStep2] executed in 26ms
```



#### 16.错误重试（Retry）

默认出现错误，SpringBatch会结束任务。但是如果不想结束任务可以设置错误重试。

关键部分：

```
return stepBuilderFactory.get("retryDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(retryItemProcessor)
				.writer(retryItemWriter)
				.faultTolerant() //容错
				.retry(CustomRetryException.class) //发生CustomRetryException异常，触发重试
				.retryLimit(5) //尝试重试的次数  ，超过次数就不会重试，任务就会停止。 重试的次数：reader、processor、writer 总计的异常次数。没有超过5次就会重试，超过就不会重试了，任务就会终止
				.build();
```



```java
@Configuration
public class RetryDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> retryItemWriter;
	
	@Autowired
	private ItemProcessor<String,String> retryItemProcessor;
	
	@Bean
	public Job retryDemoJob(){
		return jobBuilderFactory.get("retryDemoJob")
				.start(retryDemoStep())
				.build();
	}

	@Bean
	public Step retryDemoStep() {
		return stepBuilderFactory.get("retryDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(retryItemProcessor)
				.writer(retryItemWriter)
				.faultTolerant() //容错
				.retry(CustomRetryException.class) //发生CustomRetryException异常，触发重试
				.retryLimit(5) //尝试重试的次数  ，超过次数就不会重试，任务就会停止。 重试的次数：reader、processor、writer 总计的异常次数。没有超过5次就会重试，超过就不会重试了，任务就会终止
				.build();
	}

	@Bean
	@StepScope
	public ItemReader<String> reader() {
		List<String> items = new ArrayList<>();
		for(int i=0;i<60;i++){
			items.add(String.valueOf(i));
		}
		ListItemReader<String> reader = new ListItemReader<>(items);
		return reader;
	}

}
```

```java
@Component
public class RetryItemWriter implements ItemWriter<String>{

	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String item : items){
			System.out.println(item);
		}
	}
}
```

```java
@Component
public class RetryItemProcessor implements ItemProcessor<String, String>{

	private int attemptCount = 0;
	@Override
	public String process(String item) throws Exception {
		System.out.println("processing item "+item);
		if(item.equalsIgnoreCase("26")){
			attemptCount++;
			if(attemptCount >= 3){
				System.out.println("Retried "+attemptCount+" times success.");
				return String.valueOf(Integer.valueOf(item) * -1);
			}else{
				System.out.println("Processed the "+attemptCount+" times fail.");
				throw new CustomRetryException("Process failed. Attempt:"+attemptCount);
			}
			
		}else{
			return String.valueOf(Integer.valueOf(item) * -1);
		}
	}

}
```

```java
public class CustomRetryException extends Exception{

	private static final long serialVersionUID = -1406411434441162872L;

	public CustomRetryException() {
		super();
	}

	public CustomRetryException(String message) {
		super(message);
	}

}
```

执行结果：当数据为26时，重试了3次后成功

```
...
processing item 20
processing item 21
processing item 22
processing item 23
processing item 24
processing item 25
processing item 26
Processed the 1 times fail.
processing item 20
processing item 21
processing item 22
processing item 23
processing item 24
processing item 25
processing item 26
Processed the 2 times fail.
processing item 20
processing item 21
processing item 22
processing item 23
processing item 24
processing item 25
processing item 26
Retried 3 times success.
processing item 27
processing item 28
processing item 29
-20
-21
-22
-23
-24
-25
-26
-27
-28
-29
...
```



#### 17.错误跳过（Skip）

可以指定当前处理的数据可以跳过多少条数据。

关键代码：

```java
return stepBuilderFactory.get("skipDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipItemProcessor)
				.writer(skipItemWriter)
				.faultTolerant() //容错
				.skip(CustomRetryException.class) //指明发生当前异常，就跳过
				.skipLimit(5)
				.build();
```



```java
@Configuration
public class SkipDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> skipItemWriter;
	
	@Autowired
	private ItemProcessor<String,String> skipItemProcessor;
	
	@Bean
	public Job skipDemoJob(){
		return jobBuilderFactory.get("skipDemoJob")
				.start(skipDemoStep())
				.build();
	}

	@Bean
	public Step skipDemoStep() {
		return stepBuilderFactory.get("skipDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipItemProcessor)
				.writer(skipItemWriter)
				.faultTolerant() //容错
				.skip(CustomRetryException.class) //指明发生当前异常，就跳过
				.skipLimit(5)
				.build();
	}

	@Bean
	@StepScope
	public ItemReader<String> reader() {
		List<String> items = new ArrayList<>();
		for(int i=0;i<60;i++){
			items.add(String.valueOf(i));
		}
		ListItemReader<String> reader = new ListItemReader<>(items);
		return reader;
	}

}
```

```java
@Component
public class SkipItemWriter implements ItemWriter<String>{

	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String item : items){
			System.out.println(item);
		}
	}

}
```

```java
@Component
public class SkipItemProcessor implements ItemProcessor<String, String>{

	private int attemptCount = 0;
	@Override
	public String process(String item) throws Exception {
		System.out.println("processing item "+item);
		if(item.equalsIgnoreCase("26")){
			attemptCount++;
			if(attemptCount >= 3){
				System.out.println("Retried "+attemptCount+" times success.");
				return String.valueOf(Integer.valueOf(item) * -1);
			}else{
				System.out.println("Processed the "+attemptCount+" times fail.");
				throw new CustomRetryException("Process failed. Attempt:"+attemptCount);
			}
			
		}else{
			return String.valueOf(Integer.valueOf(item) * -1);
		}
	}

}
```

```java
public class CustomRetryException extends Exception{

	private static final long serialVersionUID = -1406411434441162872L;

	public CustomRetryException() {
		super();
	}

	public CustomRetryException(String message) {
		super(message);
	}

}
```

执行结果：正常跳过数据为26的数据

#### 18.错误跳过监听器

```java
@Configuration
public class SkipListenerDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> skipListenerItemWriter;
	
	@Autowired
	private ItemProcessor<String,String> skipListenerItemProcessor;

	@Autowired
	private SkipListener<String, String> mySkipListener;
	
	@Bean
	public Job skipListenerDemoJob(){
		return jobBuilderFactory.get("skipListenerDemoJob")
				.start(skipListenerDemoStep())
				.build();
	}

	@Bean
	public Step skipListenerDemoStep() {
		return stepBuilderFactory.get("skipListenerDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipListenerItemProcessor)
				.writer(skipListenerItemWriter)
				.faultTolerant() //容错
				.skip(CustomRetryException.class) //指明发生当前异常，就跳过
				.skipLimit(5)
				.listener(mySkipListener) //错误跳过监听器
				.build();
	}

	@Bean
	@StepScope
	public ItemReader<String> reader() {
		List<String> items = new ArrayList<>();
		for(int i=0;i<60;i++){
			items.add(String.valueOf(i));
		}
		ListItemReader<String> reader = new ListItemReader<>(items);
		return reader;
	}

}
```

```java
@Component
public class SkipListenerItemWriter implements ItemWriter<String>{

	@Override
	public void write(List<? extends String> items) throws Exception {
		for(String item : items){
			System.out.println(item);
		}
	}

}

```

```java
@Component
public class SkipListenerItemProcessor implements ItemProcessor<String, String>{

	private int attemptCount = 0;
	@Override
	public String process(String item) throws Exception {
		System.out.println("processing item "+item);
		if(item.equalsIgnoreCase("26")){
			attemptCount++;
			if(attemptCount >= 3){
				System.out.println("Retried "+attemptCount+" times success.");
				return String.valueOf(Integer.valueOf(item) * -1);
			}else{
				System.out.println("Processed the "+attemptCount+" times fail.");
				throw new CustomRetryException("Process failed. Attempt:"+attemptCount);
			}
			
		}else{
			return String.valueOf(Integer.valueOf(item) * -1);
		}
	}

}
```

```java
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
```

```java
public class CustomRetryException extends Exception{

	private static final long serialVersionUID = -1406411434441162872L;

	public CustomRetryException() {
		super();
	}

	public CustomRetryException(String message) {
		super(message);
	}

}
```

执行结果：正常监听到处理程序异常的部分

```
-20
-21
-22
-23
-24
-25
-27
-28
-29
26 occur exception org.springbatch.skiplistener.CustomRetryException: Process failed. Attempt:1
```

