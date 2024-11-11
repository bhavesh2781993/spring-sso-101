CREATE TABLE public.users
(
    id        bigint,
    username  character varying(45),
    password  character varying(45),
    authority character varying(45),
    PRIMARY KEY (id)
);
