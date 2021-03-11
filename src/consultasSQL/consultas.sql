-----------------------------------------------
--              Types and Routines           --
--   Authors: Henry A. Serra Morejon         --
--            Humberto Cabrera Dominguez     --
--   Stored: Postgres SQL 9.3                --
-----------------------------------------------

--Types--
---------------------------------------------------
create type cantidades_hechos_pext_tpub_anno as
(
  anno double precision,
  mes  double precision,
  pext integer,
  tpub integer
);

create type conteo_hechos_unidad_organizativas as
(
  unidad_organizativa        varchar(255),
  cant_hechos                bigint,
  servicios_afectados        double precision,
  dinero_servicios_afectados double precision
);

create type material_afectado_anno as
(
  material_afectado varchar(255),
  cant_hechos       bigint,
  cant_material     real
);

create type tipo_afectacion_cant_hechos as
(
  tipo_afectacion varchar(255),
  cant_hechos     bigint
);

create type unidad_organizativa_estaciones_publ as
(
  unidad_organizativa varchar(255),
  estaciones_publ     bigint
);
----------------------------------------------------------------------------------------------------------


--Routines--
-----------------------------------------------------------------------------------------------------------


create function cant_hechos_pext_hastacierre(tiempo date, annoanterior character varying, tipohecho integer)
  returns integer
language plpgsql
as $$
DECLARE cant INTEGER;
BEGIN
  SELECT INTO cant COUNT(id_reg)
  FROM hechos
  WHERE hechos.id_tipo_hecho = tipoHecho
    AND fecha_ocurrencia <= tiempo
    AND to_date(to_char(fecha_ocurrencia, 'YYYY-MM-DD'), 'YYYY-MM-DD') >= to_timestamp(annoAnterior, 'YYYY-MM-DD');

  RETURN cant;
end;
$$;


create function cant_hechos_pext_hastacierre(tiempo date, annoanterior character varying)
  returns integer
language plpgsql
as $$
DECLARE cant INTEGER;
BEGIN
  SELECT INTO cant COUNT(id_reg)
  FROM hechos
  WHERE hechos.id_tipo_hecho = 1
    AND fecha_ocurrencia <= tiempo
    AND to_date(to_char(fecha_ocurrencia, 'YYYY-MM-DD'), 'YYYY-MM-DD') >= to_timestamp(annoAnterior, 'YYYY-MM-DD');

  RETURN cant;
end;
$$;

create function cant_hechos_pext_hastacierre(tiempo date)
  returns integer
language plpgsql
as $$
DECLARE cant INTEGER;
BEGIN
  SELECT INTO cant COUNT(id_reg)
  FROM hechos
  WHERE hechos.id_tipo_hecho = 1
    AND fecha_ocurrencia <= tiempo
    AND fecha_ocurrencia >= make_date(extract(year from tiempo) :: integer, 1, 1);

  RETURN cant;
end;
$$;

create function actualizar_exclarecimiento(id                    integer, sindenuncia boolean, expsac boolean,
                                           esclarecidodata       boolean, articulo83data boolean,
                                           articulo82data        boolean, medidasadministrativasdata boolean,
                                           pendientejuiciodata   boolean, expfasepreparatoriadata boolean,
                                           menoredaddata         boolean, pendientedespachodata boolean,
                                           privacionlibertaddata boolean, cantsancionadosdata character varying,
                                           sentenciadata         character varying)
  returns void
language plpgsql
as $$
BEGIN
  update hechos
  set hechos.sin_denuncia          = sinDenuncia,
      hechos.expediente_inv_sac    = expSAC,
      hechos.esclarecido           = esclarecidoData,
      hechos.articulo_83           = articulo83Data,
      hechos.articulo_82           = articulo82Data,
      hechos.medida_administrativa = medidasAdministrativasData,
      hechos.pendiente_juicio      = pendienteJuicioData,
      hechos.expfp                 = expFasePreparatoriaData,
      hechos.menor_edad            = menorEdadData,
      hechos.pendiente_despacho    = pendienteDespachoData,
      hechos.priv_lib              = privacionLibertadData,
      hechos.cantidad_sancionados  = cantSancionadosData,
      hechos.sentencia             = sentenciaData
  WHERE id_reg = id;
end;
$$;


create function actualizar_exclarecimiento(id                    integer, sindenuncia boolean, expsac boolean,
                                           esclarecidodata       boolean, articulo83data boolean,
                                           articulo82data        boolean, medidasadministrativasdata boolean,
                                           pendientejuiciodata   boolean, expfasepreparatoriadata boolean,
                                           menoredaddata         boolean, pendientedespachodata boolean,
                                           privacionlibertaddata boolean, cantsancionadosdata integer,
                                           sentenciadata         character varying)
  returns void
language plpgsql
as $$
BEGIN
  update hechos
  set sin_denuncia          = sinDenuncia,
      expediente_inv_sac    = expSAC,
      esclarecido           = esclarecidoData,
      articulo_83           = articulo83Data,
      articulo_82           = articulo82Data,
      medida_administrativa = medidasAdministrativasData,
      pendiente_juicio      = pendienteJuicioData,
      expfp                 = expFasePreparatoriaData,
      menor_edad            = menorEdadData,
      pendiente_despacho    = pendienteDespachoData,
      priv_lib              = privacionLibertadData,
      cantidad_sancionados  = cantSancionadosData,
      sentencia             = sentenciaData
  WHERE id_reg = id;
end;
$$;

create function cant_hechos_pext_mes_anno(mes double precision, anno double precision, id_tipo_hecho integer)
  returns integer
language plpgsql
as $$
DECLARE cant integer;
BEGIN
  SELECT INTO cant COUNT(hechos.id_reg) AS cant_pext
  FROM hechos
  WHERE hechos.id_tipo_hecho = $3
    AND date_part('mons', hechos.fecha_ocurrencia) = $1
    AND date_part('year', hechos.fecha_ocurrencia) = $2;

  RETURN cant;
END;
$$;

create function cantidad_afectaciones_anno_cierre_fiscalia(fecha date, fechainicio character varying)
  returns SETOF tipo_afectacion_cant_hechos
language plpgsql
as $$
DECLARE
  reg RECORD;
BEGIN
  FOR reg IN SELECT tv.afect_tpublica, count(id_reg)
             FROM hechos
                    JOIN tipo_vandalismo tv on hechos.id_afectacion_telefonia_publica = tv.id_afect_tpublica
             WHERE fecha_ocurrencia <= fecha
               AND to_date(to_char(fecha_ocurrencia, 'YYYY-MM-DD'), 'YYYY-MM-DD') >=
                   to_timestamp(fechainicio, 'YYYY-MM-DD')
               AND id_tipo_hecho = 2
             GROUP BY tv.afect_tpublica LOOP
    RETURN next reg;
  end loop;
  return;
end;
$$;

create function cantidad_estaciones_publicas_unidades_organizativas()
  returns SETOF unidad_organizativa_estaciones_publ
language plpgsql
as $$
DECLARE
  reg RECORD;
BEGIN
  FOR reg IN SELECT unidad_organizativa, sum(estacion_publica_centro_agente.estaciones_publicas)
             FROM estacion_publica_centro_agente
                    JOIN unidades_organizativas o on estacion_publica_centro_agente.id_uorg = o.id_unidad_organizativa
             WHERE unidad_organizativa LIKE 'DT%'
                OR unidad_organizativa LIKE 'DV%'
             GROUP BY unidad_organizativa LOOP
    RETURN NEXT reg;
  end loop;
end;
$$;

create function cantidad_materiales_anno_cierre_fiscalia(fecha date, fechainicio character varying)
  returns SETOF material_afectado_anno
language plpgsql
as $$
DECLARE
  reg RECORD;
BEGIN
  FOR reg IN SELECT m2.materiales, count(id_reg), sum(cantidad)
             FROM hechos
                    JOIN tipo_materiales m2 on hechos.id_materialespe = m2.id_materiales
             WHERE fecha_ocurrencia <= fecha
               AND to_date(to_char(fecha_ocurrencia, 'YYYY-MM-DD'), 'YYYY-MM-DD') >=
                   to_timestamp(fechainicio, 'YYYY-MM-DD')
               AND hechos.id_tipo_hecho = 1
             GROUP BY m2.materiales LOOP
    RETURN next reg;
  end loop;
  return;
end;
$$;


create function contar_hechos_pext_por_unidades_organizativas_annos(fecha date, fechainicio character varying,
                                                                    tipo  integer)
  returns SETOF conteo_hechos_unidad_organizativas
language plpgsql
as $$
DECLARE
  reg record;
BEGIN
  FOR reg IN Select unidad_organizativa, count(id_reg), sum(hechos.afectacion_servicio), sum(afectacion_usd)
             FROM hechos
                    JOIN unidades_organizativas ON hechos.id_uorg = unidades_organizativas.id_unidad_organizativa
             WHERE hechos.id_tipo_hecho = tipo
               AND unidad_organizativa LIKE 'DT%'
               AND fecha_ocurrencia <= fecha
               AND to_date(to_char(fecha_ocurrencia, 'YYYY-MM-DD'), 'YYYY-MM-DD') >=
                   to_timestamp(fechainicio, 'YYYY-MM-DD')
             GROUP BY unidad_organizativa LOOP
    RETURN NEXT reg;

  end loop;
  return;
end;
$$;

create function obtener_cant_hechos_pext_mes_anno(anno double precision)
  returns SETOF cantidades_hechos_pext_tpub_anno
language plpgsql
as $$
DECLARE rec record;

BEGIN

  FOR rec IN
  SELECT DISTINCT date_part('year', hechos.fecha_ocurrencia)                               AS anho,
                  date_part('mons', hechos.fecha_ocurrencia)                               AS mes,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia),
                                            date_part('year', hechos.fecha_ocurrencia), 1) AS pext,
                  cant_hechos_pext_mes_anno(date_part('mons', hechos.fecha_ocurrencia),
                                            date_part('year', hechos.fecha_ocurrencia), 2) AS tpub
  FROM hechos
  WHERE date_part('year', hechos.fecha_ocurrencia) = $1
  GROUP BY anho, mes
  ORDER BY mes LOOP

    RETURN NEXT rec;
  END LOOP;
  RETURN;

END;
$$;

create function registrar_hecho(titulo          character varying, id_tipo_hecho integer, fechaocurrencia date,
                                fecha_resumen   date, id_uorg integer, centro character varying,
                                lugar           character varying, id_municipio integer,
                                numero_denuncia character varying, afectacion_usd double precision,
                                afectacion_mn   double precision, afectacion_servicio double precision,
                                observaciones   character varying, cod_cdnt character varying)
  returns void
language plpgsql
as $$
BEGIN
  insert into hechos (titulo,
                      id_tipo_hecho,
                      fecha_ocurrencia,
                      fecha_parte,
                      id_uorg,
                      centro,
                      lugar,
                      id_municipio,
                      numero_denuncia,
                      afectacion_usd,
                      afectacion_mn,
                      afectacion_servicio,
                      observaciones,
                      cod_cdnt)
  values ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14);
end;
$$;

create function registrar_hecho_accidente_transito(id integer, imputable boolean, incidencias boolean)
  returns void
language plpgsql
as $$
BEGIN
  update hechos
  set incidencias                     = $3,
      imputable                       = $2,
      id_afectacion_telefonia_publica = null,
      id_materialespe                 = null,
      cantidad                        = null
  WHERE id_reg = id;
end;

$$;

create function registrar_hecho_delito_tpubl(id integer, id_vandalismo integer)
  returns void
language plpgsql
as $$
BEGIN
  update hechos
  set id_afectacion_telefonia_publica = id_vandalismo,
      id_materialespe                 = null,
      cantidad                        = null,
      imputable                       = null,
      incidencias                     = null
  WHERE id_reg = id;
end;
$$;

create function registrar_hecho_delito_tpubl(id integer, id_vandalismo integer)
  returns void
language plpgsql
as $$
BEGIN
  update hechos
  set id_afectacion_telefonia_publica = id_vandalismo,
      id_materialespe                 = null,
      cantidad                        = null,
      imputable                       = null,
      incidencias                     = null
  WHERE id_reg = id;
end;
$$;
------------------------------------------------------------------------------------------------------