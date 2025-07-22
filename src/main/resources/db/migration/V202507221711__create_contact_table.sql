CREATE TABLE contacts(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    description VARCHAR(50) NOT NULL,
    type VARCHAR(30),

    FOREIGN KEY(employee_id) REFERENCES employees(id)
);