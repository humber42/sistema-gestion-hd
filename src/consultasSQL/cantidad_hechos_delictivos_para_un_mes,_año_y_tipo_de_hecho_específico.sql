--@author Henry Serra Morejón modified by Humberto Cabrera
--Metodo para obtener la cantidad de hechos en un mes o anno y un tipo de hecho especifico
CREATE OR REPLACE FUNCTION obtener_cant_hechos__mes_o_anno(anno double precision, mes integer, id_tipo_hecho integer)
  RETURNS devolver_hechos_uorg AS
$BODY$

DECLARE rec record;

BEGIN

  IF mes > 0
  THEN
    for rec in

    SELECT *
    FROM hechos
    WHERE date_part('year', hechos.fecha_ocurrencia) = $1
      and hechos.id_tipo_hecho = $3
      AND date_part('mons', hechos.fecha_ocurrencia) = $2
    ORDER BY id_reg
    LOOP
      return next rec;
    end loop;
    ;

  ELSE
    for rec in
    SELECT *
    FROM hechos
    WHERE date_part('year', hechos.fecha_ocurrencia) = $1
      and hechos.id_tipo_hecho = $3
    ORDER BY id_reg

    LOOP
      return next rec;
    end loop;
    ;

  END IF;

  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE