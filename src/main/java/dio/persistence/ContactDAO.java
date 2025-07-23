package dio.persistence;

import dio.persistence.entity.ContactEntity;
import dio.persistence.entity.EmployeeEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    public void insert(final ContactEntity contactEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO contacts (description, type, employee_id) VALUES (?, ?, ?);");

            statement.setString(1, contactEntity.getDescription());
            statement.setString(2, contactEntity.getType());
            statement.setLong(3, contactEntity.getId());

            statement.executeUpdate();
            System.out.println("Contato Inserido");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<ContactEntity> findByEmployeeId(final long employeeId) {
        List<ContactEntity> contactList = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts WHERE employee_id = ?");

            statement.setLong(1, employeeId);
            statement.executeQuery();

            ResultSet result = statement.getResultSet();
            while(result.next()) {
                EmployeeEntity employee = new EmployeeEntity();
                employee.setId(result.getLong("employee_id"));

                ContactEntity contact = new ContactEntity();
                contact.setId(result.getLong("id"));
                contact.setEmployeeId(result.getLong("employee_id"));
                contact.setDescription(result.getString("description"));
                contact.setType(result.getString("type"));

                contactList.add(contact);
            }

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return contactList;
    }
}
