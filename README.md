# Query Builder

# Descripcion

# Requisitos
- Instalar Hadoop
- Instalar Apache Spark
- Instalar Java 8
- Instalar Maven

# Instalacion


Agregar la dependencia a archivo pom.xml

```
<dependency>
  <groupId>io.github.marianovarela</groupId>
  <artifactId>qbuilder</artifactId>
  <version>0.0.3</version>
</dependency>
```

# Uso

## 

Inyectar el la clase encargada de ejecutar la consultas

```
@Autowired
Executor executor;
```

Seleccionar el tipo de consulta a realizar(en este ejemplo un select customizable)

```
SelectCustom select = new SelectCustom();
```

Seleccionar la tabla base a consultar

```
select.setEntity(Entity.Associations);
```

Agregar un filtro(opcional)

```
Where where = new Where("type = -2001");
select.setWhere(where);
```



## Resultado
Cada consulta retorna una instancia de la clase ResultSet. A esta se le podrá consultar:
* estado de la consulta
* mensaje en caso de error
* get() para obtener el resultado
* count() para obtener la cantidad obtenida en el resultado

```
ResultSet result = executor.execute(select); 
if(result.isError()) {
	String message = result.getMessage();
} else {
	List<Row> rows = result.get();
	long count = result.count();
}
```


## Condiciones de join

#### tableA.id = tableB.id
* Condition condition = Condition.makeWithFirstTableAndSecondTable(LogicOperator.AND,"id", "id");

#### tableA.id = 30
* Condition condition2 = Condition.makeWithFirstTableAndValue(LogicOperator.AND, "id", "30");

#### tableB.id = 30
* Condition condition3 = Condition.makeWithSecondTableAndValue(LogicOperator.AND, "id", "30");

## Join types

* JoinType.INNER("inner")
* JoinType.LEFT("left"),
* JoinType.LEFT_OUTER("left_outer"),
* JoinType.RIGHT("right"),
* JoinType.RIGHT_OUTER("right_outer"),
* JoinType.OUTER("outer");