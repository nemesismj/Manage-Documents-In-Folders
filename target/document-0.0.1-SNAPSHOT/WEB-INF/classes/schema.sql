create table users
(
    username varchar(50)  not null primary key,
    password varchar(200) not null,
    enabled  boolean                 not null
);

create table authorities
(
    id        int primary key auto_increment,
    username  varchar(50) not null,
    authority varchar(50) not null
);
DROP TABLE IF EXISTS file;
create table file
(
    file_name      varchar(100) not null primary key,
    insertion_date timestamp               not null default current_timestamp(),
    file_type      varchar(50)  not null,
    user_uploader  varchar(50)  not null,
    constraint fk_files_users foreign key (user_uploader) references users (username)
);
DROP TABLE IF EXISTS file_authority;
create table file_authority
(
    id        int primary key auto_increment,
    file_name varchar(100),
    file_authority varchar(50),
    constraint fk_files_authority foreign key (file_name) references file (file_name));
