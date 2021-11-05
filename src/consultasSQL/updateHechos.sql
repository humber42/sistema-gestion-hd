CREATE OR REPLACE FUNCTION update_hecho(id_reg integer, titulo character varying, id_tipo_hecho integer, fechaocurrencia date, fecha_resumen date, id_uorg integer, centro character varying, lugar character varying, id_municipio integer, numero_denuncia character varying, afectacion_usd double precision, afectacion_mn double precision, afectacion_servicio double precision,
  observaciones character varying, cod_cdnt character varying, imputable boolean, incidencias boolean,
  id_material integer, cantidad integer, id_vandalismo integer, prevenido boolean default false )
  RETURNS void AS
$BODY$
BEGIN
  UPDATE hechos
  SET  titulo = $2,  id_tipo_hecho = $3,  fecha_ocurrencia = $4,  fecha_parte = $5,  id_uorg = $6,  centro = $7,
       lugar = $8,  id_municipio = $9,  numero_denuncia = $10,  afectacion_usd = $11,  afectacion_mn = $12,  afectacion_servicio = $13,
       observaciones = $14,  cod_cdnt = $15,  imputable = $16,  incidencias = $17,  id_materialespe = $18,  cantidad = $19,
       id_afectacion_telefonia_publica = $20, prevenido = $21
  WHERE hechos.id_reg = $1;
end;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION registrar_hecho_modified(titulo character varying, id_tipo_hecho integer, fechaocurrencia date, fecha_resumen date, id_uorg integer, centro character varying,
                                                    lugar character varying, id_municipio integer, numero_denuncia character varying, afectacion_usd double precision, afectacion_mn double precision, afectacion_servicio double precision,
                                                    observaciones character varying, cod_cdnt character varying, prevenido boolean)
  RETURNS void AS
$BODY$
BEGIN
  insert into hechos(titulo, id_tipo_hecho, fecha_ocurrencia, fecha_parte,
                     id_uorg, centro, lugar, id_municipio, numero_denuncia,
                     afectacion_usd, afectacion_mn, afectacion_servicio, observaciones,
                     cod_cdnt, prevenido)values ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15);
end;
$BODY$
LANGUAGE plpgsql VOLATILE
