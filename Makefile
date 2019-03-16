default: Record ColumnID Table File Print

%: %.java
	javac $@.java
	java -ea $@
