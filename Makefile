jfx = --module-path FX/lib --add-modules=javafx.controls
default: Window

FileSystem:
	javac $@.java Table.java String_handler.java TYPE.java
	java -ea FileSystem
Table:
	javac $@.java Column.java String_handler.java TYPE.java
	java -ea Table
Column:
	javac $@.java String_handler.java TYPE.java
	java -ea Column
String_handler:
	javac $@.java TYPE.java
	java -ea String_handler
	
Window:
	javac $@.java TableWindow.java TableHoverPanel.java Control.java Table.java Column.java String_handler.java TYPE.java $(jfx)
	java $(jfx) $@
