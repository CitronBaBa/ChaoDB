jfx = --module-path FX/lib --add-modules=javafx.controls
default: Table
Control:
	javac $@.java Table.java String_handler.java TYPE.java
Table:
	javac $@.java String_handler.java TYPE.java
String_handler:
	javac $@.java TYPE.java
Window:
	javac $@.java TableWindow.java TableHoverPanel.java ColumnEntry.java Control.java Table.java String_handler.java TYPE.java $(jfx)
	java $(jfx) $@
