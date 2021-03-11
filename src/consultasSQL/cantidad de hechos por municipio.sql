CREATE TYPE municipios_hechos AS (
  municipio       character varying(255),
  cantidad_hechos bigint
);

CREATE OR REPLACE FUNCTION obtener_hechos_por_municipio(anno double precision, id_tipo_hecho integer)
  RETURNS SETOF municipios_hechos AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN
  SELECT municipios.municipio, COUNT(hechos.id_reg) as cantidad_hechos
  FROM hechos
         JOIN municipios ON municipios.id_municipio = hechos.id_municipio
  WHERE hechos.id_tipo_hecho = $2
    AND date_part('year', hechos.fecha_ocurrencia) = $1
  GROUP BY municipios.municipio
  ORDER BY COUNT(hechos.id_reg) DESC
  LOOP

    RETURN next rec;
  END LOOP;
  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100
ROWS 1000;

