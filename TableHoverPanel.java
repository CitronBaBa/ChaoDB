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

        TableColumn<ColumnEntry,Integer> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(cell-> new SimpleObjectProperty<>(cell.getValue().getSize()));
        panel.getColumns().add(sizeColumn);

        panel.setItems(loadDataFromTable());
    }

//could be simplified
    private ObservableList<TableHoverPanel.ColumnEntry> loadDataFromTable()
    {   ObservableList<TableHoverPanel.ColumnEntry> colEntries = FXCollections.observableArrayList();
        for(int i=0;i<tablehovered.getwidth();i++)
        {   String name = tablehovered.getColName(i);
            String type = String_handler.typeToString(tablehovered.getColType(i));
            int size = tablehovered.getsize();
            colEntries.add(new ColumnEntry(name,type,size));
        }
        return colEntries;
    }

    private static class ColumnEntry
    {   private String name;
        private String type;
        private int size;
        public ColumnEntry()
        {   this.name = "";
            this.type = "";
            this.size = 0;
        }
        public ColumnEntry(String name, String type, int size)
        {   this.name = name;
            this.type = type;
            this.size = size;
        }
        public String getName() {   return name;}
        public String getType() {   return type;}
        public int getSize() {   return size;}
        public void setName(String name){   this.name = name;}
        public void setType(String type){   this.type = type;}
        public void setSize(int size){   this.size = size;}
    }

}
