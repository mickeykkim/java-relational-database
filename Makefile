default: Record ColumnID Table Print Database File

%: %.java
	javac $@.java
	java -ea $@
