-- Создаём таблицу пользователей

CREATE TABLE IF NOT EXISTS users (

	id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	username VARCHAR(255) NOT NULL UNIQUE,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	avatar bytea,
	status VARCHAR(20),
	role VARCHAR(50) NOT NULL DEFAULT 'USER'

);


-- Создаём таблицу чатов

CREATE TABLE IF NOT EXISTS chats (

	id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	name VARCHAR(255),
	type VARCHAR(100),
	createdAt timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- Создаём таблицу для связи чатов и пользователей

CREATE TABLE IF NOT EXISTS chat_participants
(
    chat_id integer NOT NULL,
    user_id integer NOT NULL,
    joined_at timestamp without time zone NOT NULL DEFAULT now(),
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    CONSTRAINT "PK_Id" PRIMARY KEY (id),
    CONSTRAINT unique_chat_id_user_id UNIQUE (chat_id, user_id),
    CONSTRAINT chat_participants_chat_id_fkey FOREIGN KEY (chat_id)
        REFERENCES chats (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chat_participants_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


-- Создаём таблицу сообщений

CREATE TABLE IF NOT EXISTS messages(

	id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
	chat_id int NOT NULL REFERENCES chats(id),
	sender_id int NOT NULL REFERENCES users(id),
	content TEXT NOT NULL,
	status VARCHAR(20) NOT NULL DEFAULT 'SENT',
	created_at TIMESTAMP NOT NULL DEFAULT NOW()

);