CREATE OR REPLACE FUNCTION obtener_hechos_para_resumen_mincom(anno double precision, mes integer)
  RETURNS
    SETOF devolver_hechos_uorg AS
$BODY$
DECLARE rec record;
BEGIN
  if mes > 0
  THEN
    for rec in
    SELECT *
    from hechos
    where date_part('year', hechos.fecha_ocurrencia) = $1
      and date_part('mons', hechos.fecha_ocurrencia) = $2
    ORDER BY id_reg

    LOOP
      RETURN next rec;
    end loop;

  else
    for rec in
    SELECT * from hechos where date_part('year', hechos.fecha_ocurrencia) = $1 ORDER BY id_reg
    LOOP
      return next rec;
    end loop;
  end if;
end;

$BODY$
language plpgsql
VOLATILE