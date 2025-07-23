package dio;

import dio.persistence.ContactDAO;
import dio.persistence.EmployeeAuditDAO;
import dio.persistence.EmployeeDAO;

import dio.persistence.EmployeeParamDAO;
import dio.persistence.ModuleDAO;
import dio.persistence.entity.ContactEntity;
import dio.persistence.entity.EmployeeEntity;
import dio.persistence.entity.ModuleEntity;
import net.datafaker.Faker;
import org.flywaydb.core.Flyway;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
	private final static ModuleDAO moduleDAO = new ModuleDAO();

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

			List<ModuleEntity> moduleList = new ArrayList<>();
			int moduleAmount = faker.number().numberBetween(1, 4);
			for(int i = 0; i < moduleAmount; i++) {
				ModuleEntity module = new ModuleEntity();
				module.setId(i + 1);
				moduleList.add(module);
			}
			employee.setModuleList(moduleList);

			return employee;
		}).limit(3).toList();

		fakeEmployeeList.forEach(employeeParamDAO::insertWithProcedure);*/

		/*EmployeeEntity employee = new EmployeeEntity();
		employee.setName("Maria");
		employee.setSalary(new BigDecimal("4000"));
		employee.setBirthday(OffsetDateTime.now().minusYears(21));
		employeeParamDAO.insertWithProcedure(employee);

		ContactEntity contact1 = new ContactEntity();
		contact1.setDescription("maria@mail.com");
		contact1.setType("e-mail");
		contact1.setEmployeeId(employee.getId());
		contactDAO.insert(contact1);*/

		/*ContactEntity contact2 = new ContactEntity();
		contact2.setDescription("(00) 91122-3344");
		contact2.setType("phone");
		contact2.setEmployeeId(employee.getId());
		contactDAO.insert(contact2);*/

		// System.out.println(employeeParamDAO.findById(1));
		//employeeParamDAO.findAll().forEach(System.out::println);

		//moduleDAO.findAll().forEach(System.out::println);
	}

}
