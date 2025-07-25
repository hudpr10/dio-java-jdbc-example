package dio.persistence.entity;

import lombok.Data;

import java.util.List;

@Data
public class ModuleEntity {
    private long id;
    private String name;
    private List<EmployeeEntity> employeeList;
}
