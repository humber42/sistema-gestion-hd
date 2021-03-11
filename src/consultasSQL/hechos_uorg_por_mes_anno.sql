---Created By Henry A. Serra---


CREATE TYPE hechos_uorg_mes_anno AS (
  date_part   double precision,
  unidad      character varying(255),
  mes         text,
  cant_hechos integer
);

--este metodo devuelve la cantidad de hechos (del tipo especificado) de una unidad organizativa en un mes y anno dado
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


--metodo que devuelve la cantidad de hechos por mes para todas las unidades organizativas  
CREATE OR REPLACE FUNCTION obtener_cantidad_hechos_uorg_por_mes(anno double precision, id_tipo_hecho integer)
  RETURNS SETOF hechos_uorg_mes_anno AS
$BODY$
DECLARE rec record;
BEGIN

  FOR rec IN

  SELECT DISTINCT date_part('mons', hechos.fecha_ocurrencia),
                  unidad_organizativa,
                  CASE date_part('mons', hechos.fecha_ocurrencia)
                    WHEN 1 THEN 'Enero'
                    WHEN 2 THEN 'Febrero'
                    WHEN 3 THEN 'Marzo'
                    WHEN 4 THEN 'Abril'
                    WHEN 5 THEN 'Mayo'
                    WHEN 6 THEN 'Junio'
                    WHEN 7 THEN 'Julio'
                    WHEN 8 THEN 'Agosto'
                    WHEN 9 THEN 'Septiembre'
                    WHEN 10 THEN 'Octubre'
                    WHEN 11 THEN 'Noviembre'
                    WHEN 12 THEN 'Diciembre'
                      END                      AS mes,
                  cant_hechos_por_mes_uorg(date_part('mons', hechos.fecha_ocurrencia), $1, id_unidad_organizativa,
                                           $2) as pext
  FROM unidades_organizativas
         JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
  WHERE unidad_organizativa LIKE '%DT%'
    AND date_part('year', hechos.fecha_ocurrencia) = $1
  ORDER BY unidad_organizativa, date_part('mons', hechos.fecha_ocurrencia)


  LOOP

    RETURN NEXT rec;
  END LOOP;
  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE

