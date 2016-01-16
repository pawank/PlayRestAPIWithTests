# --- !Ups
create table "USERS" ("ID" INTEGER PRIMARY KEY,"USER" VARCHAR NOT NULL,"PASSWORD" VARCHAR NOT NULL);

insert into "USERS" ("USER","PASSWORD") values ("admin","admin");
# --- !Downs
drop table "USERS";
