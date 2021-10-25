CREATE OR REPLACE FUNCTION dar_baja_pase(id_reg integer)
  RETURNS void AS
$BODY$
BEGIN
  update registro_pases set baja = 1
  where registro_pases.id_reg = $1;
end;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;