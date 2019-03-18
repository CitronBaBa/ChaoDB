import java.util.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.beans.*;
import javafx.beans.property.*;
import javafx.beans.binding.Bindings;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

/* launch a brand new window the display the detail of a specific table
   has the ability to : add/del a row data;
                        add/del a column (at the end);
   the layout is organized by a vertical box:
                      a table view - row manipulation - column manipulation - exit
   some of them are global so that they can be changed when column is added or deleted
   or that others can retrieve information from them
*/

public class TableWindow
{   Table table;
    private Stage window = new Stage();
    TableView<Buffer> tableview = new TableView<>();
    List<TextField> inputs = new ArrayList<>();
    List<TextField> colinputs = new ArrayList<>();
    HBox hbox,hbox2;

    public TableWindow(Table table)
    {   this.table = table;
    }

// do the layout setup and launch the window
    public void launch()
    {   //layout setup
        addViewcolumns();
        tableview.setItems(loadtable());

        hbox = inputHbox();
        hbox2 = ColinputHbox();
        VBox vbox = new VBox();
        HBox btnbox = new HBox();
        Button btn = closebutton();
        btnbox.getChildren().add(btn);
        btnbox.setAlignment(Pos.CENTER_RIGHT);

        HBox texthbox = textHBox("Add a Row / delete a selected Row in the table:");
        HBox texthbox2 = textHBox("Append/delete a Column at the end : (data type, column name, is Key?) ");
        HBox texthbox3 = textHBox(" allowed types: int, string, float, boolean");

        vbox.getChildren().addAll(tableview,texthbox,hbox,texthbox2,hbox2,texthbox3,btnbox);
        Scene scene = new Scene(vbox,500,500);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.showAndWait();
    }

// row add/delete horzontal box
    private HBox inputHbox()
    {   HBox hbox = new HBox();
        hbox.setPadding(new Insets(5,5,5,5));
        hbox.setSpacing(5);

        for(int i=0;i<table.getwidth();i++)
        {   inputs.add(addInputField(table.getColName(i),hbox));
        }

        Button add = new Button("ADD");
        add.setMinWidth(55);
        add.setOnAction(e->addRow());
        hbox.getChildren().add(add);
        Button del = new Button("DEL");
        del.setOnAction(e->deleteRow());
        del.setMinWidth(55);
        hbox.getChildren().add(del);

        return hbox;
    }

// column add/delete horzontal box
    private HBox ColinputHbox()
    {   HBox hbox = new HBox();
        hbox.setPadding(new Insets(5,5,5,5));
        hbox.setSpacing(5);

        colinputs.add(addInputField("data type",hbox));
        colinputs.add(addInputField("column name",hbox));
        colinputs.add(addInputField("is it a Key",hbox));

        Button add = new Button("ADD");
        add.setMinWidth(55);
        add.setOnAction(e->colAdd());
        hbox.getChildren().add(add);

        Button del = new Button("DEL");
        del.setOnAction(e->colDel());
        del.setMinWidth(55);
        hbox.getChildren().add(del);
        return hbox;
    }

// add a text field to a hbox, with a propmpt text
// wrap up in a function
    private TextField addInputField(String propmptText, HBox hbox)
    {   TextField input = new TextField();
        input.setPromptText(propmptText);
        hbox.getChildren().add(input);
        return input;
    }

// return a HBox containing a label with the text, like a explanation bar
    private HBox textHBox(String text)
    {   HBox hbox = new HBox();
        hbox.setPadding(new Insets(3));
        hbox.setSpacing(5);
        Label label = new Label(text);
        hbox.getChildren().add(label);
        return hbox;
    }

// column operation only add/remove at the end of the list
// add/remove arbitrary column not supported yet
    private void colAdd()
    {   String type = colinputs.get(0).getText();
        String name = colinputs.get(1).getText();
        String iskey = colinputs.get(2).getText();
        if(!table.addColumn(type,name,iskey)) return;

        // reload all the buffers to the view
        tableview.setItems(loadtable());

        //add on the graphical screen
        int index = table.getwidth()-1;
        addViewcolumn(index);
        TextField newfield = new TextField();
        newfield.setPromptText(table.getColName(index));
        inputs.add(newfield);
        hbox.getChildren().add(hbox.getChildren().size()-2,newfield);
    }

    private void colDel()
    {   if(!table.delColumn()) return;
        tableview.setItems(loadtable());
        delViewcolumn();
        inputs.remove(inputs.size()-1);
        hbox.getChildren().remove(hbox.getChildren().size()-3);
    }

// delete the one row which is selected to table window and also to the model
// currently only selecting one row
    private void deleteRow()
    {   ObservableList<Buffer> selected,all;
        all = tableview.getItems();
        selected = tableview.getSelectionModel().getSelectedItems();
        if (all.size()==0 || selected.size()==0) return;

        Buffer target = selected.get(0);
        int index = all.indexOf(target);

        all.remove(index);
        table.deleteRow(index);
    }

// add one row to the table model and table window
    private void addRow()
    {   // form a (input,input1,..) string
        String inputstring = "";
        for(TextField t : inputs)
        {   String s = t.getText();
            inputstring += ","+s;
            t.clear();
        }
        if(inputstring.equals("")) return;
        inputstring = inputstring.substring(1);
        inputstring = "("+inputstring+")";

        //write to databse
        if(!table.rowEntry(inputstring)) return;

        //add to the window
        tableview.getItems().add(bufferAssemble(table.getsize()-1));
    }

// add all columns from the table to the table window
    private void addViewcolumns()
    {   for(int i=0;i<table.getwidth();i++)
        {  addViewcolumn(i);
        }
    }

    private void addViewcolumn(int col_index)
    {   tableview.getColumns().add(createColumn(col_index));
    }

// this version of linking tablecolumn cell to data is dynamic,
// there is a binding between tablecolumn cell and buffer
    private TableColumn<Buffer,String> createColumn(int col_index)
    {   TableColumn<Buffer,String> col = new TableColumn<>(table.getColName(col_index));
        col.setMaxWidth(100);
        col.setCellValueFactory(param ->
        {   return Bindings.createObjectBinding
            (  ()->{ return (param.getValue().valueProperty(col_index).getValue());}
               , param.getValue().valueProperty(col_index)
            );
       });
        return col;
    }

// delete the last column in the window
    private void delViewcolumn()
    {   int index = tableview.getColumns().size()-1;
        tableview.getColumns().remove(index);
    }

// return a button that close the whole window
    private Button closebutton()
    {   Button btn = new Button("Close table view");
        btn.setOnAction(e->
        {   window.close();
        });
        return btn;
    }

// load all data from table into a list of buffers
    private ObservableList<Buffer> loadtable()
    {   ObservableList<Buffer> buffers = FXCollections.observableArrayList();
        for(int i=0;i<table.getsize();i++)
        {   buffers.add(bufferAssemble(i));
        }
        return buffers;
    }

// creat a single row data as one buffer
    private Buffer bufferAssemble(int index)
    {   Buffer buffer = new Buffer(table.query(index));
        return buffer;
    }

// a Buffer class serves as a row of data, used to convert columns to rows
// using a list so that the lengh of the row is variable
    static class Buffer
    {    public List<SimpleObjectProperty<String>> valuelist = new ArrayList<>();
         public Buffer(List<String> values)
         {   for(String s : values)
             {   valuelist.add(new SimpleObjectProperty<String>(s));
             }
         }
         public SimpleObjectProperty<String> valueProperty(int col_index)
         {   return  valuelist.get(col_index);
         }
    }

}
