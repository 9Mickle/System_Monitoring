CREATE TABLE public.module
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    deadline timestamp without time zone,
    description character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    start_date timestamp without time zone NOT NULL,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    student_id bigint,
    course_id bigint,
    mentor_id bigint,
    CONSTRAINT module_pkey PRIMARY KEY (id),
    CONSTRAINT fk_mentor_id FOREIGN KEY (mentor_id)
        REFERENCES public.mentor (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_course_id FOREIGN KEY (course_id)
        REFERENCES public.course (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_student_id FOREIGN KEY (student_id)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
GO
ALTER TABLE public.module
    OWNER to postgres;
GO