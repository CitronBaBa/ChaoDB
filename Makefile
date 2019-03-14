jfx = --module-path FX/lib --add-modules=javafx.controls
default: Table
Control:
	javac $@.java Table.java String_handler.java TYPE.java
Table:
	javac $@.java String_handler.java TYPE.java
	java -ea Table
String_handler:
	javac $@.java TYPE.java
	java -ea String_handler
Window:
	javac $@.java TableWindow.java TableHoverPanel.java Control.java Table.java Column.java String_handler.java TYPE.java $(jfx)
	java $(jfx) $@
