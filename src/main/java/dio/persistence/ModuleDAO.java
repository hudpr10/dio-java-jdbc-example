package dio.persistence;

import dio.persistence.entity.EmployeeEntity;
import dio.persistence.entity.ModuleEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ModuleDAO {
    public List<ModuleEntity> findAll() {
        List<ModuleEntity> moduleList = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT m.id module_id, m.name module_name, e.id employee_id, e.name employee_name, e.salary employee_salary, e.birthday employee_birthday " +
                    "FROM employees e " +
                    "INNER JOIN accesses a ON e.id = a.employee_id " +
                    "INNER JOIN modules m ON  m.id = a.module_id " +
                    "ORDER BY m.id";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeQuery();

            ResultSet result = statement.getResultSet();
            boolean hasNext = result.next();
            while(hasNext) {
                ModuleEntity module = new ModuleEntity();
                List<EmployeeEntity> employeeList = new ArrayList<>();
                module.setId(result.getLong("module_id"));
                module.setName(result.getString("module_name"));

                do {
                    EmployeeEntity employee = new EmployeeEntity();
                    employee.setId(result.getLong("employee_id"));
                    employee.setName(result.getString("employee_name"));
                    employee.setSalary(result.getBigDecimal("employee_salary"));
                    employee.setBirthday(convertTimestampToOffsetDateTime(result.getTimestamp("employee_birthday")));
                    employeeList.add(employee);

                    hasNext = result.next();
                } while((hasNext) && (module.getId() == result.getLong("module_id")));

                module.setEmployeeList(employeeList);
                moduleList.add(module);
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return moduleList;
    }

    private OffsetDateTime convertTimestampToOffsetDateTime(final Timestamp date) {
        return OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
}
