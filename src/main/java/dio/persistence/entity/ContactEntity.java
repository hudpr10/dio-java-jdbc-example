package dio.persistence.entity;

import lombok.Data;

@Data
public class ContactEntity {
    private long id;
    private String description;
    private String type;
    private long employeeId;
}
