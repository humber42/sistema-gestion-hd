create function delete_posicion_agente(id_reg integer)
  returns void
language plpgsql
as $$
begin
  DELETE FROM registro_posiciones_agentes
  WHERE registro_posiciones_agentes.id_reg = $1;
end;
$$;

create or replace function update_registro_posiciones_agentes(id integer, proveedor integer, horaslab integer, horasnolab integer, efectivo integer)
  returns void
language plpgsql
as $$
BEGIN
  UPDATE registro_posiciones_agentes SET id_pservicio = $2,
                                         horas_dias_laborables = $3,
                                         horas_no_laborales = $4,
                                         cantidad_efectivos = $5
  WHERE registro_posiciones_agentes.id_reg = $1;
end;
$$;