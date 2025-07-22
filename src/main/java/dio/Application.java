package dio;

import dio.persistence.EmployeeDAO;
import org.flywaydb.core.Flyway;

public class Application {

	private final static String DB_URL = System.getenv("DB_URL");
	private final static String DB_USERNAME = System.getenv("DB_USERNAME");
	private final static String DB_PASSWORD = System.getenv("DB_PASSWORD");

	private final static EmployeeDAO employeeDAO = new EmployeeDAO();

	public static void main(String[] args) {
		Flyway flyway = Flyway.configure()
				.dataSource(DB_URL, DB_USERNAME, DB_PASSWORD)
				.load();

		flyway.migrate();
	}

}
