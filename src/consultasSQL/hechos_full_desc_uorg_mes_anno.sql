--@Henry Serra Morejon
--TYPE que se corresponde con la tabla hechos
CREATE TYPE devolver_hechos_uorg AS (
  id_reg                          integer,
  titulo                          character varying(255),
  id_tipo_hecho                   integer,
  fecha_ocurrencia                date,
  fecha_parte                     date,
  numero_parte                    character varying(255),
  id_uorg                         integer,
  centro                          character varying(255),
  lugar                           character varying(255),
  id_municipio                    integer,
  numero_denuncia                 character varying(255),
  afectacion_usd                  double precision,
  afectacion_mn                   double precision,
  afectacion_servicio             double precision,
  observaciones                   character varying(1000),
  cod_cdnt                        character varying(255),
  imputable                       boolean,
  incidencias                     boolean,
  id_materialespe                 integer,
  cantidad                        real,
  id_afectacion_telefonia_publica integer,
  id_averia_planta_exterior       integer,
  esclarecido                     boolean,
  expediente_inv_sac              boolean,
  sin_denuncia                    boolean,
  articulo_83                     boolean,
  articulo_82                     boolean,
  medida_administrativa           boolean,
  menor_edad                      boolean,
  expfp                           boolean,
  pendiente_despacho              boolean,
  pendiente_juicio                boolean,
  priv_lib                        boolean,
  cantidad_sancionados            integer,
  sentencia                       character varying(255),
  prevenido                       boolean
);

--@Henry Serra Morejon
--La siguiente funcionalidad permite conocer todos los hechos para una unidad organizativa especificada
--Diferentes llamadas a la funcion: 
-- 1. Select * From hechos_por_uorg_mes_anno(41, 2019, 6). Devuelve los hechos de la unidad con id 41 en el anno 2019, en el mes de junio
-- 2. Select * From hechos_por_uorg_mes_anno(41, 2019, 0). Devuelve los hechos de la unidad con id 41 en el anno 2019.

CREATE OR REPLACE FUNCTION hechos_por_uorg_mes_anno(id_uorg integer, anno double precision, mes integer)
  RETURNS SETOF devolver_hechos_uorg AS
$BODY$

DECLARE rec record;

BEGIN

  IF mes > 0
  THEN

    FOR rec IN

    SELECT *
    FROM hechos
    WHERE hechos.id_uorg = $1
      AND date_part('year', hechos.fecha_ocurrencia) = $2
      AND date_part('mons', hechos.fecha_ocurrencia) = $3
    ORDER BY id_reg

    LOOP
      RETURN NEXT rec;
    END LOOP;

  ELSE
    FOR rec IN

    SELECT *
    FROM hechos
    WHERE hechos.id_uorg = $1
      AND date_part('year', hechos.fecha_ocurrencia) = $2
    ORDER BY id_reg

    LOOP
      RETURN NEXT rec;
    END LOOP;

  END IF;

  RETURN;

END;
$BODY$
LANGUAGE plpgsql
VOLATILE

