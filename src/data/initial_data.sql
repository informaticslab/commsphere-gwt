
insert into role_type (id, version, role) values (1, 0, 'Administrator');
insert into role_type (id, version, role) values (2, 0, 'Coordinator');
insert into role_type (id, version, role) values (3, 0, 'Analyst');

insert into person (id, version, username, password, family_name, given_name, enabled)
       values (1, 0, 'admin', '$2a$10$tlxkGR7ZhSuIXeSQrkzmH.5mvOTHA69axRb8evNDkUzgagV.fjZMG', 
               'Administrator', '', true);
       
insert into person_roles (person, roles) values (1, 1);
insert into person_roles (person, roles) values (1, 2);
