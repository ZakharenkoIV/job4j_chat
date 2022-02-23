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
values ('pete', '123@gmail.ru', '0');
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

CREATE UNIQUE INDEX "UI_rule_to_person_person_id_role_id"
    ON "role_to_person"
        USING btree
        ("role_id", "person_id");

insert into role_to_person (role_id, person_id)
values (1, 1);
insert into role_to_person (role_id, person_id)
values (2, 2);
insert into role_to_person (role_id, person_id)
values (3, 3);

create table messages
(
    id        bigserial primary key not null,
    content   varchar(2000)         not null,
    author_id bigint                not null
);

insert into messages (content, author_id)
values ('My name is Pete', 1);
insert into messages (content, author_id)
values ('My name is Ban', 2);
insert into messages (content, author_id)
values ('My name is Ivan', 3);

create table rooms
(
    id   bigserial primary key not null,
    name varchar(255)          not null
);

insert into rooms (name)
values ('hello');

CREATE TABLE room_to_person
(
    id        bigserial primary key,
    room_id   bigint not null references rooms ("id"),
    person_id bigint not null references persons ("id")
);

CREATE UNIQUE INDEX "UI_room_to_person_person_id_room_id"
    ON "room_to_person"
        USING btree
        ("room_id", "person_id");

insert into room_to_person (room_id, person_id)
values (1, 1);
insert into room_to_person (room_id, person_id)
values (1, 2);
insert into room_to_person (room_id, person_id)
values (1, 3);

CREATE TABLE message_to_room
(
    id         bigserial primary key,
    room_id    bigint not null references rooms ("id"),
    message_id bigint not null references messages ("id")
);

CREATE UNIQUE INDEX "UI_room_to_message_message_id_room_id"
    ON "message_to_room"
        USING btree
        ("room_id", "message_id");

insert into message_to_room (room_id, message_id)
values (1, 1);
insert into message_to_room (room_id, message_id)
values (1, 2);
insert into message_to_room (room_id, message_id)
values (1, 3);
