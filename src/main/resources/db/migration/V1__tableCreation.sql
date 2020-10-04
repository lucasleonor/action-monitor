create table user
(
    id       int primary key auto_increment,
    username varchar not null unique,
    password varchar not null
);

create table message
(
    id        int primary key auto_increment,
    text      varchar  not null,
    send_to   int      not null,
    sent_from int      not null,
    datetime  datetime not null default sysdate(),

    foreign key (send_to) references user (id),
    foreign key (sent_from) references user (id)
);

