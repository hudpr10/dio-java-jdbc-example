package dio.persistence;

import dio.persistence.entity.EmployeeEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    public void insert(final EmployeeEntity employeeEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "INSERT INTO employees(name, birthday, salary) " +
            "VALUES ('" + employeeEntity.getName() + "', '" + formatOffsetDateTime(employeeEntity.getBirthday()) + "', '" + employeeEntity.getSalary().toString() + "')";

            System.out.println("Employee inserido.");
            System.out.println("Foram afetados: " + statement.getUpdateCount() + " registros na base de dados.");

            statement.executeUpdate(sql);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(final EmployeeEntity employeeEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "UPDATE employees " +
                    "SET " +
                    "name     = '"+ employeeEntity.getName() + "', " +
                    "birthday = '" + formatOffsetDateTime(employeeEntity.getBirthday()) + "', " +
                    "salary   = '" + employeeEntity.getSalary().toString() + "' " +
                    "WHERE id = " + employeeEntity.getId();
            statement.executeUpdate(sql);
            System.out.println("Employee atualizado.");
            System.out.println("Foram afetados: " + statement.getUpdateCount() + " registros na base de dados.");

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(final long id) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "DELETE FROM employees WHERE id = " + id;
            statement.executeUpdate(sql);

            System.out.println("Employee deletado.");
            System.out.println("Foram afetados: " + statement.getUpdateCount() + " registros na base de dados.");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<EmployeeEntity> findAll() {
        List<EmployeeEntity> employeeEntityList = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM employees ORDER BY name";
            statement.executeQuery(sql);
            ResultSet result = statement.getResultSet();

            while(result.next()) {
                EmployeeEntity employee = new EmployeeEntity();
                employee.setId(result.getLong("id"));
                employee.setName(result.getString("name"));
                employee.setSalary(result.getBigDecimal("salary"));

                OffsetDateTime birthday = convertTimestampToOffsetDateTime(result.getTimestamp("birthday"));
                employee.setBirthday(birthday);

                employeeEntityList.add(employee);
            }
            System.out.println("Foram afetados: " + statement.getUpdateCount() + " registros na base de dados.");

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return employeeEntityList;
    }

    public EmployeeEntity findById(final long id) {
        EmployeeEntity employee = new EmployeeEntity();

        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM employees WHERE id = " + id;
            statement.executeQuery(sql);

            ResultSet result = statement.getResultSet();

            if(result.next()) {
                employee.setId(result.getLong("id"));
                employee.setName(result.getString("name"));
                employee.setSalary(result.getBigDecimal("salary"));

                OffsetDateTime birthday = convertTimestampToOffsetDateTime(result.getTimestamp("birthday"));
                employee.setBirthday(birthday);
            }
            System.out.println("Foram afetados: " + statement.getUpdateCount() + " registros na base de dados.");

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return employee;
    }

    private String formatOffsetDateTime(final OffsetDateTime dateTime) {
        OffsetDateTime utcDateTime = dateTime.withOffsetSameInstant(ZoneOffset.UTC);
        return utcDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private OffsetDateTime convertTimestampToOffsetDateTime(final Timestamp date) {
        return OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
}
