--Insertar U/O--
create function insertar_unidad_organizativa(unidad_organizativa character varying) returns void
  AS
  $$
  BEGIN
    insert into unidades_organizativas(unidad_organizativa) values ($1);
  end;

$$
  language plpgsql;

--Editar U/O--
CREATE function editar_unidad_organizativa(id integer, unidades_organizativas character varying) returns void
  as $$
begin
  update unidades_organizativas set unidad_organizativa=$2
  where id_unidad_organizativa=$1;
end;
$$
language plpgsql;

--Eliminar U/O--
CREATE function eliminar_unidad_organizativa(id INTEGER) returns void
  as $$
  begin
  Delete FROM unidades_organizativas Where id_unidad_organizativa=$1;
end ;
  $$
  language plpgsql;

--Insertar Tipo de Maaterial--
CREATE function insertar_tipo_material(tipo_materiales character varying) returns void
  as $$
BEGIN
  insert into tipo_materiales(materiales) values ($1);
end;
$$
  language plpgsql;

--Editar tipo de Material--
create function editar_tipo_material (id integer, tipoMateriales character varying) RETURNS VOID
  AS $$
begin
  update tipo_materiales set materiales=$2
  where id_materiales=$1;
end;
$$
  language plpgsql;

--Eliminar tipo de Material--
create function eliminar_tipo_material(id integer)returns void
  as $$
begin
  delete from tipo_materiales where id_materiales=$1;
end;
$$
  language plpgsql;

--Insertar Tipo de Averia--
create function insertar_averia_pext (tipo_averia character varying)returns void
  as $$
begin
  insert into codaverias(causa) values ($1);
end;
$$
  LANGUAGE plpgsql;

--Editar Tipo de Averia--
create function editar_averia_pext(id integer, tipo_averia character varying) returns void
  as $$
begin
  update codaverias set causa=$2
  where id_avpext=$1;
end;
$$
  language plpgsql;

--Eliminar Tipo de Averia--
create function eliminar_averia_pext(id integer)returns void
  as $$
begin
  delete FROM codaverias where id_avpext=$1;
end;
$$
  language plpgsql;

--Insertar Municipio--
create function insertar_municipio (municipio character varying) returns void
  as $$
begin
  insert into municipios(municipio) values ($1);
end;
  $$
  language plpgsql;

--Editar Municipio--
create function editar_municipio (id integer, municipio character varying)returns void
  as $$
begin
  update municipios set municipio=$2
  where id_municipio=$1;
end;
$$
  language plpgsql

--Eliminar Municipio--
create function eliminar_municipio (id integer)returns void
  as $$
begin
  delete from municipios where id_municipio=$1;
end;
$$
  language plpgsql;

--Insertar Tipo de Vandalismo--
create function inserta_tipo_vandalismo (tipoVandalismo character varying)returns void
  as $$
begin
  insert into tipo_vandalismo (afect_tpublica) values ($1);
end;
$$
  language plpgsql;

--Editar Tipo de Vandalismo--
create function editar_tipo_vandalismo (id integer, tipoVandalismo character varying)returns void
  as $$
begin
  update tipo_vandalismo set afect_tpublica=$2
  where id_afect_tpublica=$1;
end;
$$
  language plpgsql;

--Eliminar Tipo de Vandalismo--
create function eliminar_tipo_vandalismo (id integer)returns void
  as $$
begin
  delete from tipo_vandalismo where id_afect_tpublica=$1;
end;
$$
  language plpgsql;

--Editar Hechos--
create function editar_hechos (id integer, titulo character varying, centro character varying, lugar character varying) returns void
  as $$
begin
  update hechos set titulo=$2, centro=$3, lugar=$4
  where id_reg=$1;
end;
$$
  language plpgsql;

--Eliminar Hechos--
create function eliminar_hechos(id integer)returns void
  as $$
begin
  delete from hechos where id_reg=$1;
end;
$$
  language plpgsql;