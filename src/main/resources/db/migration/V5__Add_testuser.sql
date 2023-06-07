insert into users (id, email, is_deleted, login, password, registration_date, role, score)
values (1, 'test_user@gmail.com', false, 'testUser', '$2a$10$QAhKArfOfjP153PjtUCkguMSOPnp8iCOkapa0.J0kGt4wkjAXBB1S',
        CURRENT_DATE, 'USER', 0);

insert into users_data (date_of_birth, changed, country, first_name, last_name, phone_number, user_id)
values ('1985-02-23',  CURRENT_DATE, 'Belarus', 'Viktar', 'Tromza', '375297987348', 1);

alter sequence users_id_seq start 2 increment 1;