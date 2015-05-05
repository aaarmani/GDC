CREATE TABLE poi_type
(
  id integer NOT NULL,
  name character varying(16) NOT NULL,
  type integer NOT NULL,
  CONSTRAINT poi_type_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE poi_type
  OWNER TO teste;


CREATE TABLE video_type
(
  id integer NOT NULL,
  name character varying(16) NOT NULL,
  type integer NOT NULL,
  CONSTRAINT video_type_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE video_type
  OWNER TO teste;



INSERT INTO poi_type VALUES(1,'DEFAULT', 1);
INSERT INTO poi_type VALUES(2,'SCHOOL', 2);
INSERT INTO poi_type VALUES(3,'HOSPITAL', 3);
INSERT INTO poi_type VALUES(4,'FIREMAN', 4);
INSERT INTO poi_type VALUES(5,'POLICE', 5);
INSERT INTO poi_type VALUES(6,'MONUMENT', 6);
INSERT INTO poi_type VALUES(7,'SHOPPING', 7);


INSERT INTO video_type VALUES(1,'INSTITUTIONAL',1);
INSERT INTO video_type VALUES(2,'CONTENT',2);
INSERT INTO video_type VALUES(3,'ADVERTISEMENT',3);

