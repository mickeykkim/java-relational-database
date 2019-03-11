default: Record Table

%: %.java
	javac $@.java
	java -ea $@
