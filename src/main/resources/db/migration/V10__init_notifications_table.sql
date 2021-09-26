drop table if exists  notifications;
create table notifications(
    id int primary key auto_increment,
    title varchar(30),
    message varchar(100),
    image varchar(100)
);