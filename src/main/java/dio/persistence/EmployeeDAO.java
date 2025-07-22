package dio.persistence;

import dio.persistence.entity.EmployeeEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmployeeDAO {
    public void insert(final EmployeeEntity employeeEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "INSERT INTO employees(name, birthday, salary) " +
            "VALUES ('" + employeeEntity.getName() + "', '" + formatOffsetDateTime(employeeEntity.getBirthday()) + "', '" + employeeEntity.getSalary().toString() + "')";

            // System.out.println("Foram afetados: " + statement.getUpdateCount() + " registros na base de dados.");

            statement.executeUpdate(sql);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(final EmployeeEntity employeeEntity) {

    }

    public void delete(final long id) {

    }

    public List<EmployeeEntity> findAll() {
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM employees";

            statement.executeUpdate(sql);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public EmployeeEntity findById(final long id) {
        return null;
    }

    private String formatOffsetDateTime(final OffsetDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
