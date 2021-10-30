CREATE OR REPLACE FUNCTION cant_hechos_anno(id_tipo_hecho integer, anno double precision)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant COUNT(hechos.id_reg) AS cantidad_hechos FROM hechos
  WHERE hechos.id_tipo_hecho = $1 AND date_part('year',hechos.fecha_ocurrencia) = $2;

  IF cant IS NULL THEN
    cant = 0;
  END IF;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql VOLATILE

