--@Henry Serra Morejon
--Type para devolver la cantidad de delitos en el rango de fecha (trimestre, semestre, anno)
CREATE TYPE cant_delitos_rango_fecha AS (
  mes      text,
  pext     integer,
  tpub     integer,
  robo     integer,
  hurto    integer,
  fraude   integer,
  accioncr integer,
  otros    integer
);

--Metodo que permite obtener la cantidad de hechos delictivos dado un rango de fecha determinado
CREATE OR REPLACE FUNCTION obtener_cant_hechos_delictivos_rango_fecha(fecha_inicio date, fecha_fin date)
  RETURNS SETOF cant_delitos_rango_fecha AS
$BODY$
DECLARE
  rec  record;
  anno double precision;

BEGIN

  anno = date_part('year', fecha_inicio);

  FOR rec IN

  SELECT DISTINCT CASE date_part('mons', hechos.fecha_ocurrencia)
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
                      END                                                                        AS mes,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), anno, 1) as pext,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), anno, 2) as tpub,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), anno, 3) as robo,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), anno, 4) as hurto,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), anno, 5) as fraude,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), anno, 6) as accionescr,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), anno, 7) as otros_delitos
  FROM hechos
  WHERE (hechos.fecha_ocurrencia BETWEEN $1 AND $2)
  GROUP BY mes, hechos.fecha_ocurrencia

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
ALTER FUNCTION obtener_cant_hechos_delictivos_rango_fecha(date, date)
  OWNER TO postgres;
