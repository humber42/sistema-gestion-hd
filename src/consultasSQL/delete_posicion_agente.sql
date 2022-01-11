create function delete_posicion_agente(id_reg integer)
  returns void
language plpgsql
as $$
begin
  DELETE FROM registro_posiciones_agentes
  WHERE registro_posiciones_agentes.id_reg = $1;
end;
$$;