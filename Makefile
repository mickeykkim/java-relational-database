default: Record Table File Print

%: %.java
	javac $@.java
	java -ea $@
