package dio.persistence;

import dio.persistence.entity.ContactEntity;
import dio.persistence.entity.EmployeeEntity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class EmployeeParamDAO {
    private final ContactDAO contactDAO = new ContactDAO();

    public void insertWithProcedure(final EmployeeEntity employeeEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            CallableStatement statement = connection.prepareCall("CALL prc_insert_employee(?, ?, ?, ?);");

            statement.registerOutParameter(1, TimeZone.LONG);
            statement.setString(2, employeeEntity.getName());
            statement.setBigDecimal(3, employeeEntity.getSalary());
            statement.setTimestamp(4, Timestamp.valueOf(employeeEntity.getBirthday().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));

            statement.execute();
            System.out.println("Employee inserido.");

            employeeEntity.setId(statement.getLong(1));

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insert(final List<EmployeeEntity> employeeEntityList) {
        try(Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO employees (name, salary, birthday) VALUES (?, ?, ?);";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                // O padrão de Commit é TRUE - Caso tiver algum problema na inserção ele poderia dar rollback
                // Como FALSE o cuidado precisa ser MANUAL
                connection.setAutoCommit(false);

                for(int i = 0; i < employeeEntityList.size(); i++) {
                    statement.setString(1, employeeEntityList.get(i).getName());
                    statement.setBigDecimal(2, employeeEntityList.get(i).getSalary());
                    statement.setTimestamp(3, Timestamp.valueOf(employeeEntityList.get(i).getBirthday().atZoneSimilarLocal(ZoneOffset.UTC).toLocalDateTime()));
                    statement.addBatch(); // Adiciona na FILA
                    if(i % 1000 == 0 || i == employeeEntityList.size() - 1) {
                        statement.executeBatch(); // Executa o lote
                    }

                    if(i == 8000) throw new SQLException(); // vai fazer o rollback
                }

                connection.commit(); // Commit final onde os dados são inseridos

            } catch(SQLException ex) {
                connection.rollback();
                ex.printStackTrace();
            }
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

            statement.executeQuery("SELECT * FROM employees ORDER BY name");

            ResultSet result = statement.getResultSet();
            while(result.next()) {
                EmployeeEntity employee = new EmployeeEntity();
                employee.setId(result.getLong("id"));
                employee.setName(result.getString("name"));
                employee.setSalary(result.getBigDecimal("salary"));
                employee.setBirthday(convertTimestampToOffsetDateTime(result.getTimestamp("birthday")));
                employee.setContactList(contactDAO.findByEmployeeId(employee.getId()));

                employeeEntityList.add(employee);
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return employeeEntityList;
    }

    public EmployeeEntity findById(final long id) {
        EmployeeEntity employee = new EmployeeEntity();
        List<ContactEntity> contactList = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT e.id AS employee_id, e.name, e.salary, e.birthday, c.id AS contact_id, c.description, c.type " +
                    "FROM employees e LEFT JOIN contacts c ON e.id = c.employee_id " +
                    "WHERE e.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);

            statement.executeQuery();
            ResultSet result = statement.getResultSet();

            if(result.next()) {
                employee.setId(result.getLong("employee_id"));
                employee.setName(result.getString("name"));
                employee.setSalary(result.getBigDecimal("salary"));
                employee.setBirthday(convertTimestampToOffsetDateTime(result.getTimestamp("birthday")));

                do {
                    ContactEntity contact = new ContactEntity();
                    contact.setId(result.getLong("contact_id"));
                    contact.setDescription(result.getString("description"));
                    contact.setType(result.getString("type"));
                    contactList.add(contact);
                } while(result.next());

                employee.setContactList(contactList);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return employee;
    }

    private OffsetDateTime convertTimestampToOffsetDateTime(final Timestamp date) {
        return OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
}
