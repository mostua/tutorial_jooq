-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2015-05-17 18:24:18.697


-- tables
-- Table: friend
CREATE TABLE friend (
    id int  NOT NULL,
    name varchar(255)  NOT NULL,
    surname varchar(255)  NOT NULL,
    CONSTRAINT friend_pk PRIMARY KEY (id)
);


-- Table: phone_number
CREATE TABLE phone_number (
    id int  NOT NULL,
    phone varchar(13)  NOT NULL,
    friend_Id int  NOT NULL,
    CONSTRAINT phone_number_pk PRIMARY KEY (id)
);


-- foreign keys
-- Reference:  phone_number_friend (table: phone_number)


ALTER TABLE phone_number ADD CONSTRAINT phone_number_friend 
    FOREIGN KEY (friend_Id)
    REFERENCES friend (id)
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE 
;

-- sequences
-- Sequence: friend_id_seq

CREATE SEQUENCE friend_id_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 500 
      
      NO CYCLE
      
;

-- Sequence: phone_number_id_seq

CREATE SEQUENCE phone_number_id_seq
      INCREMENT BY 1
      NO MINVALUE
      NO MAXVALUE
      START WITH 500 
      
      NO CYCLE
      
;

-- End of file.

