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

    public void launch()
    {   window.initModality(Modality.APPLICATION_MODAL);

        addViewcolumns();
        tableview.setItems(loadtable());

        //exit button
        Button btn = closebutton();

        //overall
        hbox = inputHbox();
        hbox2 = ColinputHbox();
        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableview,hbox,hbox2,btn);
        Scene scene = new Scene(vbox,400,400);
        window.setScene(scene);
        window.showAndWait();
    }

    private HBox inputHbox()
    {   HBox hbox = new HBox();
        hbox.setPadding(new Insets(5,5,5,5));
        hbox.setSpacing(5);
        for(int i=0;i<table.getwidth();i++)
        {   TextField input = new TextField();
            input.setPromptText(table.getColName(i));
            hbox.getChildren().add(input);
            inputs.add(input);
        }
        Button add = new Button("ADD");
        add.setMinWidth(55);
        add.setOnAction(e->add());
        hbox.getChildren().add(add);
        Button del = new Button("DEL");
        del.setOnAction(e->del());
        del.setMinWidth(55);
        hbox.getChildren().add(del);
        return hbox;
    }

    private HBox ColinputHbox()
    {   HBox hbox = new HBox();
        hbox.setPadding(new Insets(5,5,5,5));
        hbox.setSpacing(5);
        TextField input = new TextField();
        input.setPromptText("data type");
        hbox.getChildren().add(input);
        colinputs.add(input);

        TextField input2 = new TextField();
        input2.setPromptText("column name");
        hbox.getChildren().add(input2);
        colinputs.add(input2);

        TextField input3 = new TextField();
        input3.setPromptText("is it a Key");
        hbox.getChildren().add(input3);
        colinputs.add(input3);

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

// column operation only add/remove at the end of the list
// add/remove arbitrary column not supported yet

    private void colAdd()
    {   String type = colinputs.get(0).getText();
        String name = colinputs.get(1).getText();
        String iskey = colinputs.get(2).getText();
        if(!table.addColumn(type,name,iskey)) return;
        // reload all the buffers to the view
        tableview.setItems(loadtable());

        //add at the end
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


// all buffers in the lists are different objects
// so each button can be uniquely selected and deleted
// **** feature needed **** select multi rows
    private void del()
    {   ObservableList<Buffer> selected,all;
        all = tableview.getItems();
        selected = tableview.getSelectionModel().getSelectedItems();
        if (all.size()==0 || selected.size()==0) return;

        List<Integer> indexs = new ArrayList<>();
        for(int i=0;i<selected.size();i++)
        {   Buffer target = selected.get(i);
            indexs.add(all.indexOf(target));
        }
        for(int index : indexs)
        {   all.remove(index);
            table.deleteRow(index);
        }
    }

    private void add()
    {   String inputstring = "";
        for(TextField t : inputs)
        {   String s = t.getText();
            inputstring += ","+s;
            t.clear();
        }
        if(inputstring.equals("")) return;
        inputstring = inputstring.substring(1);
        inputstring = "("+inputstring+")";
        //type check and write to databse
        if(!table.rowEntry(inputstring)) return;
        // write to buffer
        tableview.getItems().add(bufferAssemble(table.getsize()-1));
    }


    private void addViewcolumns()
    {   for(int i=0;i<table.getwidth();i++)
        {  addViewcolumn(i);
        }
    }

    private void addViewcolumn(int col_index)
    {   tableview.getColumns().add(createColumn(col_index));
    }

    private void delViewcolumn()
    {   int index = tableview.getColumns().size()-1;
        tableview.getColumns().remove(index);
    }

// this version is dynamic, there is a binding between cell and buffer
    private TableColumn<Buffer,String> createColumn(int col_index)
    {   TableColumn<Buffer,String> col = new TableColumn<>(table.getColName(col_index));
        col.setCellValueFactory(param ->
        {   return Bindings.createObjectBinding
            (  ()->{ return (param.getValue().valueProperty(col_index).getValue());}
               , param.getValue().valueProperty(col_index)
            );
       });
        return col;
    }

// this version is static, once cell is generated, its value will not be influenced
// by the changes in the buffer
/*
     private TableColumn<Buffer,String> create_column(int col_index)
    {   TableColumn<Buffer,String> col = new TableColumn<>(table.get_col_name(col_index));
        col.setCellValueFactory(param ->   new SimpleObjectProperty<>
        (param.getValue().valueProperty(col_index).getValue())
        );
        return col;
    }*/

    private Button closebutton()
    {   Button btn = new Button("Close table view");
        btn.setOnAction(e->
        {   window.close();
        });
        return btn;
    }

    private ObservableList<Buffer> loadtable()
    {   ObservableList<Buffer> buffers = FXCollections.observableArrayList();
        for(int i=0;i<table.getsize();i++)
        {   buffers.add(bufferAssemble(i));
        }
        return buffers;
    }

    private Buffer bufferAssemble(int index)
    {   Buffer buffer = new Buffer(table.query(index));
        return buffer;
    }
}

class Buffer
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
