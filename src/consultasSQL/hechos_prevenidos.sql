--Todo creado por @Henry A. Serra Morejon
--Type para devolver los hechos prevenidos en un anno determinado
CREATE TYPE hechos_prevenidos AS (
  uorg                   character varying(255),
  cant_hechos_delictivos integer,
  cant_pext              integer,
  cant_tpub              integer,
  cant_robos             integer,
  cant_hurtos            integer,
  cant_fraudes           integer,
  cant_accionescr        integer,
  cant_otros             integer
);

--Metodo que permite obtener la cantidad de hechos segun el anno, la unidad organizativa, el id del hecho y si es prevenido o no
CREATE OR REPLACE FUNCTION cant_hechos_uorg_prevenidos_o_no(anno                   double precision,
                                                            id_unidad_organizativa integer, id_tipo_hecho integer,
                                                            prevenido              boolean)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant COUNT(hechos.id_reg) AS cantidad_hechos
  FROM hechos
  WHERE hechos.id_tipo_hecho = $3
    AND date_part('year', hechos.fecha_ocurrencia) = $1
    AND hechos.id_uorg = $2
    AND hechos.prevenido = $4;

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
ALTER FUNCTION cant_hechos_uorg_prevenidos_o_no(double precision, integer, integer, boolean)
  OWNER TO postgres;


--Metodo que permite obtener los hechos delictivos no prevenidos de una unidad organizativa para un anno determinado
CREATE OR REPLACE FUNCTION cant_hechos_delictivos_no_prevenidos(anno double precision, id_unidad_organizativa integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant (cant_hechos_uorg_prevenidos_o_no($1, $2, 1, false) +
                    cant_hechos_uorg_prevenidos_o_no($1, $2, 2, false) +
                    cant_hechos_uorg_prevenidos_o_no($1, $2, 3, false) +
                    cant_hechos_uorg_prevenidos_o_no($1, $2, 4, false) +
                    cant_hechos_uorg_prevenidos_o_no($1, $2, 5, false) +
                    cant_hechos_uorg_prevenidos_o_no($1, $2, 6, false) +
                    cant_hechos_uorg_prevenidos_o_no($1, $2, 7, false)) AS cantidad_hechos;


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
ALTER FUNCTION cant_hechos_delictivos_no_prevenidos(double precision, integer)
  OWNER TO postgres;

--Metodo que permite obtener el reporte de los hechos prevenidos en un anno determinado
CREATE OR REPLACE FUNCTION obtener_cantidad_hechos_prevenidos(anno double precision)
  RETURNS SETOF hechos_prevenidos AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN

  SELECT unidad_organizativa,
         cant_hechos_delictivos_no_prevenidos($1, id_unidad_organizativa),
         cant_hechos_uorg_prevenidos_o_no($1, id_unidad_organizativa, 1, true),
         cant_hechos_uorg_prevenidos_o_no($1, id_unidad_organizativa, 2, true),
         cant_hechos_uorg_prevenidos_o_no($1, id_unidad_organizativa, 3, true),
         cant_hechos_uorg_prevenidos_o_no($1, id_unidad_organizativa, 4, true),
         cant_hechos_uorg_prevenidos_o_no($1, id_unidad_organizativa, 5, true),
         cant_hechos_uorg_prevenidos_o_no($1, id_unidad_organizativa, 6, true),
         cant_hechos_uorg_prevenidos_o_no($1, id_unidad_organizativa, 7, true)
  FROM unidades_organizativas

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
ALTER FUNCTION obtener_cantidad_hechos_prevenidos(double precision)
  OWNER TO postgres;
