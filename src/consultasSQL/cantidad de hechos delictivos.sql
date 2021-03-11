--@Henry Serra Morejón

--Type para la funcionalidad de la cantidad de delitos
CREATE TYPE cant_delitos_anno AS (
  uorg   character varying(255),
  pext   integer,
  tpub   integer,
  robo   integer,
  hurto  integer,
  fraude integer,
  otros  integer
);

--@Henry Serra Morejón
--Método para la recuperación de la cantidad de hechos de una unidad organizativa en un año
CREATE OR REPLACE FUNCTION cant_hechos_uorg(anno          double precision, id_unidad_organizativa integer,
                                            id_tipo_hecho integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant COUNT(hechos.id_reg) AS cantidad_hechos
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.id_tipo_hecho = $3
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
ALTER FUNCTION cant_hechos_uorg(double precision, integer, integer)
  OWNER TO postgres;


--@Henry Serra Morejón
--Método para la recuperación de la cantidad de hechos (id de hecho pasado por parámetro), según el año y mes para una unidad organizativa en específico
CREATE OR REPLACE FUNCTION cant_hechos_por_mes_uorg(mes                    double precision, anno double precision,
                                                    id_unidad_organizativa integer, id_tipo_hecho integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant COUNT(hechos.id_reg) AS cantidad_hechos
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.id_tipo_hecho = $4
    AND date_part('mons', hechos.fecha_ocurrencia) = $1
    AND date_part('year', hechos.fecha_ocurrencia) = $2
    AND unidades_organizativas.id_unidad_organizativa = $3;

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
ALTER FUNCTION cant_hechos_por_mes_uorg(double precision, double precision, integer, integer)
  OWNER TO postgres;


--@Henry Serra Morejón
--Método para obtener la cantidad de hechos delictivos para un año y para un mes de un año en específico
--Funciona así....
--si la llamas de esta manera SELECT * FROM obtener_cant_hechos_delictivos_mes_anno(2019,0) te devuelve la cantidad total de cada UO para ese año
--si la llamas de esta otra SELET * FROM obtener_cant_hechos_delictivos_mes_anno(2019,1) te devuelve la cantidad para el mes de enero en ese año
CREATE OR REPLACE FUNCTION obtener_cant_hechos_delictivos_mes_anno(anno double precision, mes integer)
  RETURNS SETOF cant_delitos_anno AS
$BODY$

DECLARE rec record;

BEGIN

  IF mes > 0
  THEN

    FOR rec IN

    SELECT DISTINCT unidad_organizativa,
                    cant_hechos_por_mes_uorg($2, $1, id_unidad_organizativa, 1) as pext,
                    cant_hechos_por_mes_uorg($2, $1, id_unidad_organizativa, 2) as tpub,
                    cant_hechos_por_mes_uorg($2, $1, id_unidad_organizativa, 3) as robo,
                    cant_hechos_por_mes_uorg($2, $1, id_unidad_organizativa, 4) as hurto,
                    cant_hechos_por_mes_uorg($2, $1, id_unidad_organizativa, 5) as fraude,
                    cant_hechos_por_mes_uorg($2, $1, id_unidad_organizativa, 7) as otros_delitos
    FROM unidades_organizativas
           JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
    WHERE date_part('year', hechos.fecha_ocurrencia) = $1
      AND date_part('mons', hechos.fecha_ocurrencia) = $2
    LOOP
      RETURN NEXT rec;
    END LOOP;

  ELSE
    FOR rec IN

    SELECT DISTINCT unidad_organizativa,
                    cant_hechos_uorg($1, id_unidad_organizativa, 1) as pext,
                    cant_hechos_uorg($1, id_unidad_organizativa, 2) as tpub,
                    cant_hechos_uorg($1, id_unidad_organizativa, 3) as robo,
                    cant_hechos_uorg($1, id_unidad_organizativa, 4) as hurto,
                    cant_hechos_uorg($1, id_unidad_organizativa, 5) as fraude,
                    cant_hechos_uorg($1, id_unidad_organizativa, 7) as otros_delitos
    FROM unidades_organizativas
           JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
    WHERE date_part('year', hechos.fecha_ocurrencia) = $1
    LOOP
      RETURN NEXT rec;
    END LOOP;

  END IF;

  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
