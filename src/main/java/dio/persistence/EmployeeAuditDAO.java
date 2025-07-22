package dio.persistence;

import dio.persistence.entity.EmployeeAuditEntity;
import dio.persistence.entity.EmployeeEntity;
import dio.persistence.entity.OperationEnum;

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

import static java.util.Objects.isNull;

public class EmployeeAuditDAO {
    public List<EmployeeAuditEntity> findAll() {
        List<EmployeeAuditEntity> employeeAuditEntityList = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT * FROM view_employee_audit";
            statement.executeQuery(sql);

            ResultSet result = statement.getResultSet();
            while(result.next()) {
                employeeAuditEntityList.add(new EmployeeAuditEntity(
                        result.getLong("employee_id"),
                        result.getString("name"),
                        result.getString("old_name"),
                        result.getBigDecimal("salary"),
                        result.getBigDecimal("old_salary"),
                        getDateTimeOrNull(result.getTimestamp("birthday")),
                        getDateTimeOrNull(result.getTimestamp("old_birthday")),
                        OperationEnum.getByDbOperation(result.getString("operation"))
                ));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return employeeAuditEntityList;
    }

    private OffsetDateTime getDateTimeOrNull(final Timestamp date) {
        return isNull(date)
                ? null
                : OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }
}
