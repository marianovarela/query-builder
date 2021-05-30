# <img src="src\main\resources\termita.svg" width="80" height="80"> Termita Query Builder

# Descripci�n

# Instalaci�n
- Instalar Hadoop
- Instalar Apache Spark
- Instalar Java 8
- Instalar Maven

# Uso

## Condiciones de join

tableA.id = tableB.id
Condition condition = Condition.makeWithFirstTableAndSecondTable(LogicOperator.AND,"id", "id");
tableA.id = 30
Condition condition2 = Condition.makeWithFirstTableAndValue(LogicOperator.AND, "id", "30");
tableB.id = 30
Condition condition3 = Condition.makeWithSecondTableAndValue(LogicOperator.AND, "id", "30");
