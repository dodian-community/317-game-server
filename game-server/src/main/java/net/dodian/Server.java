package net.dodian;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dodian.managers.DefinitionsManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The starting point of Elvarg.
 * All necessary utilities are loaded
 * here.
 * 
 * @author Professor Oak
 */
@SpringBootApplication
@EnableJpaRepositories
public class Server implements InitializingBean, BeanFactoryAware {

	private final Initializer initializer;
	private static BeanFactory beanFactory;

	/**
	 * A logic service, used for carrying out
	 * asynchronous tasks such as file-writing.
	 */
	private static final ScheduledExecutorService logicService = createLogicService();   
	
	/**
	 * Is the server currently updating?
	 */
	private static boolean updating;

	/**
	 * The main logger.
	 */
	private static final Logger logger = Logger.getLogger("Dodian");

	public static void main(String[] params) {
		SpringApplication.run(Server.class, params);
	}

	@Autowired
	public Server(Initializer initializer) {
		this.initializer = initializer;
	}

	@Override
	public void afterPropertiesSet() {
		this.initializer.initialize();
	}

	public static ScheduledExecutorService createLogicService() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.setRejectedExecutionHandler(new CallerRunsPolicy());
		executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
		executor.setKeepAliveTime(45, TimeUnit.SECONDS);
		executor.allowCoreThreadTimeOut(true);
		return Executors.unconfigurableScheduledExecutorService(executor);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		Server.updating = updating;
	}

	public static boolean isUpdating() {
		return Server.updating;
	}

	public static void submit(Runnable t) {
		try {
			logicService.execute(t);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		Server.beanFactory = beanFactory;
	}

	public static BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public static DefinitionsManager getDefinitionsHandler() {
		return beanFactory.getBean(DefinitionsManager.class);
	}
}