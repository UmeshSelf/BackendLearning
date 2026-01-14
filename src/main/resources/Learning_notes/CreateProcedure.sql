DELIMITER //
CREATE PROCEDURE CreateUserTable()
BEGIN
  CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    city VARCHAR(255)
  );
  INSERT INTO users (first_name, last_name, city) VALUES ('Durgesh', 'Tiwari', 'Lucknow');
  INSERT INTO users (first_name, last_name, city) VALUES ('Ankit', 'Shukla', 'Delhi');
  SELECT * FROM users;
END//
DELIMITER ;



To call created PROCEDURE
call CreateUserTable() ;

Use IN parameter

delimiter //
mysql> Create procedure SelectByLast(IN l_name varchar(255))
    -> begin
    -> select * from users where last_name=l_name;
    -> end//




call SelectByLast('Tiwari')

