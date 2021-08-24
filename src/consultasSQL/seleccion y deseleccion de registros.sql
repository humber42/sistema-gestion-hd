CREATE OR REPLACE FUNCTION update_selection_on_registro_pases(identidad character varying)
  RETURNS void AS
$BODY$
BEGIN
  UPDATE registro_pases SET seleccionado = TRUE WHERE numero_identidad = $1;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION update_selection_on_registro_pases(character varying)
  OWNER TO postgres;

CREATE OR REPLACE FUNCTION deselect_all_selected_on_registro_pases()
  RETURNS void AS
$BODY$
BEGIN
  UPDATE registro_pases SET seleccionado = FALSE WHERE seleccionado = TRUE;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION deselect_all_selected_on_registro_pases()
  OWNER TO postgres;
