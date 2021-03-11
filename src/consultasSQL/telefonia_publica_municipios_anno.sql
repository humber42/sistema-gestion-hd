CREATE TYPE telf_pub_municipios AS (
  municipio                character varying(255),
  cant_hechos              bigint,
  cant_servicios_afectados double precision,
  estaciones_publicas      integer,
  sa_ep                    double precision
);

CREATE OR REPLACE FUNCTION obtener_telefonia_publica_municipios_anno(tiempo date, annoAnterior character VARYING)
  RETURNS SETOF telf_pub_municipios AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN
  SELECT municipios.municipio,
         COUNT(hechos.id_reg)                                                                       as cant_hechos,
         SUM(hechos.afectacion_servicio)                                                            as cant_servicios_afectados,
         estacion_publica_centro_agente.estaciones_publicas,
         100 * SUM(hechos.afectacion_servicio) / estacion_publica_centro_agente.estaciones_publicas as sa_ep
  FROM hechos
         JOIN municipios ON municipios.id_municipio = hechos.id_municipio
         JOIN estacion_publica_centro_agente ON estacion_publica_centro_agente.id_municipio = municipios.id_municipio
  WHERE hechos.id_tipo_hecho = 2
    AND fecha_ocurrencia <= tiempo
    AND to_date(to_char(fecha_ocurrencia, 'YYYY-MM-DD'), 'YYYY-MM-DD') >= to_timestamp(annoAnterior, 'YYYY-MM-DD')
  GROUP BY municipios.municipio, estacion_publica_centro_agente.estaciones_publicas
  ORDER BY sa_ep DESC

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
ALTER FUNCTION obtener_telefonia_publica_municipios_anno(double precision)
  OWNER TO postgres;


