default: Print

%: %.java
	javac $@.java
	java -ea $@
