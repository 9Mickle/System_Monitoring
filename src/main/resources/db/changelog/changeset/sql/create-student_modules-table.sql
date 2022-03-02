CREATE TABLE public.student_modules
(
    student_id bigint NOT NULL,
    modules_id bigint NOT NULL,
    CONSTRAINT uk_47ad09nh6hhgqi097riixi06 UNIQUE (modules_id),
    CONSTRAINT fkemkhunj90917qgfdu9fb4c1v2 FOREIGN KEY (student_id)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fki7rf2n0cf9hahnwypocqnal0v FOREIGN KEY (modules_id)
        REFERENCES public.module (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
GO
ALTER TABLE public.student_modules
    OWNER to postgres;
GO