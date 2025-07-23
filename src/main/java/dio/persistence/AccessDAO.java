package dio.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccessDAO {
    public void insert(final long employeeId, final long moduleId) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO accesses(employee_id, module_id) VALUES (?, ?);");

            statement.setLong(1, employeeId);
            statement.setLong(2, moduleId);

            statement.executeUpdate();
            System.out.println("Dados de Acesso adicionados.");
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
