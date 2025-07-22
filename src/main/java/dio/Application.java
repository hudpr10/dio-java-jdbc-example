package dio;

import dio.persistence.EmployeeDAO;

import dio.persistence.entity.EmployeeEntity;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


public class Application {

	private final static String DB_URL = System.getenv("DB_URL");
	private final static String DB_USERNAME = System.getenv("DB_USERNAME");
	private final static String DB_PASSWORD = System.getenv("DB_PASSWORD");

	private final static EmployeeDAO employeeDAO = new EmployeeDAO();

	public static void main(String[] args) {
		Flyway flyway = Flyway.configure().dataSource(DB_URL, DB_USERNAME, DB_PASSWORD).load();
		flyway.migrate();

		EmployeeEntity employeeInsert = new EmployeeEntity();
		employeeInsert.setName("Pedro");
		employeeInsert.setSalary(new BigDecimal("1412"));
		employeeInsert.setBirthday(OffsetDateTime.now().minusYears(2));
		employeeDAO.insert(employeeInsert);

		employeeDAO.findAll().forEach(System.out::println);

		System.out.println(employeeDAO.findById(1));

		EmployeeEntity employeeUpdate = new EmployeeEntity();
		employeeUpdate.setId(2);
		employeeUpdate.setName("Maria");
		employeeUpdate.setBirthday(OffsetDateTime.now().minusYears(32));
		employeeUpdate.setSalary(new BigDecimal("6000"));
		employeeDAO.update(employeeUpdate);

		employeeDAO.delete(1);

	}

}
