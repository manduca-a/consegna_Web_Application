PGDMP                            |            postgres    14.10    15.3                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    13754    postgres    DATABASE     {   CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';
    DROP DATABASE postgres;
                postgres    false                       0    0    DATABASE postgres    COMMENT     N   COMMENT ON DATABASE postgres IS 'default administrative connection database';
                   postgres    false    3334                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                postgres    false                       0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   postgres    false    5            	           0    0    SCHEMA public    ACL     Q   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;
                   postgres    false    5            �            1255    16514    set_default_scaricato()    FUNCTION     �   CREATE FUNCTION public.set_default_scaricato() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.scaricato := FALSE;
    RETURN NEW;
END;
$$;
 .   DROP FUNCTION public.set_default_scaricato();
       public          postgres    false    5            �            1259    16542 	   documenti    TABLE     c  CREATE TABLE public.documenti (
    id integer NOT NULL,
    data_emissione date,
    data_scadenza date,
    descrizione character varying,
    scaricato boolean NOT NULL,
    tipologia character varying,
    utente_id bigint,
    file_path character varying,
    file_content oid,
    file_name character varying,
    relative_path character varying
);
    DROP TABLE public.documenti;
       public         heap    postgres    false    5            �            1259    16563 	   documento    TABLE     e  CREATE TABLE public.documento (
    id bigint NOT NULL,
    data_emissione date,
    data_scadenza date,
    descrizione character varying(255),
    file_name character varying(255),
    file_path character varying(255),
    relative_path character varying(255),
    scaricato boolean NOT NULL,
    tipologia character varying(255),
    utente_id bigint
);
    DROP TABLE public.documento;
       public         heap    postgres    false    5            �            1259    16404    documento_seq    SEQUENCE     w   CREATE SEQUENCE public.documento_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.documento_seq;
       public          postgres    false    5            �            1259    16541    prova_id_seq    SEQUENCE     �   CREATE SEQUENCE public.prova_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.prova_id_seq;
       public          postgres    false    5    214            
           0    0    prova_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.prova_id_seq OWNED BY public.documenti.id;
          public          postgres    false    213            �            1259    16463    utente    TABLE     e  CREATE TABLE public.utente (
    id bigint NOT NULL,
    amministratore boolean NOT NULL,
    archiviato boolean NOT NULL,
    cognome character varying(255),
    data_di_nascita date,
    email character varying(255),
    formatore boolean NOT NULL,
    nome character varying(255),
    password character varying(255),
    token character varying(255)
);
    DROP TABLE public.utente;
       public         heap    postgres    false    5            �            1259    16405 
   utente_seq    SEQUENCE     t   CREATE SEQUENCE public.utente_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 !   DROP SEQUENCE public.utente_seq;
       public          postgres    false    5            h           2604    16545    documenti id    DEFAULT     h   ALTER TABLE ONLY public.documenti ALTER COLUMN id SET DEFAULT nextval('public.prova_id_seq'::regclass);
 ;   ALTER TABLE public.documenti ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    213    214    214            �          0    16542 	   documenti 
   TABLE DATA           �   COPY public.documenti (id, data_emissione, data_scadenza, descrizione, scaricato, tipologia, utente_id, file_path, file_content, file_name, relative_path) FROM stdin;
    public          postgres    false    214   J                  0    16563 	   documento 
   TABLE DATA           �   COPY public.documento (id, data_emissione, data_scadenza, descrizione, file_name, file_path, relative_path, scaricato, tipologia, utente_id) FROM stdin;
    public          postgres    false    215   n#       �          0    16463    utente 
   TABLE DATA           �   COPY public.utente (id, amministratore, archiviato, cognome, data_di_nascita, email, formatore, nome, password, token) FROM stdin;
    public          postgres    false    212   �#                  0    0    documento_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.documento_seq', 1851, true);
          public          postgres    false    210                       0    0    prova_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.prova_id_seq', 14, true);
          public          postgres    false    213                       0    0 
   utente_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.utente_seq', 1851, true);
          public          postgres    false    211            n           2606    16569    documento documento_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.documento
    ADD CONSTRAINT documento_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.documento DROP CONSTRAINT documento_pkey;
       public            postgres    false    215            l           2606    16559    documenti prova_pk 
   CONSTRAINT     P   ALTER TABLE ONLY public.documenti
    ADD CONSTRAINT prova_pk PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.documenti DROP CONSTRAINT prova_pk;
       public            postgres    false    214            j           2606    16525    utente utente_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    212            o           2606    16570 %   documento fk24uwyiace5y7mv2dgw20k13v9    FK CONSTRAINT     �   ALTER TABLE ONLY public.documento
    ADD CONSTRAINT fk24uwyiace5y7mv2dgw20k13v9 FOREIGN KEY (utente_id) REFERENCES public.utente(id);
 O   ALTER TABLE ONLY public.documento DROP CONSTRAINT fk24uwyiace5y7mv2dgw20k13v9;
       public          postgres    false    3178    212    215            �   B   1	2024-01-15	2024-02-14	u	f	ATTESTATO FORMAZIONE	1752	\N	\N	\N	\N
 G   2	2024-01-15	2024-02-14	òpoà	f	ATTESTATO FORMAZIONE	1752	\N	\N	\N	\N
 6   3	2024-01-15	2024-02-14	8u	f	DOC. ID.	252	\N	\N	\N	\N
 3   4	2024-01-15	2024-02-14	a	f	NOMINE	252	\N	\N	\N	\N
 L   6	2024-01-15	2024-02-14	h	f	DOC. ID.	1752	\N	\N	\N	utente_1752/due_date.pdf
 W   9	2024-01-15	2024-02-14	sd	f	ATTESTATO FORMAZIONE	252	\N	\N	\N	utente_252/due_date.pdf
 �   10	2024-01-15	2024-02-14	0	f	ATTESTATO FORMAZIONE	1752	C:\\Users\\alex2\\Desktop\\documenti\\immaginiProvvisorie\\utente_1752\\due_date.pdf	\N	\N	utente_1752/due_date.pdf
 �   11	2024-01-15	2024-02-14	yy	f	DOC. ID.	252	C:\\Users\\alex2\\Desktop\\documenti\\immaginiProvvisorie\\utente_252\\due_date.pdf	\N	\N	utente_252/due_date.pdf
 �   12	2024-01-15	2024-02-14	e	f	DOC. ID.	252	C:\\Users\\alex2\\Desktop\\documenti\\utente_252\\due_date.pdf	\N	due_date.pdf	utente_252/due_date.pdf
 �   13	2024-01-15	2024-02-14	y	f	NOMINE	252	C:\\Users\\alex2\\Desktop\\documenti\\utente_252\\due_date.pdf	\N	due_date.pdf	utente_252/due_date.pdf
 �   14	2024-01-15	2024-02-14	le date le prende in automatica dalla scannerizzazione del file	f	ATTESTATO FORMAZIONE	152	C:\\Users\\alex2\\Desktop\\documenti\\utente_152\\due_date.pdf	\N	due_date.pdf	utente_152/due_date.pdf
    \.


             \.


      �   M   152	t	f	Cognome_Admin	2020-03-08	example@tuodominio.it	f	Nome_Admin	Admin	\N
 ?   352	f	f	Cognome_Dip	\N	dipendente@example.it	f	Nome_Dip	Dip	\N
 J   552	f	f	Cognome_Form	2024-01-01	formatore@example.com	t	Nome_Form	Form	\N
 H   1752	f	f	Sturni	2000-02-09	alessiosturniolo2901@gm	f	Alessio	alessio	\N
 e   252	f	f	Sturniolo	\N	alessiosturniolo2901@gmail.com	f	Alessio	1	2a129166-31ba-4d45-a1e4-aa91962d237c
    \.


     