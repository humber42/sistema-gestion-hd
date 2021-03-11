--@Henry A. Serra Morejon
--Type para devolver los hechos cuya denuncia sea null y las perdidas sean 0 en un anno determinado
CREATE TYPE hechos_denuncia_y_perdidas AS (
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


--Metodo que permite obtener los hechos sin denuncia y con las perdidas en 0 para un anno determinado.
CREATE OR REPLACE FUNCTION obtener_hechos_denuncia_perdidas(anno double precision)
  RETURNS SETOF hechos_denuncia_y_perdidas AS
$BODY$

DECLARE rec record;

BEGIN

  FOR rec IN

  SELECT *
  FROM hechos
  WHERE date_part('year', hechos.fecha_ocurrencia) = $1
    AND (hechos.id_tipo_hecho = 1 or hechos.id_tipo_hecho = 2)
    AND (hechos.afectacion_usd + hechos.afectacion_mn = 0 Or hechos.numero_denuncia IS NULL)
  ORDER BY hechos.id_tipo_hecho

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
ALTER FUNCTION obtener_hechos_denuncia_perdidas(double precision)
  OWNER TO postgres;
