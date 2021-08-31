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