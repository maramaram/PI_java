create database session2;
       use session2;
           create table session(
ID INTEGER(20) auto_increment primary key,
               Cap INTEGER(50) not null,
               Des varchar(20) not null,
               Type VARCHAR(20) not null
           );
           insert into session(Cap,Des,Type) values(50,"Yoga","endurance");