package dio;

import dio.persistence.EmployeeAuditDAO;
import dio.persistence.EmployeeDAO;

import dio.persistence.EmployeeParamDAO;
import dio.persistence.entity.EmployeeEntity;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


public class Application {

	private final static String DB_URL = System.getenv("DB_URL");
	private final static String DB_USERNAME = System.getenv("DB_USERNAME");
	private final static String DB_PASSWORD = System.getenv("DB_PASSWORD");

	private final static EmployeeParamDAO employeeParamDAO = new EmployeeParamDAO();
	private final static EmployeeAuditDAO employeeAuditDAO = new EmployeeAuditDAO();

	public static void main(String[] args) {
		Flyway flyway = Flyway.configure().dataSource(DB_URL, DB_USERNAME, DB_PASSWORD).load();
		flyway.migrate();

		EmployeeEntity employeeInsert = new EmployeeEntity();
		employeeInsert.setName("Monica");
		employeeInsert.setSalary(new BigDecimal("2400"));
		employeeInsert.setBirthday(OffsetDateTime.now().minusYears(30));
		employeeParamDAO.insert(employeeInsert);

		employeeParamDAO.findAll().forEach(System.out::println);

		System.out.println(employeeParamDAO.findById(2));

		EmployeeEntity employeeUpdate = new EmployeeEntity();
		employeeUpdate.setId(2);
		employeeUpdate.setName("Hudson");
		employeeUpdate.setBirthday(OffsetDateTime.now().minusYears(21));
		employeeUpdate.setSalary(new BigDecimal("1412"));
		employeeParamDAO.update(employeeUpdate);

		employeeParamDAO.delete(4);

		// employeeAuditDAO.findAll().forEach(System.out::println);

	}

}
