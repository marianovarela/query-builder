# <img src="src\main\resources\termita.svg" width="80" height="80"> Termita Query Builder

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

para realizar joins las columnas tiene que ser distintas