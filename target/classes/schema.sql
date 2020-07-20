INSERT INTO `database`.`users`
(`username`,
`email`,
`enabled`,
`firstname`,
`lastname`,
`password`)
VALUES
('admin',
'admin@mail.ru',
1,
'admin',
'admin',
'$2a$04$Rav/a84q0v5FvE8LtI1xK.lv1fKnSguuEkUvHTt7lIna9n4P5M7SC');

INSERT INTO `database`.`authorities`
(
`authority`,
`username`)
VALUES
(
'ROLE_ADMIN',
'admin');



