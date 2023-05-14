create sequence bets_poker_id_seq start 1 increment 1;
create sequence bets_roulette_id_seq start 1 increment 1;
create sequence games_id_seq start 1 increment 1;
create sequence roulette_games_id_seq start 1 increment 1;
create sequence texasholdem_games_id_seq start 1 increment 1;
create sequence users_id_seq start 1 increment 1;
create sequence users_games_id_seq start 1 increment 1;
create sequence wallets_id_seq start 1 increment 1;

create table bets_poker
(
    id            int4 not null,
    casino_amount numeric(19, 2),
    game_id       int4,
    player_amount numeric(19, 2),
    round         int4,
    type_casino   varchar(255),
    type_player   varchar(255),
    primary key (id)
);

create table bets_roulette
(
    id              int4 not null,
    amount          numeric(19, 2),
    game_id         int4,
    player_choise   varchar(255),
    roulette_number int4,
    type            varchar(255),
    winning_amount  float8,
    primary key (id)
);

create table games
(
    id          int4 not null,
    time_create timestamp,
    finish      timestamp,
    result      float8,
    status      varchar(255),
    type        varchar(255),
    primary key (id)
);

create table roulette_games
(
    id      int4 not null,
    changed timestamp,
    game_id int4,
    losses  int4,
    result  float8,
    spin    int4,
    status  varchar(255),
    wins    int4,
    primary key (id),
    unique (game_id)
);

create table texasholdem_games
(
    id             int4 not null,
    bank           float8,
    casino_preflop varchar(255),
    changed        timestamp,
    flop           varchar(255),
    game_id        int4,
    player_deposit float8,
    player_preflop varchar(255),
    result         float8,
    river          varchar(255),
    status         varchar(255),
    tern           varchar(255),
    winner         varchar(255),
    primary key (id),
    unique (game_id)
);

create table users
(
    id                int4 not null,
    email             varchar(255),
    is_deleted        boolean,
    login             varchar(255),
    password          varchar(255),
    registration_date date,
    role              varchar(255),
    score             float8,
    primary key (id)
);

create table users_data
(
    date_of_birth date,
    changed       timestamp,
    country       varchar(255),
    first_name    varchar(255),
    last_name     varchar(255),
    phone_number  varchar(255),
    user_id       int4 not null,
    primary key (user_id)
);

create table users_games
(
    id      int4 not null,
    game_id int4 not null,
    user_id int4 not null,
    primary key (id)
);

create table wallets
(
    id      int4 not null,
    balance numeric(19, 2),
    user_id int4,
    primary key (id)
);

alter table if exists users_data
    add constraint users_data_fk
    foreign key (user_id) references users;

alter table if exists users_games
    add constraint users_games_games_id_fk
    foreign key (game_id) references games;

alter table if exists users_games
    add constraint users_games_users_id_fk
    foreign key (user_id) references users;

alter table if exists wallets
    add constraint wallets_users_id_fk
    foreign key (user_id) references users;

alter table if exists roulette_games
    add constraint roulette_games_games_id_fk
    foreign key (game_id) references games;

alter table if exists bets_roulette
    add constraint bets_roulette_roulette_games_game_id_fk
    foreign key (game_id) references roulette_games (game_id);

alter table if exists texasholdem_games
    add constraint texasholdem_games_games_id_fk
        foreign key (game_id) references games;

alter table if exists bets_poker
    add constraint bets_poker_games_id_fk
        foreign key (game_id) references texasholdem_games (game_id);
