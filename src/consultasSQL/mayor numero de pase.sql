CREATE OR REPLACE FUNCTION obtener_mayor_numero_pase(tipo_pase integer, codigo_pase integer)
  RETURNS character varying AS
$BODY$
DECLARE num_pase character varying;
BEGIN

  SELECT INTO num_pase numero_pase
  FROM registro_pases
  WHERE id_tipo_pase = $1
    AND id_codigo_pase = $2
  ORDER BY numero_pase DESC;

  RETURN num_pase;

END
$BODY$
LANGUAGE plpgsql
VOLATILE
COST 100;
ALTER FUNCTION obtener_mayor_numero_pase(integer, integer)
  OWNER TO postgres;
