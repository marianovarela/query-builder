# Query Builder

# Descripci�n

# Instalaci�n
- Instalar Hadoop
- Instalar Apache Spark
- Instalar Java 8
- Instalar Maven

# Uso

## Resultado: ResultSet
Cada consulta retorna una instancia de la clase ResultSet. A esta se le podrá consultar:
* estado de la consulta
* mensaje en caso de error
* get() para obtener el resultado
* count() para obtener la cantidad obtenida en el resultado

## Condiciones de join

tableA.id = tableB.id
Condition condition = Condition.makeWithFirstTableAndSecondTable(LogicOperator.AND,"id", "id");
tableA.id = 30
Condition condition2 = Condition.makeWithFirstTableAndValue(LogicOperator.AND, "id", "30");
tableB.id = 30
Condition condition3 = Condition.makeWithSecondTableAndValue(LogicOperator.AND, "id", "30");

## Join types

JoinType.INNER("inner")
JoinType.LEFT("left"),
JoinType.LEFT_OUTER("left_outer"),
JoinType.RIGHT("right"),
JoinType.RIGHT_OUTER("right_outer"),
JoinType.OUTER("outer");

## JOIN 

Para realizar joins las columnas tiene que ser distintas o llamar por el alias de las tablas de manera tal de eliminar al ambiguedad.
Para esto por defecto al atributo *from* le agregamos el alias *df1* y al *to* el alias *df2*. De esta manera vamos a poder agregar un where donde df1.id = 30
o df1.id = df2.id.