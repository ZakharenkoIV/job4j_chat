create table roles
(
    id   bigserial primary key not null,
    name varchar(255)          not null
);

insert into roles (name)
values ('user');
insert into roles (name)
values ('admin');
insert into roles (name)
values ('moderator');

create table persons
(
    id       bigserial primary key not null,
    name     varchar(255)          not null,
    email    varchar(255)          not null,
    password varchar(255)          not null
);

insert into persons (name, email, password)
values ('pete', '123@gmail.ru', '$2a$10$a5AUrt19/5JJXGHdRrgeFOxgj.fvpHCwkuzG540RoXuyi28F4QOvS');
insert into persons (name, email, password)
values ('ban', 'qwerty@gmail.ru', '0');
insert into persons (name, email, password)
values ('ivan', 'a@gmail.ru', '0');

CREATE TABLE role_to_person
(
    id        bigserial primary key,
    role_id   bigint not null references roles ("id"),
    person_id bigint not null references persons ("id")
);

insert into role_to_person (role_id, person_id)
values (1, 1);
insert into role_to_person (role_id, person_id)
values (2, 2);
insert into role_to_person (role_id, person_id)
values (3, 3);

create table messages
(
    id        bigserial primary key not null,
    content   varchar(2000)         not null
);

insert into messages (content)
values ('My name is Pete');
insert into messages (content)
values ('My name is Ban');
insert into messages (content)
values ('My name is Ivan');

create table rooms
(
    id   bigserial primary key not null,
    name varchar(255)          not null
);

insert into rooms (name)
values ('hello');

CREATE TABLE message_to_room
(
    id         bigserial primary key,
    room_id    bigint not null references rooms ("id"),
    message_id bigint not null references messages ("id")
);

insert into message_to_room (room_id, message_id)
values (1, 1);
insert into message_to_room (room_id, message_id)
values (1, 2);
insert into message_to_room (room_id, message_id)
values (1, 3);
