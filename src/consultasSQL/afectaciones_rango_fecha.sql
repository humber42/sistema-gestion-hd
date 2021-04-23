--@Henry Serra Morejon
--Type para obtener las afectaciones monetarias en un rango de fecha determinado
CREATE TYPE afectaciones_rango_fecha AS (
  usd            double precision,
  mn             double precision,
  serv_afectados double precision
);

--Metodo que permite obtener las afectaciones en un rango de fecha especificado que debe ser superior o igual a un trimestre
--SELECT * FROM obtener_afectaciones_rango_fecha('2020-01-01','2020-03-10',1), esta consulta debe lanzar excepcion porque el rango especificado
--no es un trimestre.
CREATE OR REPLACE FUNCTION obtener_afectaciones_rango_fecha(fecha_inicial date, fecha_final date, id_tipo_hecho integer)
  RETURNS SETOF afectaciones_rango_fecha AS
$BODY$

DECLARE
  rec        record;
  resta_anno double precision;
  resta_mes  double precision;

BEGIN

  resta_anno = date_part('year', $2) - date_part('year', $1);
  resta_mes = date_part('mons', $2) - date_part('mons', $1);

  IF (resta_anno = 0 AND resta_mes > 2)
  THEN

    FOR rec IN

    SELECT SUM(hechos.afectacion_usd)      as USD,
           SUM(hechos.afectacion_mn)       as MN,
           SUM(hechos.afectacion_servicio) as Servicios_afectados
    FROM hechos
    WHERE (hechos.fecha_ocurrencia BETWEEN $1 AND $2)
      AND hechos.id_tipo_hecho = $3

    LOOP
      RETURN NEXT rec;
    END LOOP;

  ELSIF resta_anno > 0
    THEN

      FOR rec IN

      SELECT SUM(hechos.afectacion_usd)      as USD,
             SUM(hechos.afectacion_mn)       as MN,
             SUM(hechos.afectacion_servicio) as Servicios_afectados
      FROM hechos
      WHERE (hechos.fecha_ocurrencia BETWEEN $1 AND $2)
        AND hechos.id_tipo_hecho = $3

      LOOP
        RETURN NEXT rec;
      END LOOP;

  ELSE

    raise exception 'El rango de fecha no puede ser inferior a un trimestre';

  END IF;

  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
  