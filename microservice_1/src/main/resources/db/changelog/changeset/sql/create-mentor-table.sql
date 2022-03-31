CREATE TABLE public.mentor
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    name character varying(255) COLLATE pg_catalog."default",
    surname character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    phone_number character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT mentor_pkey PRIMARY KEY (id),
    CONSTRAINT unique_mentor_email UNIQUE (email),
    CONSTRAINT unique_mentor_username UNIQUE (username)
)
GO
ALTER TABLE public.mentor
    OWNER to postgres;
GO