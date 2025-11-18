alter table employees
alter column name type varchar(50);

alter table employees
drop constraint if exists employees_name_key;