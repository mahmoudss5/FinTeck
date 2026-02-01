use azbanking;
CREATE TABLE loan_applications (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   user_id BIGINT NOT NULL,
                                   full_name VARCHAR(255) NOT NULL,
                                   email VARCHAR(255) NOT NULL,
                                   phone_number VARCHAR(50) NOT NULL,
                                   date_of_birth DATE NOT NULL,
                                   marital_status VARCHAR(50) NOT NULL,

                                   employment_status VARCHAR(50) NOT NULL,
                                   monthly_income DECIMAL(19, 2) NOT NULL,
                                   employer_name VARCHAR(255),
                                   years_at_current_job VARCHAR(50),

                                   loan_purpose VARCHAR(50) NOT NULL,
                                   requested_amount DECIMAL(19, 2) NOT NULL,

                                   status VARCHAR(50) DEFAULT 'PENDING',
                                   applied_at DATETIME DEFAULT CURRENT_TIMESTAMP,

                                   FOREIGN KEY (user_id) REFERENCES users(id)
);