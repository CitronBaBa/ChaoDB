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

public class TableHoverPanel
{   Table tablehovered;
    TableView<ColumnEntry> panel = new TableView<>();

    public TableHoverPanel(Table tablehovered)
    {   this.tablehovered = tablehovered;
        loadTableToPanel();
    }
    public TableView getpanel() {   return panel;}
    public Table get_table() {   return tablehovered;}

    private void loadTableToPanel()
    {   TableColumn<ColumnEntry,String> nameColumn = new TableColumn<>("Variable name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        panel.getColumns().add(nameColumn);

        TableColumn<ColumnEntry,String> typeColumn = new TableColumn<>("Data Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        panel.getColumns().add(typeColumn);

        TableColumn<ColumnEntry,Integer> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        panel.getColumns().add(sizeColumn);

        panel.setItems(loadDataFromTable());
    }

//could be simplified
    private ObservableList<ColumnEntry> loadDataFromTable()
    {   ObservableList<ColumnEntry> colEntries = FXCollections.observableArrayList();
        for(int i=0;i<tablehovered.getwidth();i++)
        {   String name = tablehovered.get_col_name(i);
            String type = String_handler.type_to_string(tablehovered.get_col_type(i));
            int size = tablehovered.getsize();
            colEntries.add(new ColumnEntry(name,type,size));
        }
        return colEntries;
    }
}
