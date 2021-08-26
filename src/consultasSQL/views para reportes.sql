--reporte 1
CREATE or REPLACE VIEW resumen_general_pases as
SELECT tipo_pase, codigo, numero_pase, nombre, numero_identidad, unidad_organizativa,
CASE baja
    WHEN (1) THEN 'No'::text
    ELSE 'Si'::text
END AS activo
FROM registro_pases
JOIN tipos_pase_identificativo ON registro_pases.id_tipo_pase = tipos_pase_identificativo.id
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa;

--reporte 2
CREATE or REPLACE VIEW resumen_pases_uorg as
SELECT tipo_pase, codigo, numero_pase, nombre, numero_identidad, unidad_organizativa, acceso, fecha_validez
FROM registro_pases
JOIN tipos_pase_identificativo ON registro_pases.id_tipo_pase = tipos_pase_identificativo.id
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
GROUP BY unidad_organizativa, tipo_pase, codigo, numero_pase, nombre, numero_identidad, acceso, fecha_validez;

--reporte 3
CREATE or REPLACE VIEW resumen_pases_permanentes as
SELECT codigo, numero_pase, nombre, numero_identidad, unidad_organizativa, acceso
FROM registro_pases
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
WHERE id_tipo_pase = 1
GROUP BY unidad_organizativa, codigo, numero_pase, nombre, numero_identidad, acceso;

--reporte 4
CREATE or REPLACE VIEW resumen_pases_provisionales as
SELECT codigo, numero_pase, nombre, numero_identidad, unidad_organizativa, acceso
FROM registro_pases
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
WHERE id_tipo_pase = 2
GROUP BY unidad_organizativa, codigo, numero_pase, nombre, numero_identidad, acceso;

--reporte 5
CREATE or REPLACE VIEW resumen_pases_especiales as
SELECT codigo, numero_pase, nombre, numero_identidad, unidad_organizativa, acceso
FROM registro_pases
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
WHERE id_tipo_pase = 3
GROUP BY unidad_organizativa, codigo, numero_pase, nombre, numero_identidad, acceso;

--reporte 6
CREATE or REPLACE VIEW resumen_pases_negros as
SELECT codigo, numero_pase, nombre, numero_identidad, unidad_organizativa, acceso
FROM registro_pases
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
WHERE id_tipo_pase = 4
GROUP BY unidad_organizativa, codigo, numero_pase, nombre, numero_identidad, acceso;

--reporte 7
CREATE or REPLACE VIEW resumen_pases_son_baja as
SELECT tipo_pase, codigo, numero_pase, nombre, unidad_organizativa
FROM registro_pases
JOIN tipos_pase_identificativo ON registro_pases.id_tipo_pase = tipos_pase_identificativo.id
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
WHERE baja = 1;

--reporte 8
CREATE or REPLACE VIEW resumen_pases_impresos as
SELECT tipo_pase, codigo, numero_pase, nombre, cant_impresiones, to_date(registro_impresiones.ultima_impresion,'DD-MM-YYYY') as ultima_impresion
FROM registro_pases
JOIN tipos_pase_identificativo ON registro_pases.id_tipo_pase = tipos_pase_identificativo.id
JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
JOIN registro_impresiones ON registro_pases.id_reg = registro_impresiones.id_reg
ORDER BY nombre;

--reporte 9
CREATE OR REPLACE VIEW  resumen_pases_pendiente_de_foto AS
SELECT tipo_pase, codigo, numero_pase, nombre, numero_identidad, unidad_organizativa
FROM registro_pases
       JOIN tipos_pase_identificativo ON registro_pases.id_tipo_pase = tipos_pase_identificativo.id
       JOIN codigo_pase_identificativo ON registro_pases.id_codigo_pase = codigo_pase_identificativo.id
       JOIN unidades_organizativas ON registro_pases.id_uorg = unidades_organizativas.id_unidad_organizativa
WHERE image_url = '' OR image_url = 'no-image.jpg';

