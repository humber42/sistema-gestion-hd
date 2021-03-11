CREATE TYPE hechos_uorg_anno AS (
  unidad character varying(255),
  pext   integer,
  tpub   integer
);

CREATE OR REPLACE FUNCTION cant_hechos_pext_uorg_hastacierre(tiempo    date, annoanterior character varying,
                                                             tipohecho integer, id_uorg integer)
  RETURNS integer AS
$BODY$
DECLARE cant INTEGER;
BEGIN
  SELECT INTO cant COUNT(id_reg)
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.id_tipo_hecho = $3
    AND fecha_ocurrencia <= $1
    AND to_date(to_char(fecha_ocurrencia, 'YYYY-MM-DD'), 'YYYY-MM-DD') >= to_timestamp($2, 'YYYY-MM-DD')
    AND unidades_organizativas.id_unidad_organizativa = $4;

  RETURN cant;
end;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;
ALTER FUNCTION cant_hechos_pext_uorg_hastacierre(date, character varying, integer, integer)
  OWNER TO postgres;


CREATE OR REPLACE FUNCTION obtener_hechos_uorg_hasta_fecha(fecha date, anno character varying)
  RETURNS SETOF hechos_uorg_anno AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN

  SELECT unidades_organizativas.unidad_organizativa,
         cant_hechos_pext_uorg_hastacierre($1, $2, 1, id_unidad_organizativa) as pext,
         cant_hechos_pext_uorg_hastacierre($1, $2, 2, id_unidad_organizativa) as tpub
  FROM unidades_organizativas
         JOIN hechos ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
  WHERE unidades_organizativas.unidad_organizativa LIKE '%DT%'
  GROUP BY unidades_organizativas.unidad_organizativa, id_unidad_organizativa

  LOOP

    RETURN NEXT rec;
  END LOOP;
  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE
