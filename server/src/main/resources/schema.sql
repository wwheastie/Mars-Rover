create table image
(
    photoid integer not null,
    imageblob longblob,
    nasaurl varchar(200) not null unique,
    url varchar(200) not null unique,
    imgdate date not null,
    imgsize integer not null,
    primary key(photoid)
);