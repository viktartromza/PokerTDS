alter sequence users_games_id_seq owned by users_games.id;

alter table if exists users_games
    alter column id set default nextval('users_games_id_seq');
