delete from users where id=1;

insert into users (id, email, is_deleted, login, password, registration_date, role, score)
values (1000000, 'onlinecasinotds@gmail.com', false, 'admin', '$2a$10$wpcBNtouiZsA1dhctnbKyuxKVjSEGubH6RskCKzgbn7Ridinag/Fy',
        CURRENT_DATE, 'ADMIN', 0);