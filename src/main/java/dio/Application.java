package dio;

import dio.persistence.ContactDAO;
import dio.persistence.EmployeeAuditDAO;
import dio.persistence.EmployeeDAO;

import dio.persistence.EmployeeParamDAO;
import dio.persistence.entity.ContactEntity;
import dio.persistence.entity.EmployeeEntity;
import net.datafaker.Faker;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


public class Application {

	private final static String DB_URL = System.getenv("DB_URL");
	private final static String DB_USERNAME = System.getenv("DB_USERNAME");
	private final static String DB_PASSWORD = System.getenv("DB_PASSWORD");

	private final static Faker faker = new Faker(Locale.of("pt", "BR"));

	private final static EmployeeParamDAO employeeParamDAO = new EmployeeParamDAO();
	private final static EmployeeAuditDAO employeeAuditDAO = new EmployeeAuditDAO();
	private final static ContactDAO contactDAO = new ContactDAO();

	public static void main(String[] args) {
		try {
			Flyway flyway = Flyway.configure().dataSource(DB_URL, DB_USERNAME, DB_PASSWORD).load();
			flyway.migrate();
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		/*EmployeeEntity employeeInsert = new EmployeeEntity();
		employeeInsert.setName("Tiago");
		employeeInsert.setSalary(new BigDecimal("8000"));
		employeeInsert.setBirthday(OffsetDateTime.now().minusYears(22));
		employeeParamDAO.insertWithProcedure(employeeInsert);*/

		//employeeParamDAO.findAll().forEach(System.out::println);

		//System.out.println(employeeParamDAO.findById(2));

		/*EmployeeEntity employeeUpdate = new EmployeeEntity();
		employeeUpdate.setId(2);
		employeeUpdate.setName("Hudson");
		employeeUpdate.setBirthday(OffsetDateTime.now().minusYears(21));
		employeeUpdate.setSalary(new BigDecimal("1412"));
		employeeParamDAO.update(employeeUpdate);*/

		// employeeParamDAO.delete(4);

		// employeeAuditDAO.findAll().forEach(System.out::println);

		/*List<EmployeeEntity> fakeEmployeeList = Stream.generate(() -> {
			EmployeeEntity employee = new EmployeeEntity();
			employee.setName(faker.name().fullName());
			employee.setSalary(new BigDecimal(faker.number().digits(4)));
			employee.setBirthday(OffsetDateTime.of(faker.date().birthdayLocalDate(18, 50), LocalTime.MIN, ZoneOffset.UTC));
			return employee;
		}).limit(10000).toList();

		employeeParamDAO.insert(fakeEmployeeList);*/

		/*ContactEntity contact = new ContactEntity();
		EmployeeEntity employee = new EmployeeEntity();
		contact.setDescription("lucas@mail.com");
		contact.setType("e-mail");

		employee.setName("Hudson");
		employee.setSalary(new BigDecimal("2000"));
		employee.setBirthday(OffsetDateTime.now().minusYears(21));
		contact.setEmployee(employee);

		employeeParamDAO.insertWithProcedure(employee);
		contactDAO.insert(contact);*/

		System.out.println(employeeParamDAO.findById(37365));
	}

}
