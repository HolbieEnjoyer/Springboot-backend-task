alter table employees
alter column id type bigint;

alter sequence employees_id_seq as bigint;

alter table employees
alter column id set default nextval('employees_id_seq');