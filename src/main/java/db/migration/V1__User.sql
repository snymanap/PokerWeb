-- User table
create table User (
   username varchar(12) not null,
    password varchar(255) not null,
    --salt varchar(255) not null,
    balance BIGINT,
    primary key (username)
);

create table Game(
    gameName varchar(100) not null,
    gameDate timestamp,
    active bit,
    host varchar(100),
    buyIn BIGINT,
    primary key (gameName)
);

create table UserGame(
    username varchar(12) not null,
    gameName varchar(100) not null,
    hand varchar(100) not null,
    constraint username_fk foreign key (username) references User(username),
    constraint gameName_fk foreign key (gameName) references Game(gameName),
    primary key(username, gameName)
);

--create sequence hibernate_sequence;