drop table if exists  users;
create table users(
    id int primary key auto_increment,
    token varchar(30)
);