--Ejecuta este script
CREATE OR REPLACE FUNCTION insert_tarifa(id_uorg integer, id_prov integer, tarifa double precision) RETURNS VOID AS
$BODY$
BEGIN
  INSERT INTO tarifas_posiciones_agentes(id_uorg, id_pservicio, tarifa) VALUES ($1,$2,$3);
end;
$BODY$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_tarifa(id_tarifa integer, tarifa double precision) RETURNS VOID AS
$BODY$
BEGIN
  UPDATE tarifas_posiciones_agentes SET tarifa = $2
  WHERE tarifas_posiciones_agentes.id_tarifa = $1;
end;
$BODY$
LANGUAGE plpgsql;
  
CREATE OR REPLACE FUNCTION insert_proveedor(prov character varying) RETURNS VOID AS
$BODY$
BEGIN 
    INSERT INTO proveedores_servicio_agentes(proveedores_servicio) VALUES ($1);
end;
$BODY$
LANGUAGE plpgsql;
  
CREATE OR REPLACE FUNCTION update_proveedor(id_prov integer, prov character varying) RETURNS VOID AS
$BODY$
BEGIN
UPDATE proveedores_servicio_agentes SET proveedores_servicio = $2 
WHERE proveedores_servicio_agentes.id = $1;
end;
$BODY$
LANGUAGE plpgsql;

create function update_registro_posiciones_agentes(id integer, horaslab integer, horasnolab integer, efectivo integer)
  returns void
language plpgsql
as $$
BEGIN
  UPDATE registro_posiciones_agentes SET horas_dias_laborables = $2,
                                         horas_no_laborales = $3,
                                         cantidad_efectivos = $4
  WHERE registro_posiciones_agentes.id_reg = $1;
end;
$$;

alter function update_registro_posiciones_agentes(integer, integer, integer, integer)
  owner to postgres;