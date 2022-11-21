--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

-- Started on 2022-11-16 16:30:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 16384)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 3333 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 210 (class 1259 OID 42370)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    first_name character varying(25),
    last_name character varying(25)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 42375)
-- Name: Users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Users_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 215 (class 1259 OID 42394)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    product_id bigint NOT NULL,
    date_of_by date NOT NULL
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 42393)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.orders ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 213 (class 1259 OID 42377)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    title character varying(25),
    price bigint
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 42376)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.products ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3327 (class 0 OID 42394)
-- Dependencies: 215
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (id, user_id, product_id, date_of_by) FROM stdin;
1	1	1	2021-04-20
8	1	5	2022-07-11
9	1	3	2022-07-15
10	2	1	2022-06-11
11	2	7	2022-06-11
12	2	10	2022-07-18
13	6	8	2022-09-01
14	6	6	2022-09-01
15	7	5	2022-11-11
16	3	2	2022-11-05
17	3	4	2022-06-12
18	3	10	2022-06-12
19	4	5	2022-04-01
20	4	4	2022-04-01
21	4	1	2022-05-09
\.


--
-- TOC entry 3325 (class 0 OID 42377)
-- Dependencies: 213
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products (id, title, price) FROM stdin;
1	Кофе	150
2	Молоко	50
3	Хлеб	30
4	Конфеты	120
5	Рыба	230
6	Колбаса	250
7	Масло	130
8	Сыр	280
9	Сметана	180
10	Печенье	75
11	Чай	46
12	Зефир	29
\.


--
-- TOC entry 3322 (class 0 OID 42370)
-- Dependencies: 210
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, first_name, last_name) FROM stdin;
1	Сергей	Сергеев
2	Петр	Петров
3	Иван	Иванов
4	Олег	Олегов
5	Игорь	Петров
6	Сергей	Алексеев
7	Иван	Сергеев
\.


--
-- TOC entry 3334 (class 0 OID 0)
-- Dependencies: 211
-- Name: Users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Users_id_seq"', 7, true);


--
-- TOC entry 3335 (class 0 OID 0)
-- Dependencies: 214
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 21, true);


--
-- TOC entry 3336 (class 0 OID 0)
-- Dependencies: 212
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 12, true);


--
-- TOC entry 3176 (class 2606 OID 42374)
-- Name: users Users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY (id);


--
-- TOC entry 3180 (class 2606 OID 42398)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 3178 (class 2606 OID 42381)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 3182 (class 2606 OID 42404)
-- Name: orders product_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT product_id FOREIGN KEY (product_id) REFERENCES public.products(id) NOT VALID;


--
-- TOC entry 3181 (class 2606 OID 42399)
-- Name: orders user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2022-11-16 16:30:45

--
-- PostgreSQL database dump complete
--

