CREATE TABLE public.clients
(
    id bigint,
    client_id character varying(45),
    secret character varying(45),
    scope character varying(45),
    auth_method character varying(45),
    grant_type character varying(45),
    redirect_uri character varying(200),
    PRIMARY KEY (id)
);