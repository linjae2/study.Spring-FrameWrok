
# Spring Framework6.2.8

## gradle project 등록

```sh
$ gradle help --task init

$ gradle init --type java-application --no-incubating --no-split-project --java-version 17 --dsl groovy --test-framework junit-jupiter --project-name HelloWorld

# Runs this project as a JVM appliation
$ gradle run
```

## Spring Boot Application 으로 변경

```sh
$ gradle build

# Runs this project as a Spring Boot application
$ gradle bootRun
```



# DataJpaTest

```java
@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@EnableJpaRepositories(considerNestedRepositories = true)
```


```sql
alter table if exists test.foreign_key_test$fk_post
  drop
  foreign key if exists FKlo51hdvbm5p605mbpoh1nk0ej;

create table `foreign_key_test$fk_post` (
  id bigint not null auto_increment,
  `member_id` bigint,
  content varchar(255),
  title varchar(255),
  primary key (id)
) engine=InnoDB

create table `foreign_key_test$member` (
  id bigint not null auto_increment,
  name varchar(255),
  primary key (id)
) engine=InnoDB

 alter table if exists test.foreign_key_test$fk_post
  add constraint FKlo51hdvbm5p605mbpoh1nk0ej
  foreign key (member_id)
  references test.foreign_key_test$member (id);
```

