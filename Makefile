default: Record Table File

%: %.java
	javac $@.java
	java -ea $@
