--el campo date_part lo puse para luego poner el order by por el entero que devuelve la funcion date_part al obtener el mes,
--porque si le dices que ordene por mes que fue como le puse al case, el lo ordena alfabeticamente
--ya despues tu mapeas lo que te interesa, o sea all menos el date_part ese que esta en el type y ya queda empingao
CREATE TYPE hechos_mes_anno AS (
  date_part     double precision,
  mes           text,
  pext          integer,
  tpub          integer,
  robo          integer,
  hurto         integer,
  fraude        integer,
  otros_delitos integer
);


--la funcion cant_hechos_pext_mes_anno ya yo la habia hecho anteriormente y la tienes en la bd que en la update que me diste
--esta implementado el metodo
CREATE OR REPLACE FUNCTION obtener_hechos_mes_anno(anno double precision)
  RETURNS SETOF hechos_mes_anno AS
$BODY$
DECLARE rec record;
BEGIN

  FOR rec IN

  SELECT DISTINCT date_part('mons', hechos.fecha_ocurrencia),
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
                      END                                                                      AS mes,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), $1, 1) as pext,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), $1, 2) as tpub,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), $1, 3) as robo,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), $1, 4) as hurto,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), $1, 5) as fraude,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia), $1, 7) as otros_delitos
  FROM hechos
  WHERE date_part('year', fecha_ocurrencia) = $1
    AND hechos.id_tipo_hecho = 1
  GROUP BY mes, hechos.fecha_ocurrencia
  ORDER BY date_part('mons', hechos.fecha_ocurrencia)

  LOOP

    RETURN NEXT rec;
  END LOOP;
  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE