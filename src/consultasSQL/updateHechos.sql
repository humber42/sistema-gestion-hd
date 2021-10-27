CREATE OR REPLACE FUNCTION update_hecho(id_reg integer, titulo character varying, id_tipo_hecho integer, fechaocurrencia date, fecha_resumen date, id_uorg integer, centro character varying, lugar character varying, id_municipio integer, numero_denuncia character varying, afectacion_usd double precision, afectacion_mn double precision, afectacion_servicio double precision, observaciones character varying, cod_cdnt character varying, imputable boolean, incidencias boolean, id_material integer, cantidad integer, id_vandalismo integer)
  RETURNS void AS
$BODY$
BEGIN
  UPDATE hechos
  SET  titulo = $2,  id_tipo_hecho = $3,  fecha_ocurrencia = $4,  fecha_parte = $5,  id_uorg = $6,  centro = $7,
       lugar = $8,  id_municipio = $9,  numero_denuncia = $10,  afectacion_usd = $11,  afectacion_mn = $12,  afectacion_servicio = $13,
       observaciones = $14,  cod_cdnt = $15,  imputable = $16,  incidencias = $17,  id_materialespe = $18,  cantidad = $19,
       id_afectacion_telefonia_publica = $20
  WHERE hechos.id_reg = $1;
end;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
ALTER FUNCTION update_hecho(integer, character varying, integer, date, date, integer, character varying, character varying, integer, character varying, double precision, double precision, double precision, character varying, character varying, boolean, boolean, integer, integer, integer)
  OWNER TO postgres;