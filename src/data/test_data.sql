
insert into person (id, version, username, password, family_name, given_name, enabled)
       values (2, 0, 'tsavel', '$2a$10$tlxkGR7ZhSuIXeSQrkzmH.5mvOTHA69axRb8evNDkUzgagV.fjZMG', 
               'Savel', 'Tom', true);
insert into person (id, version, username, password, family_name, given_name, enabled)
       values (3, 0, 'sbrown', '$2a$10$tlxkGR7ZhSuIXeSQrkzmH.5mvOTHA69axRb8evNDkUzgagV.fjZMG', 
               'Brown', 'Sara', true);
               
insert into person (id, version, username, password, family_name, given_name, enabled)
       values (4, 0, 'analyst1', '$2a$10$tlxkGR7ZhSuIXeSQrkzmH.5mvOTHA69axRb8evNDkUzgagV.fjZMG', 
               'One', 'Analyst', true);
insert into person (id, version, username, password, family_name, given_name, enabled)
       values (5, 0, 'analyst2', '$2a$10$tlxkGR7ZhSuIXeSQrkzmH.5mvOTHA69axRb8evNDkUzgagV.fjZMG', 
               'Two', 'Analyst', true);
insert into person (id, version, username, password, family_name, given_name, enabled)
       values (6, 0, 'analyst3', '$2a$10$tlxkGR7ZhSuIXeSQrkzmH.5mvOTHA69axRb8evNDkUzgagV.fjZMG', 
               'Three', 'Analyst', true);
insert into person (id, version, username, password, family_name, given_name, enabled)
       values (7, 0, 'analyst4', '$2a$10$tlxkGR7ZhSuIXeSQrkzmH.5mvOTHA69axRb8evNDkUzgagV.fjZMG', 
               'Four', 'Analyst', true);
       
insert into person_roles (person, roles) values (2, 2);
insert into person_roles (person, roles) values (3, 2);

insert into person_roles (person, roles) values (4, 3);
insert into person_roles (person, roles) values (5, 3);
insert into person_roles (person, roles) values (6, 3);
insert into person_roles (person, roles) values (7, 3);


