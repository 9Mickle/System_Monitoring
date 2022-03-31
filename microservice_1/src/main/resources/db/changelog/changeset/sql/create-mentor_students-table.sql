CREATE TABLE public.mentor_students
(
    mentor_id bigint NOT NULL,
    students_id bigint NOT NULL,
    CONSTRAINT uk_ds5jsg1d3tucu94ewff5hetf4 UNIQUE (students_id),
    CONSTRAINT fkmax7g1xkr4osp6wk6ati4ihs4 FOREIGN KEY (students_id)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkslof18mc1j4o1d17fv9olj9x9 FOREIGN KEY (mentor_id)
        REFERENCES public.mentor (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
GO
ALTER TABLE public.mentor_students
    OWNER to postgres;
GO