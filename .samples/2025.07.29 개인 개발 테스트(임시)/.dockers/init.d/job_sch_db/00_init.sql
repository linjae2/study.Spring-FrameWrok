create user 'sdman'@'%' identified by 'snudh!v123';
grant all privileges on *.* to 'sdman'@'%';
flush privileges;

CREATE
    DATABASE sch DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;