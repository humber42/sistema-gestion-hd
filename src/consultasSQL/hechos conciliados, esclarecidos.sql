CREATE TYPE esclarecimiento_hechos AS (
  direccion_territorial character varying(255),
  total_conciliados     int,
  cant_pext             int,
  cant_tpub             int
);


CREATE OR REPLACE FUNCTION cant_parte1(anno double precision, id_tipo_hecho integer, id_unidad_organizativa integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN
  SELECT INTO cant COUNT(hechos.id_reg) AS cant_pext
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.numero_denuncia IS NOT NULL
    AND unidades_organizativas.id_unidad_organizativa = $3
    AND hechos.id_tipo_hecho = $2
    AND date_part('year', hechos.fecha_ocurrencia) = $1;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;


CREATE OR REPLACE FUNCTION obtener_total_conciliados_anho(anno double precision)
  RETURNS SETOF esclarecimiento_hechos AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN
  SELECT unidades_organizativas.unidad_organizativa,
         cant_parte1($1, 1, unidades_organizativas.id_unidad_organizativa) +
         cant_parte1($1, 2, unidades_organizativas.id_unidad_organizativa) as total_conciliados,
         cant_parte1($1, 1, unidades_organizativas.id_unidad_organizativa) as pext,
         cant_parte1($1, 2, unidades_organizativas.id_unidad_organizativa) as tpub
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.numero_denuncia IS NOT NULL
    AND date_part('year', hechos.fecha_ocurrencia) = $1
    AND unidades_organizativas.unidad_organizativa LIKE '%DT%'
  GROUP BY unidades_organizativas.unidad_organizativa, unidades_organizativas.id_unidad_organizativa
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


CREATE OR REPLACE FUNCTION cant_parte2_3(anno                   double precision, id_tipo_hecho integer,
                                         id_unidad_organizativa integer, esclarecido boolean)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN
  SELECT INTO cant COUNT(hechos.id_reg) AS cant_pext
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.numero_denuncia IS NOT NULL
    AND unidades_organizativas.id_unidad_organizativa = $3
    AND hechos.id_tipo_hecho = $2
    AND date_part('year', hechos.fecha_ocurrencia) = $1
    AND hechos.esclarecido = $4;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql
VOLATILE;


CREATE OR REPLACE FUNCTION obtener_total_hechos_esclarecidos_o_no_anho(anno double precision, esclarecido boolean)
  RETURNS SETOF esclarecimiento_hechos AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN
  SELECT unidades_organizativas.unidad_organizativa,
         cant_parte2_3($1, 1, unidades_organizativas.id_unidad_organizativa, $2) +
         cant_parte2_3($1, 2, unidades_organizativas.id_unidad_organizativa, $2) as total_conciliados,
         cant_parte2_3($1, 1, unidades_organizativas.id_unidad_organizativa, $2) as pext,
         cant_parte2_3($1, 2, unidades_organizativas.id_unidad_organizativa, $2) as tpub
  FROM hechos
         JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
  WHERE hechos.numero_denuncia IS NOT NULL
    AND date_part('year', hechos.fecha_ocurrencia) = $1
    AND unidades_organizativas.unidad_organizativa LIKE '%DT%'
    AND hechos.esclarecido = $2
  GROUP BY unidades_organizativas.unidad_organizativa, unidades_organizativas.id_unidad_organizativa
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


  
  
  
