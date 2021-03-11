CREATE TYPE afectaciones AS (
  unidad                   character varying(255),
  cantidad_hechos          integer,
  cant_serv_afectados      integer,
  cant_estaciones_publicas integer
);
--LA HABANA--
CREATE OR REPLACE FUNCTION cant_hechos_habana_anno(anno    double precision, id_norte integer, id_sur integer,
                                                   id_este integer, id_oeste integer)
  RETURNS integer AS
$BODY$
DECLARE
  cant integer;
BEGIN

  SELECT INTO cant cant_hechos($1, $2) + cant_hechos($1, $3) + cant_hechos($1, $4) + cant_hechos($1, $5);

  RETURN cant;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;
ALTER FUNCTION cant_hechos_habana_anno(double precision, integer, integer, integer, integer)
  OWNER TO postgres;


CREATE OR REPLACE FUNCTION cant_serv_afectados_habana_anno(anno    double precision, id_norte integer, id_sur integer,
                                                           id_este integer, id_oeste integer)
  RETURNS integer AS
$BODY$
DECLARE
  cant integer;
BEGIN

  SELECT INTO cant cant_serv_afectados($1, $2) + cant_serv_afectados($1, $3) + cant_serv_afectados($1, $4) +
                   cant_serv_afectados($1, $5);

  RETURN cant;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;
ALTER FUNCTION cant_serv_afectados_habana_anno(double precision, integer, integer, integer, integer)
  OWNER TO postgres;


CREATE OR REPLACE FUNCTION obtener_telefonia_publica_habana_anno(anno   double precision, id_norte integer,
                                                                 id_sur integer, id_este integer, id_oeste integer)
  RETURNS SETOF afectaciones AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN

  SELECT unidades_organizativas.unidad_organizativa,
         cant_hechos_habana_anno($1, $2, $3, $4, $5),
         cant_serv_afectados_habana_anno($1, $2, $3, $4, $5),
         cant_estaciones_instaladas(unidades_organizativas.id_unidad_organizativa)
  FROM unidades_organizativas
         JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
  WHERE unidades_organizativas.unidad_organizativa LIKE 'DVLH'
    AND hechos.id_tipo_hecho = 2
  GROUP BY unidades_organizativas.unidad_organizativa, unidades_organizativas.id_unidad_organizativa

  LOOP

    RETURN NEXT rec;
  END LOOP;
  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100
ROWS 1000;
ALTER FUNCTION obtener_telefonia_publica_habana_anno(double precision, integer, integer, integer, integer)
  OWNER TO postgres;


--DEMÁS PROVINCIAS
CREATE OR REPLACE FUNCTION cant_estaciones_instaladas(id_unidad_organizativa integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant SUM(estacion_publica_centro_agente.estaciones_publicas)
  FROM estacion_publica_centro_agente
         JOIN unidades_organizativas
           ON unidades_organizativas.id_unidad_organizativa = estacion_publica_centro_agente.id_uorg
                AND unidades_organizativas.id_unidad_organizativa = $1;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;
ALTER FUNCTION cant_estaciones_instaladas(integer)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION cant_hechos(anno double precision, id_unidad_organizativa integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant COUNT(hechos.id_reg) AS cantidad_hechos
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.id_tipo_hecho = 2
    AND date_part('year', hechos.fecha_ocurrencia) = $1
    AND unidades_organizativas.id_unidad_organizativa = $2;

  IF cant IS NULL
  THEN
    cant = 0;
  END IF;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;
ALTER FUNCTION cant_hechos(double precision, integer)
  OWNER TO postgres;


CREATE OR REPLACE FUNCTION cant_serv_afectados(anno double precision, id_unidad_organizativa integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant SUM(hechos.afectacion_servicio)
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.id_tipo_hecho = 2
    AND date_part('year', hechos.fecha_ocurrencia) = $1
    AND unidades_organizativas.id_unidad_organizativa = $2;

  IF cant IS NULL
  THEN
    cant = 0;
  END IF;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;
ALTER FUNCTION cant_serv_afectados(double precision, integer)
  OWNER TO postgres;


CREATE OR REPLACE FUNCTION obtener_telefonia_publica_anno(anno double precision)
  RETURNS SETOF afectaciones AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN

  SELECT unidades_organizativas.unidad_organizativa,
         cant_hechos($1, unidades_organizativas.id_unidad_organizativa),
         cant_serv_afectados($1, unidades_organizativas.id_unidad_organizativa),
         cant_estaciones_instaladas(unidades_organizativas.id_unidad_organizativa)
  FROM unidades_organizativas
         JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
  WHERE unidades_organizativas.unidad_organizativa LIKE '%DT%'
    AND unidades_organizativas.unidad_organizativa NOT LIKE 'DTSR'
    AND unidades_organizativas.unidad_organizativa NOT LIKE 'DTNO'
    AND unidades_organizativas.unidad_organizativa NOT LIKE 'DTES'
    AND unidades_organizativas.unidad_organizativa NOT LIKE 'DTOE'
    AND hechos.id_tipo_hecho = 2
  GROUP BY unidades_organizativas.unidad_organizativa, unidades_organizativas.id_unidad_organizativa

  LOOP

    RETURN NEXT rec;
  END LOOP;
  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100
ROWS 1000;
ALTER FUNCTION obtener_telefonia_publica_anno(double precision)
  OWNER TO postgres;

