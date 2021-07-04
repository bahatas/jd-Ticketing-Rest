insert into roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time,
                  last_update_user_id, description)
VALUES ('2020-01-05 00:00:00',1,false,'2021-01-05 00:00:00',1,'Admin'),
       ('2020-01-05 00:00:00',1,false,'2021-01-05 00:00:00',1,'Manager'),
       ('2020-01-05 00:00:00',1,false,'2021-01-05 00:00:00',1,'Employee');
insert into users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                  enabled, first_name, gender, last_name, password, phone, user_name, role_id)

VALUES ('2021-01-05 00:00:00',1,false,'2021-01-05 00:00:00',1,
        true,'admin','MALE','admin','$2a$10$Q7ilQ6Hv11qpU0T7xfMzMeqxoPXkvhTVXxFqg0UL2xvLnhNqB7vba','0123456789','admin@admin.com',null);