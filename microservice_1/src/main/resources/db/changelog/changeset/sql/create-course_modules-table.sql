CREATE TABLE public.course_modules
(
    course_id bigint NOT NULL,
    modules_id bigint NOT NULL,
    CONSTRAINT uk_65atknawguv1oleqbww05c3a5 UNIQUE (modules_id),
    CONSTRAINT fk2kd9ath0mf6p3habbdwnwxi94 FOREIGN KEY (modules_id)
        REFERENCES public.module (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklr5ysf9t0n6t7mfjs73qvrk7u FOREIGN KEY (course_id)
        REFERENCES public.course (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
GO
ALTER TABLE public.course_modules
    OWNER to postgres;
GO