import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class StaticBlockJdbcConnectionFactory {

	private static final StaticBlockJdbcConnectionFactory INSTANCE = new StaticBlockJdbcConnectionFactory();

	private static HikariDataSource ds;

	static {

		try {
			Map<String, String> env = System.getenv();

			Properties props = new Properties();
			props.setProperty("dataSourceClassName", env.get("jdbcUrl"));
			props.setProperty("dataSource.user", env.get("username"));
			props.setProperty("dataSource.password", env.get("password"));
			props.setProperty("dataSource.databaseName", env.get("database"));
			props.setProperty("dataSource.serverName", env.get("host"));
			props.setProperty("dataSource.ApplicationName", "StaticBlockJdbcConnectionFactoryDemonstration");

			HikariConfig config = new HikariConfig(props);
			config.setMinimumIdle(Integer.parseInt(env.get("minimumIdle")));
			config.setMaximumPoolSize(Integer.parseInt(env.get("maxPoolSize")));
			config.setValidationTimeout(Long.parseLong("validationTimeout"));
			config.setConnectionTimeout(Long.parseLong("connectionTimeout"));
			config.setIdleTimeout(Long.parseLong("idleTimeout"));

			ds = new HikariDataSource(config);
		} catch (Exception e) {
			// Handle exception
		}
	}

	private StaticBlockJdbcConnectionFactory() {
		super();
	}

	public static StaticBlockJdbcConnectionFactory getInstance() {
		return INSTANCE;
	}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	public void terminate() {
		if (!ds.isClosed()) ds.close();
	}

}
