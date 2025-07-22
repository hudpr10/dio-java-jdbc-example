package dio.persistence;

import dio.persistence.entity.ContactEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactDAO {
    public void insert(final ContactEntity contactEntity) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO contacts (description, type, employee_id) VALUES (?, ?, ?);");

            statement.setString(1, contactEntity.getDescription());
            statement.setString(2, contactEntity.getType());
            statement.setLong(3, contactEntity.getEmployee().getId());

            statement.executeUpdate();
            System.out.println("Contato Inserido");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
