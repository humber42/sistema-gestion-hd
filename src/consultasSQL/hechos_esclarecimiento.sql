--PEXT
CREATE TYPE hechos_esclarecimiento_pext AS
(
    unidad_organizativa   character varying(255),
    fecha_ocurrencia      date,
    sintesis              text,
    municipio             character varying(255),
    afectacion_usd        double precision,
    afectacion_servicio   double precision,
    material_afectado     text,
    no_denuncia           character varying(255),
    exp_sac               boolean,
    sin_denuncia          boolean,
    articulo_83           boolean,
    articulo_82           boolean,
    medida_administrativa boolean,
    menor_edad            boolean,
    expfp                 boolean,
    pendiente_despacho    boolean,
    pendiente_juicio      boolean,
    priv_lib              boolean,
    cant_sancionados      integer,
    sentencia             character varying(255)
);

--ES NECESARIO PASAR POR PARAMETRO AMBAS FECHAS EN FORMATO DATE 'YYYY-MM-DD'
CREATE OR REPLACE FUNCTION obtener_hechos_esclarecimiento_pext(fecha_inicio date, fecha_fin date)
    RETURNS SETOF hechos_esclarecimiento_pext AS
$BODY$
DECLARE
    rec record;

BEGIN

    FOR rec IN
        SELECT unidad_organizativa,
               fecha_ocurrencia,
               concat(titulo, concat('. ', lugar))         AS sintesis,
               municipio,
               afectacion_usd,
               afectacion_servicio,
               concat(materiales, concat(' - ', cantidad)) AS material_afectado,
               numero_denuncia,
               expediente_inv_sac,
               sin_denuncia,
               articulo_83,
               articulo_82,
               medida_administrativa,
               menor_edad,
               expfp,
               pendiente_despacho,
               pendiente_juicio,
               priv_lib,
               cantidad_sancionados,
               sentencia
        FROM hechos
                 JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
                 JOIN tipo_materiales ON tipo_materiales.id_materiales = hechos.id_materialespe
                 JOIN municipios ON municipios.id_municipio = hechos.id_municipio
        WHERE id_tipo_hecho = 1
          AND (fecha_ocurrencia BETWEEN $1 AND $2)
        ORDER BY fecha_ocurrencia

        LOOP
            RETURN NEXT rec;
        END LOOP;
    RETURN;

END;

$BODY$
    LANGUAGE plpgsql VOLATILE;

--TPUB
CREATE TYPE hechos_esclarecimiento_tpub AS
(
    unidad_organizativa   character varying(255),
    fecha_ocurrencia      date,
    sintesis              text,
    municipio             character varying(255),
    afectacion_usd        double precision,
    afectacion_servicio   double precision,
    tipo_vandalismo       character varying(255),
    no_denuncia           character varying(255),
    exp_sac               boolean,
    sin_denuncia          boolean,
    articulo_83           boolean,
    articulo_82           boolean,
    medida_administrativa boolean,
    menor_edad            boolean,
    expfp                 boolean,
    pendiente_despacho    boolean,
    pendiente_juicio      boolean,
    priv_lib              boolean,
    cant_sancionados      integer,
    sentencia             character varying(255)
);

--ES NECESARIO PASAR POR PARAMETRO AMBAS FECHAS EN FORMATO DATE 'YYYY-MM-DD'
CREATE OR REPLACE FUNCTION obtener_hechos_esclarecimiento_tpub(fecha_inicio date, fecha_fin date)
    RETURNS SETOF hechos_esclarecimiento_tpub AS
$BODY$
DECLARE
    rec record;

BEGIN

    FOR rec IN
        SELECT unidad_organizativa,
               fecha_ocurrencia,
               concat(titulo, concat('. ', lugar)) AS sintesis,
               municipio,
               afectacion_usd,
               afectacion_servicio,
               afect_tpublica,
               numero_denuncia,
               expediente_inv_sac,
               sin_denuncia,
               articulo_83,
               articulo_82,
               medida_administrativa,
               menor_edad,
               expfp,
               pendiente_despacho,
               pendiente_juicio,
               priv_lib,
               cantidad_sancionados,
               sentencia
        FROM hechos
                 JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
                 JOIN tipo_vandalismo ON tipo_vandalismo.id_afect_tpublica = hechos.id_afectacion_telefonia_publica
                 JOIN municipios ON municipios.id_municipio = hechos.id_municipio
        WHERE id_tipo_hecho = 2
          AND (fecha_ocurrencia BETWEEN $1 AND $2)
        ORDER BY fecha_ocurrencia

        LOOP
            RETURN NEXT rec;
        END LOOP;
    RETURN;

END;

$BODY$
    LANGUAGE plpgsql VOLATILE;

--RESUMEN HECHOS ESCLARECIMIENTO PEXT TPUB
CREATE TYPE resumen_hechos_esclarecimiento_pext_tpub AS
(
    uorg                  character varying(255),
    total_conciliados     integer,
    cant_pext             integer,
    cant_tpub             integer,
    total_no_esclarecidos integer,
    cant_pext_no_esc      integer,
    cant_tpub_no_esc      integer,
    cant_exp_sac          integer,
    cant_sin_denuncia     integer,
    total_esclarecidos    integer,
    cant_pext_esc         integer,
    cant_tpub_esc         integer,
    cant_art_83           integer,
    cant_art_82           integer,
    cant_med_admin        integer,
    cant_menor            integer,
    cant_fase_prep        integer,
    cant_pend_desp        integer,
    cant_pend_juicio      integer,
    cant_casos            integer,
    cant_sanciones        integer,
    sanciones             integer
);

--METODO PARA OBTENER LA CANTIDAD DE HECHOS POR UORG
CREATE OR REPLACE FUNCTION cant_hechos_rango(id_tipo_hecho integer, id_uorg integer, fecha_inicio date, fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4);

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS O NO ESCLARECIDOS POR UORG
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_o_no(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                         fecha_fin date, esclarecido boolean)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = $5;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS NO ESCLARECIDOS SAC POR UORG
CREATE OR REPLACE FUNCTION cant_hechos_no_esclarecidos_sac(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                           fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = false
      AND hechos.expediente_inv_sac = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS SIN DENUNCIA POR UORG
CREATE OR REPLACE FUNCTION cant_hechos_no_esclarecidos_sin_denuncia(id_tipo_hecho integer, id_uorg integer,
                                                                    fecha_inicio date, fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = false
      AND hechos.sin_denuncia = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS ART 8.3
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_art83(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                          fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.articulo_83 = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS ART 8.2
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_art82(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                          fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.articulo_82 = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS MED. ADMIN.
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_med_admin(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                              fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.medida_administrativa = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS MENOR
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_menor(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                          fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.menor_edad = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS FASE PREP.
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_fase_prep(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                              fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.expfp = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS PEND. DESPACHO
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_pend_despacho(id_tipo_hecho integer, id_uorg integer,
                                                                  fecha_inicio date, fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.pendiente_despacho = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS PEND. JUICIO
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_pend_juicio(id_tipo_hecho integer, id_uorg integer,
                                                                fecha_inicio date, fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.pendiente_juicio = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE HECHOS ESCLARECIDOS CANT. CASOS
CREATE OR REPLACE FUNCTION cant_hechos_esclarecidos_casos(id_tipo_hecho integer, id_uorg integer, fecha_inicio date,
                                                          fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.priv_lib = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE SANCIONADOS DE LOS HECHOS ESCLARECIDOS POR UORG
CREATE OR REPLACE FUNCTION cant_sancionados_hechos_esclarecidos(id_tipo_hecho integer, id_uorg integer,
                                                                fecha_inicio date, fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant SUM(cantidad_sancionados)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO PARA OBTENER LA CANTIDAD DE SENTENCIAS POR UORG
CREATE OR REPLACE FUNCTION cant_sentencias_hechos_esclarecidos(id_tipo_hecho integer, id_uorg integer,
                                                               fecha_inicio date, fecha_fin date)
    RETURNS integer AS
$BODY$
DECLARE
    cant integer;
BEGIN

    SELECT INTO cant COUNT(id_reg)
    FROM hechos
             JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
    WHERE hechos.id_tipo_hecho = $1
      AND unidades_organizativas.id_unidad_organizativa = $2
      AND (hechos.fecha_ocurrencia BETWEEN $3 AND $4)
      AND hechos.esclarecido = true
      AND hechos.sentencia IS NOT NULL;

    IF cant IS NULL THEN
        cant = 0;
    END IF;

    RETURN cant;

END;

$BODY$
    LANGUAGE plpgsql;

--METODO QUE OBTIENE EL RESUMEN
CREATE OR REPLACE FUNCTION obtener_resumen_hechos_esclarecidos_o_no_por_uorg(fecha_inicio date, fecha_fin date)
    RETURNS SETOF resumen_hechos_esclarecimiento_pext_tpub AS
$BODY$

DECLARE
    rec record;

BEGIN

    FOR rec IN
        SELECT unidades_organizativas.unidad_organizativa,

               cant_hechos_esclarecidos_o_no(1, unidades_organizativas.id_unidad_organizativa, $1, $2, false) +
               cant_hechos_esclarecidos_o_no(2, unidades_organizativas.id_unidad_organizativa, $1, $2, false) +
               cant_hechos_esclarecidos_o_no(1, unidades_organizativas.id_unidad_organizativa, $1, $2, true) +
               cant_hechos_esclarecidos_o_no(2, unidades_organizativas.id_unidad_organizativa, $1, $2, true)
                                                                                                              AS total_conciliados,

               cant_hechos_rango(1, unidades_organizativas.id_unidad_organizativa, $1,
                                 $2)                                                                          AS cant_pext,
               cant_hechos_rango(2, unidades_organizativas.id_unidad_organizativa, $1,
                                 $2)                                                                          AS cant_tpub,

               cant_hechos_esclarecidos_o_no(1, unidades_organizativas.id_unidad_organizativa, $1, $2, false) +
               cant_hechos_esclarecidos_o_no(2, unidades_organizativas.id_unidad_organizativa, $1, $2, false)
                                                                                                              AS total_no_esclarecidos,

               cant_hechos_esclarecidos_o_no(1, unidades_organizativas.id_unidad_organizativa, $1, $2,
                                             false)                                                           AS cant_pext_no_esc,
               cant_hechos_esclarecidos_o_no(2, unidades_organizativas.id_unidad_organizativa, $1, $2,
                                             false)                                                           AS cant_tpub_no_esc,
               cant_hechos_no_esclarecidos_sac(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_no_esclarecidos_sac(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_exp_sac,

               cant_hechos_no_esclarecidos_sin_denuncia(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_no_esclarecidos_sin_denuncia(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_sin_denuncia,

               cant_hechos_esclarecidos_o_no(1, unidades_organizativas.id_unidad_organizativa, $1, $2, true) +
               cant_hechos_esclarecidos_o_no(2, unidades_organizativas.id_unidad_organizativa, $1, $2, true)
                                                                                                              AS total_esclarecidos,

               cant_hechos_esclarecidos_o_no(1, unidades_organizativas.id_unidad_organizativa, $1, $2,
                                             true)                                                            AS cant_pext_esc,
               cant_hechos_esclarecidos_o_no(2, unidades_organizativas.id_unidad_organizativa, $1, $2,
                                             true)                                                            AS cant_tpub_esc,

               cant_hechos_esclarecidos_art83(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_art83(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_art_83,

               cant_hechos_esclarecidos_art82(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_art82(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_art_82,

               cant_hechos_esclarecidos_med_admin(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_med_admin(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_med_admin,

               cant_hechos_esclarecidos_menor(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_menor(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_menor,

               cant_hechos_esclarecidos_fase_prep(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_fase_prep(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_fase_prep,

               cant_hechos_esclarecidos_pend_despacho(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_pend_despacho(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_pend_desp,

               cant_hechos_esclarecidos_pend_juicio(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_pend_juicio(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_pend_juicio,

               cant_hechos_esclarecidos_casos(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_hechos_esclarecidos_casos(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_casos,

               cant_sancionados_hechos_esclarecidos(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_sancionados_hechos_esclarecidos(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS cant_sancionados,

               cant_sentencias_hechos_esclarecidos(1, unidades_organizativas.id_unidad_organizativa, $1, $2) +
               cant_sentencias_hechos_esclarecidos(2, unidades_organizativas.id_unidad_organizativa, $1, $2)
                                                                                                              AS sentencias

        FROM hechos
                 JOIN unidades_organizativas ON unidades_organizativas.id_unidad_organizativa = hechos.id_uorg
        WHERE hechos.numero_denuncia IS NOT NULL
            AND (hechos.fecha_ocurrencia BETWEEN $1 AND $2)
            AND unidades_organizativas.unidad_organizativa LIKE '%DT%'
           OR unidades_organizativas.unidad_organizativa LIKE 'DVLH'
        GROUP BY unidades_organizativas.unidad_organizativa, unidades_organizativas.id_unidad_organizativa
        ORDER BY unidades_organizativas.id_unidad_organizativa

        LOOP

            RETURN NEXT rec;
        END LOOP;
    RETURN;

END;
$BODY$
    LANGUAGE plpgsql VOLATILE;