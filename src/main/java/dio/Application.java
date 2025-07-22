package dio;

import dio.persistence.EmployeeDAO;
import dio.persistence.entity.EmployeeEntity;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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

		EmployeeEntity employee = new EmployeeEntity();
		employee.setId(2);
		employee.setName("Hudson");
		employee.setSalary(new BigDecimal("1412"));
		employee.setBirthday(OffsetDateTime.now().minusYears(21).minusDays(73));

		employeeDAO.update(employee);

		// employeeDAO.delete(3);
	}

}
