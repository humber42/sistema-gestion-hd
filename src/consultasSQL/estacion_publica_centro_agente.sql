create or replace function save_new_estacion_publica_centro_agente(id_municipio integer, id_uorg integer, centros integer, estaciones integer)
  returns void
language plpgsql
as $$
begin
  insert into estacion_publica_centro_agente(id_municipio, id_uorg, centros_agente, estaciones_publicas) VALUES ($1,$2,$3,$4);
end;
$$;

CREATE OR REPLACE FUNCTION update_estacion_publica_centro_agente(id_reg integer, id_municipio integer, id_uorg integer, centros integer, estaciones integer)
  RETURNS void AS
$BODY$
begin
  UPDATE estacion_publica_centro_agente SET id_municipio = $2, id_uorg = $3, centros_agente = $4, estaciones_publicas = $5
  WHERE estacion_publica_centro_agente.id_reg = $1;
end;
$BODY$
LANGUAGE plpgsql VOLATILE

CREATE OR REPLACE FUNCTION delete_estacion_publica_centro_agente(id_reg integer)
  RETURNS void AS
$BODY$
begin
  DELETE FROM estacion_publica_centro_agente
  WHERE estacion_publica_centro_agente.id_reg = $1;
end;
$BODY$
LANGUAGE plpgsql VOLATILE
