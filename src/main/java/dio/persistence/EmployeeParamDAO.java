package dio.persistence;

import dio.persistence.entity.EmployeeEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EmployeeParamDAO {
    public void insert(final EmployeeEntity employeeEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO employees(name, salary, birthday) values(?, ?, ?);");
            statement.setString(1, employeeEntity.getName());
            statement.setBigDecimal(2, employeeEntity.getSalary());
            statement.setTimestamp(3, Timestamp.valueOf(employeeEntity.getBirthday().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));

            statement.executeUpdate();
            System.out.println("Employee inserido.");

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(final EmployeeEntity employeeEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE employees SET name = ?, salary = ?, birthday = ? WHERE id = ?;");

            statement.setString(1, employeeEntity.getName());
            statement.setBigDecimal(2, employeeEntity.getSalary());
            statement.setTimestamp(3, Timestamp.valueOf(employeeEntity.getBirthday().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
            statement.setLong(4, employeeEntity.getId());

            statement.executeUpdate();
            System.out.println("Employee atualizado.");

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete(final long id) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM employees WHERE id = ?");
            statement.setLong(1, id);

            statement.executeUpdate();
            System.out.println("Employee deletado.");

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

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return employeeEntityList;
    }

    public EmployeeEntity findById(final long id) {
        EmployeeEntity employee = new EmployeeEntity();

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees WHERE id = ?");
            statement.setLong(1, id);

            statement.executeQuery();
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

    private OffsetDateTime convertTimestampToOffsetDateTime(final Timestamp date) {
        return OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
}
