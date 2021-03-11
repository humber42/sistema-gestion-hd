--@Henry Serra Morejón
--Trigger que se dispara cuando se inserta o modifica un hecho. Si el año de la fecha_ocurrencia no se encuentra en la tabla annos, se inserta, en caso de ya existir no pasa nada.
CREATE OR REPLACE FUNCTION actualizar_annos()
  RETURNS trigger AS
$BODY$

DECLARE anho integer;

BEGIN

  SELECT INTO anho anno FROM annos ORDER BY annos DESC LIMIT 1;

  IF date_part('year', NEW.fecha_ocurrencia) > anho
  THEN

    INSERT INTO annos (anno, active) VALUES (date_part('year', NEW.fecha_ocurrencia), FALSE);

  END IF;

  RETURN NULL;
END;
$BODY$
LANGUAGE plpgsql
VOLATILE;


--@Henry Serra Morejón
--Ejecución del trigger en la tabla hechos
CREATE TRIGGER act_annos
  AFTER INSERT OR UPDATE
  ON hechos
  FOR EACH ROW
EXECUTE PROCEDURE actualizar_annos();