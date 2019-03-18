import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.beans.value.*;
import javafx.beans.property.*;

/*  when your mouse hovering each table button in the main window
    this class is used and create a peek of this table
    by creaing a small tableview containg all the descriptive information about columns in table
*/
public class TableHoverPanel
{   Table tablehovered;
    TableView<ColumnEntry> panel = new TableView<>();

    public TableHoverPanel(Table tablehovered)
    {   this.tablehovered = tablehovered;
        loadTableToPanel();
    }
    public TableView getpanel() {   return panel;}
    public Table getTable() {   return tablehovered;}

// fill the panle with three viewcolumns to describe a database column in the table
// load the actual information from table
    private void loadTableToPanel()
    {   TableColumn<ColumnEntry,String> nameColumn = new TableColumn<>("Variable name");
        nameColumn.setCellValueFactory(cell-> new SimpleObjectProperty<>(cell.getValue().getName()));
        panel.getColumns().add(nameColumn);

        TableColumn<ColumnEntry,String> typeColumn = new TableColumn<>("Data Type");
        typeColumn.setCellValueFactory(cell-> new SimpleObjectProperty<>(cell.getValue().getType()));
        panel.getColumns().add(typeColumn);

        TableColumn<ColumnEntry,Boolean> sizeColumn = new TableColumn<>("Is Key?");
        sizeColumn.setCellValueFactory(cell-> new SimpleObjectProperty<>(cell.getValue().getIskey()));
        panel.getColumns().add(sizeColumn);

        panel.setItems(loadDataFromTable());
    }

// load data from table to a list
    private ObservableList<TableHoverPanel.ColumnEntry> loadDataFromTable()
    {   ObservableList<TableHoverPanel.ColumnEntry> colEntries = FXCollections.observableArrayList();
        for(int i=0;i<tablehovered.getwidth();i++)
        {   String name = tablehovered.getColName(i);
            String type = String_handler.typeToString(tablehovered.getColType(i));
            boolean iskey = tablehovered.getColIskey(i);
            colEntries.add(new ColumnEntry(name,type,iskey));
        }
        return colEntries;
    }

// a buffer class represent a row in a panel
    private static class ColumnEntry
    {   private String name;
        private String type;
        private boolean iskey;
        public ColumnEntry(String name, String type, boolean iskey)
        {   this.name = name;
            this.type = type;
            this.iskey = iskey;
        }
        public String getName() {   return name;}
        public String getType() {   return type;}
        public boolean getIskey() {   return iskey;}
    }

}
