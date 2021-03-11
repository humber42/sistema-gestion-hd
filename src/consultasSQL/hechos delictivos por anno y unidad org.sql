--@Henry Serra Morejon
--Metodo que realiza lo mismo, devuelve la cantidad de hechos delictivos pero ahora solo para la unidad_organizativa especificada 
CREATE OR REPLACE FUNCTION obtener_cant_hechos_delictivos_mes_anno_uorg(anno    double precision, mes integer,
                                                                        id_uorg integer)
  RETURNS SETOF cant_delitos_anno AS
$BODY$

DECLARE rec record;

BEGIN

  IF mes > 0
  THEN

    FOR rec IN

    SELECT DISTINCT unidad_organizativa,
                    cant_hechos_por_mes_uorg($2, $1, $3, 1) as pext,
                    cant_hechos_por_mes_uorg($2, $1, $3, 2) as tpub,
                    cant_hechos_por_mes_uorg($2, $1, $3, 3) as robo,
                    cant_hechos_por_mes_uorg($2, $1, $3, 4) as hurto,
                    cant_hechos_por_mes_uorg($2, $1, $3, 5) as fraude,
                    cant_hechos_por_mes_uorg($2, $1, $3, 7) as otros_delitos
    FROM unidades_organizativas
           JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
    WHERE date_part('year', hechos.fecha_ocurrencia) = $1
      AND date_part('mons', hechos.fecha_ocurrencia) = $2
      AND hechos.id_uorg = $3
    LOOP
      RETURN NEXT rec;
    END LOOP;

  ELSE
    FOR rec IN

    SELECT DISTINCT unidad_organizativa,
                    cant_hechos_uorg($1, $3, 1) as pext,
                    cant_hechos_uorg($1, $3, 2) as tpub,
                    cant_hechos_uorg($1, $3, 3) as robo,
                    cant_hechos_uorg($1, $3, 4) as hurto,
                    cant_hechos_uorg($1, $3, 5) as fraude,
                    cant_hechos_uorg($1, $3, 7) as otros_delitos
    FROM unidades_organizativas
           JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
    WHERE date_part('year', hechos.fecha_ocurrencia) = $1
      AND hechos.id_uorg = $3
    LOOP
      RETURN NEXT rec;
    END LOOP;

  END IF;

  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE