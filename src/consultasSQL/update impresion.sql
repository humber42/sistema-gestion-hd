create function update_last_impression_and_quanty(id integer)
  returns void
language plpgsql
as $$
BEGIN 
  UPDATE registro_impresiones SET cant_impresiones = cant_impresiones + 1,
                                  ultima_impresion = to_char(now(),'DD-MM-YYYY hh24:mm:ss')
  WHERE registro_impresiones.id_reg = $1;
END
$$;

create function save_new_impression(id_reg integer)
  returns void
language plpgsql
as $$
declare fecha character varying;
begin
  fecha = to_char(now(),'DD-MM-YYYY hh24:mm:ss');
  insert into registro_impresiones (id_reg, cant_impresiones, ultima_impresion) values ($1, 1, fecha);
end;
$$;

CREATE OR REPLACE FUNCTION obtener_impresiones()
  RETURNS SETOF impresiones AS
$BODY$
DECLARE rec record;
BEGIN

  FOR rec IN
  SELECT numero_pase, numero_identidad, nombre, tipo_pase, substring(codigo,0,2), cant_impresiones
  FROM registro_pases
         LEFT JOIN registro_impresiones ON registro_pases.id_reg = registro_impresiones.id_reg
         JOIN tipos_pase_identificativo ON registro_pases.id_tipo_pase = tipos_pase_identificativo.id
         JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
  WHERE baja = 0

  LOOP
    RETURN NEXT rec;
  end loop;
  RETURN ;
end;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100