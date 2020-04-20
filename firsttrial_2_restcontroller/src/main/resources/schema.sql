DROP TABLE IF EXISTS CUSTOMER;
  
CREATE TABLE CUSTOMER (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(320) DEFAULT NULL
);