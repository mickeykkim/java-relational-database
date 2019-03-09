default: Record

%: %.java
	javac $@.java
	java -ea $@
