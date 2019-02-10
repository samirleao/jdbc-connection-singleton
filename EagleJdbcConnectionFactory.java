import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Eagle loading connection factory singleton. As we are sure that it
 * will be used at some point during the execution, we don't need to
 * implement as a lazy loading instance.
 * Plus as it will not be lazy loaded, we don't need to deal with
 * synchronization problems on the getInstance() method.
 * 
 * @author Samir Leão
 *
 */
public class EagleJdbcConnectionFactory {
	
	private static final EagleJdbcConnectionFactory INSTANCE = new EagleJdbcConnectionFactory();
	
	private HikariDataSource ds;
	
	private EagleJdbcConnectionFactory() {
		Map<String, String> env = System.getenv();
		
		Properties props = new Properties();
		props.setProperty("dataSourceClassName", env.get("jdbcUrl"));
		props.setProperty("dataSource.user", env.get("username"));
		props.setProperty("dataSource.password", env.get("password"));
		props.setProperty("dataSource.databaseName", env.get("database"));
		props.setProperty("dataSource.serverName", env.get("host"));
		props.setProperty("dataSource.ApplicationName", "EagleJdbcConnectionFactoryDemonstration");

		HikariConfig config = new HikariConfig(props);
		config.setMinimumIdle(Integer.parseInt(env.get("minimumIdle")));
		config.setMaximumPoolSize(Integer.parseInt(env.get("maxPoolSize")));
		config.setValidationTimeout(Long.parseLong("validationTimeout"));
		config.setConnectionTimeout(Long.parseLong("connectionTimeout"));
		config.setIdleTimeout(Long.parseLong("idleTimeout"));

		ds = new HikariDataSource(config);
	}
	
	public static EagleJdbcConnectionFactory getInstance() {
		return INSTANCE;
	}
	
	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}
	
	public void terminate() {
		if (!this.ds.isClosed()) this.ds.close();
	}
	
}
