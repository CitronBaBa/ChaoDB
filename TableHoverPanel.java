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

public class TableHoverPanel
{   Table tablehovered;
    TableView<ColumnEntry> panel = new TableView<>();

    public TableHoverPanel(Table tablehovered)
    {   this.tablehovered = tablehovered;
        loadTableToPanel();
    }
    public TableView getpanel() {   return panel;}
    public Table getTable() {   return tablehovered;}

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

//could be simplified
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
