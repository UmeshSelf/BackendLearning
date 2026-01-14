-- create table

create table [tableName](col1 colType(size) extra);

-- show tables
show tables;

-- describe tables
desc [tableName];

-- Alter table
-- add column
Alter table [tableName] add column2 varchar(10);

-- Modify column
Alter table [tableName] Modify column2 int(10);

-- Rename column
Alter table [tableName] rename column [old columnName] to [New ColumnName];

-- add column
Alter table [tableName] drop column (columName);

-- insert data into table
insert into [tableName] (column1,column2,column3) values (value1,value2,value3);

-- While inserting if we know the column sequence, 
insert into [tableName] values (value1,value2,value3);
-- To Insert bulk data in table
insert into [tableName] (column1,column2,column3) values (value1,value2,value3),(value1,value2,value3),(value1,value2,value3),(value1,value2,value3);


-- update data into table
update  [tableName] set column1=value1,column2=value2,column3=value3 where primaryKey = value ;

-- Order By
select * from [tableName] order by columnName