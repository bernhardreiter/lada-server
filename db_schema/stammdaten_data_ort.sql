\set ON_ERROR_STOP on

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.0

-- Started on 2016-03-31 11:38:13

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = stammdaten, pg_catalog;


--
-- TOC entry 4704 (class 0 OID 535789)
-- Dependencies: 270
-- Data for Name: ort; Type: TABLE DATA; Schema: stammdaten; Owner: lada
--

COPY ort (id, netzbetreiber_id, ort_id, langtext, staat_id, gem_id, unscharf, nuts_code, kda_id, koord_x_extern, koord_y_extern, hoehe_land, letzte_aenderung, geom, shape, ort_typ, kurztext, berichtstext, zone, sektor, zustaendigkeit, mp_art, aktiv, anlage_id, oz_id) FROM stdin;
19	D	T060014	WW  Kassel	0	06611000	0	DE731	5	32531152	5684269	\N	2013-08-12 07:57:26.332873	0101000020E6100000DB334B02D4E42240BA313D6189A74940	\N	1	T060014	\N	\N	\N	\N	\N	\N	\N	\N
33	D	T110001	WW  Beelitzhof, Berlin	0	11000000	0	DE300	5	33390988	5821608	\N	2013-08-12 07:57:26.332873	0101000020E61000000C1F115322C92A4031EBC5504E444A40	\N	1	T110001	\N	\N	\N	\N	\N	\N	\N	\N
51	D	T060005	WW  Eschollbruecken	0	06432018	0	DE716	5	32470178	5516260	\N	2013-08-12 07:57:26.332873	0101000020E6100000CF6BEC12D52B214052499D8026E64840	\N	1	T060005	\N	\N	\N	\N	\N	\N	\N	\N
61	D	M060003	(MVA)  Kassel	0	06611000	0	DE731	5	32531152	5684269	\N	2013-08-12 07:57:26.332873	0101000020E6100000DB334B02D4E42240BA313D6189A74940	\N	1	M060003	\N	\N	\N	\N	\N	\N	\N	\N
64	D	FKI0005	Kinzig; Hanau; PE-Stelle kurz vor Mündung in Main	0	06435014	0	DE719	5	32493851	5551177	\N	2013-08-12 07:57:26.332873	0101000020E61000008716D9CEF7D32140B554DE8E700E4940	\N	1	FKI0005	\N	\N	\N	\N	\N	\N	\N	\N
85	D	FSD0005	Schwarzbach/Hessen; Trebur; PE-Ort Trebur-Astheim	0	06433014	0	DE717	5	32456065	5529415	\N	2013-08-12 07:57:26.332873	0101000020E6100000D8BB3FDEABC6204077BE9F1A2FF54840	\N	1	FSD0005	\N	\N	\N	\N	\N	\N	\N	\N
140	D	FSP0092	Spree km 9.20; PE-Stelle Nr. 11002, Einmündung Landwehrkanal	0	11000000	0	DE300	5	33390988	5821608	\N	2013-08-12 07:57:26.332873	0101000020E61000000C1F115322C92A4031EBC5504E444A40	\N	1	FSP0092	\N	\N	\N	\N	\N	\N	\N	\N
190	06	D_ 00190	Reinheim	0	06432019	0	DE716	5	32487017	5519769	\N	2013-08-12 07:57:26.332873	0101000020E6100000C173EFE192A32140F5673F5244EA4840	\N	1	D_ 00190	\N	\N	\N	\N	\N	\N	\N	\N
156	D	T060020	WW  Wiesbaden-Schierstein	0	06414000	0	DE714	5	32447434	5548087	\N	2013-08-12 07:57:26.332873	0101000020E6100000B9DFA128D0872040D15CA791960A4940	\N	1	T060020	\N	\N	\N	\N	\N	\N	\N	\N
165	D	SMG1101	Müggelsee; Berlin; PE-Stelle Nr. 41035	0	11000000	0	DE300	5	33390988	5821608	\N	2013-08-12 07:57:26.332873	0101000020E61000000C1F115322C92A4031EBC5504E444A40	\N	1	SMG1101	\N	\N	\N	\N	\N	\N	\N	\N
168	D	D060003	Uttershausen	0	06634025	0	DE735	5	32526698	5659112	\N	2013-08-12 07:57:26.332873	0101000020E610000033164D6727C322402D95B7239C8A4940	\N	1	D060003	\N	\N	\N	\N	\N	\N	\N	\N
173	D	K060022	KLA Wiesbaden	0	06414000	0	DE714	5	32447434	5548087	\N	2013-08-12 07:57:26.332873	0101000020E6100000B9DFA128D0872040D15CA791960A4940	\N	1	K060022	\N	\N	\N	\N	\N	\N	\N	\N
198	D	M060001	(SEVA) Frankfurt/M. Sindlingen	0	06412000	0	DE712	5	32475476	5550676	\N	2013-08-12 07:57:26.332873	0101000020E6100000E353008C675021408BC3995FCD0D4940	\N	1	M060001	\N	\N	\N	\N	\N	\N	\N	\N
218	D	SMG1100	Müggelsee	0	11000000	0	DE300	5	33390988	5821608	\N	2013-08-12 07:57:26.332873	0101000020E61000000C1F115322C92A4031EBC5504E444A40	\N	1	SMG1100	\N	\N	\N	\N	\N	\N	\N	\N
223	D	T110015	Grundwasser Tempelhof ( Ringbahnstraße )	0	11000000	0	DE300	5	33390988	5821608	\N	2013-08-12 07:57:26.332873	0101000020E61000000C1F115322C92A4031EBC5504E444A40	\N	1	T110015	\N	\N	\N	\N	\N	\N	\N	\N
4	06	D_ 00004	Wabern	0	06634025	0	DE735	5	32526698	5659112	\N	2013-08-12 07:57:26.332873	0101000020E610000033164D6727C322402D95B7239C8A4940	\N	1	D_ 00004	\N	\N	\N	\N	\N	\N	\N	\N
5	06	D_ 00005	Hanau	0	06435014	0	DE719	5	32493851	5551177	\N	2013-08-12 07:57:26.332873	0101000020E61000008716D9CEF7D32140B554DE8E700E4940	\N	1	D_ 00005	\N	\N	\N	\N	\N	\N	\N	\N
12	06	D_ 00012	Wiesbaden	0	06414000	0	DE714	5	32447434	5548087	\N	2013-08-12 07:57:26.332873	0101000020E6100000B9DFA128D0872040D15CA791960A4940	\N	1	D_ 00012	\N	\N	\N	\N	\N	\N	\N	\N
23	06	D_ 00023	Melsungen	0	06634014	0	DE735	5	32541043	5665935	\N	2013-08-12 07:57:26.332873	0101000020E610000078D15790662C23408A1F63EE5A924940	\N	1	D_ 00023	\N	\N	\N	\N	\N	\N	\N	\N
26	06	D_ 00026	Trebur	0	06433014	0	DE717	5	32456065	5529415	\N	2013-08-12 07:57:26.332873	0101000020E6100000D8BB3FDEABC6204077BE9F1A2FF54840	\N	1	D_ 00026	\N	\N	\N	\N	\N	\N	\N	\N
29	06	D_ 00029	Frankfurt am Main	0	06412000	0	DE712	5	32475476	5550676	\N	2013-08-12 07:57:26.332873	0101000020E6100000E353008C675021408BC3995FCD0D4940	\N	1	D_ 00029	\N	\N	\N	\N	\N	\N	\N	\N
42	12	D_ 00042	Uebigau-Wahrenbrück	0	12062500	0	DE407	5	33383584	5715754	\N	2013-08-12 07:57:26.332873	0101000020E6100000A4367172BFA32A404A9869FB57CA4940	\N	1	D_ 00042	\N	\N	\N	\N	\N	\N	\N	\N
68	06	D_ 00068	Friedberg (Hessen)	0	06440008	0	DE71E	5	32484822	5574572	\N	2013-08-12 07:57:26.332873	0101000020E6100000344B02D4D4922140857CD0B359294940	\N	1	D_ 00068	\N	\N	\N	\N	\N	\N	\N	\N
69	12	D_ 00069	Karstädt	0	12070173	0	DE40F	5	32681930	5894394	\N	2013-08-12 07:57:26.332873	0101000020E6100000361FD7868A712740FD9FC37C79954A40	\N	1	D_ 00069	\N	\N	\N	\N	\N	\N	\N	\N
74	06	D_ 00074	Königstein im Taunus	0	06434005	0	DE718	5	32461213	5559884	\N	2013-08-12 07:57:26.332873	0101000020E61000004417D4B7CCE920400E15E3FC4D184940	\N	1	D_ 00074	\N	\N	\N	\N	\N	\N	\N	\N
76	06	D_ 00076	Altenstadt	0	06440001	0	DE71E	5	32496205	5570655	\N	2013-08-12 07:57:26.332873	0101000020E6100000BF4868CBB9E42140E275FD82DD244940	\N	1	D_ 00076	\N	\N	\N	\N	\N	\N	\N	\N
86	11	D_ 00086	Berlin	0	11000000	0	DE300	5	33390988	5821608	\N	2013-08-12 07:57:26.332873	0101000020E61000000C1F115322C92A4031EBC5504E444A40	\N	1	D_ 00086	\N	\N	\N	\N	\N	\N	\N	\N
87	06	D_ 00087	Kassel	0	06611000	0	DE731	5	32531152	5684269	\N	2013-08-12 07:57:26.332873	0101000020E6100000DB334B02D4E42240BA313D6189A74940	\N	1	D_ 00087	\N	\N	\N	\N	\N	\N	\N	\N
97	12	D_ 00097	Frankfurt (Oder)	0	12053000	0	DE403	5	33465204	5796318	\N	2013-08-12 07:57:26.332873	0101000020E6100000ED2AA4FCA4FA2C40DC4B1AA375284A40	\N	1	D_ 00097	\N	\N	\N	\N	\N	\N	\N	\N
102	06	D_ 00102	Körle	0	06634012	0	DE735	5	32536270	5669258	\N	2013-08-12 07:57:26.332873	0101000020E61000000B5EF415A4092340F54A598638964940	\N	1	D_ 00102	\N	\N	\N	\N	\N	\N	\N	\N
127	06	D_ 00127	Weilburg	0	06533017	0	DE723	5	32447433	5593044	\N	2013-08-12 07:57:26.332873	0101000020E61000006AFB57569A842040BC3FDEAB563E4940	\N	1	D_ 00127	\N	\N	\N	\N	\N	\N	\N	\N
129	06	D_ 00129	Calden	0	06633005	0	DE734	5	32522395	5698475	\N	2013-08-12 07:57:26.332873	0101000020E61000004D327216F6A4224079AF5A99F0B74940	\N	1	D_ 00129	\N	\N	\N	\N	\N	\N	\N	\N
131	06	D_ 00131	Witzenhausen	0	06636016	0	DE737	5	32558542	5688717	\N	2013-08-12 07:57:26.332873	0101000020E6100000E657738060AE23401C9947FE60AC4940	\N	1	D_ 00131	\N	\N	\N	\N	\N	\N	\N	\N
138	06	D_ 00138	Bad Schwalbach	0	06439002	0	DE71D	5	32432747	5554830	\N	2013-08-12 07:57:26.332873	0101000020E6100000035B25581C1E20406EFAB31F29124940	\N	1	D_ 00138	\N	\N	\N	\N	\N	\N	\N	\N
161	06	D_ 00161	Pfungstadt	0	06432018	0	DE716	5	32470178	5516260	\N	2013-08-12 07:57:26.332873	0101000020E6100000CF6BEC12D52B214052499D8026E64840	\N	1	D_ 00161	\N	\N	\N	\N	\N	\N	\N	\N
162	06	D_ 00162	Fulda	0	06631009	0	DE732	5	32546674	5604269	\N	2013-08-12 07:57:26.332873	0101000020E6100000363CBD5296512340888043A8524B4940	\N	1	D_ 00162	\N	\N	\N	\N	\N	\N	\N	\N
\.
