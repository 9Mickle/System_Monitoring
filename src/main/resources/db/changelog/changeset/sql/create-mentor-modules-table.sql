CREATE TABLE public.mentor_modules
(
    mentor_id bigint NOT NULL,
    modules_id bigint NOT NULL,
    CONSTRAINT uk_aito1uo8rs55uh1f5omvvx4xr UNIQUE (modules_id),
    CONSTRAINT fk54bjjv7lqvprjg0nhhyom98ig FOREIGN KEY (modules_id)
        REFERENCES public.module (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkirkuntobqojiag57y2ggyq546 FOREIGN KEY (mentor_id)
        REFERENCES public.mentor (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
GO
ALTER TABLE public.mentor_modules
    OWNER to postgres;
GO