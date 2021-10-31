CREATE OR REPLACE FUNCTION cant_pases_registrados_tipo_pase(id_pase integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant COUNT(registro_pases.id_reg) AS cantidad_pases FROM registro_pases
  WHERE registro_pases.id_tipo_pase = $1 AND registro_pases.baja = 0;

  IF cant IS NULL THEN
    cant = 0;
  END IF;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION cant_pases_impresos_tipo_pase(id_pase integer)
  RETURNS integer AS
$BODY$
DECLARE cant integer;
BEGIN

  SELECT INTO cant COUNT(registro_impresiones.id) AS cantidad_pases_impresos FROM registro_impresiones
  RIGHT JOIN registro_pases ON registro_pases.id_reg = registro_impresiones.id_reg
  WHERE registro_pases.id_tipo_pase = $1 AND registro_pases.baja = 0;

  IF cant IS NULL THEN
    cant = 0;
  END IF;

  RETURN cant;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;
