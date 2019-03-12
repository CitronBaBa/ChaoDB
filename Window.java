import java.util.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;
//// TODOLIST
// setting serial number
// del/enter col refined in talbewindow
// regex for (,,)could be refined, and could be moved to String_handler
//int out of range problem can be adressed
public class Window extends Application
{   private Control database = new Control("Untitled");
    private VBox layoutbox = new VBox();
    private GridPane topgrid = new GridPane();
    private GridPane buttonGrid = new GridPane();

    private TextField tablenameField = new TextField();
    private TextField LoadDbnameField = new TextField();
    private TextField SaveDbnameField = new TextField();
    private ArrayList<Button> tableBtns = new ArrayList<>();

    private TableHoverPanel tableglance;
    private Stage mainwindow;
    private int x0 = 0, y0 = 2;
    private int x = 0, y = 2;

    public static void main(String[] args)
    {   launch(args);
    }

    public void start(Stage window0)
    {   topgrid.setPadding(new Insets(5,5,5,5));
        topgrid.setVgap(5);
        topgrid.setHgap(5);
        buttonGrid.setPadding(new Insets(25,5,5,5));
        buttonGrid.setVgap(5);
        buttonGrid.setHgap(5);

     // intial set-up
        table_operation();
        addingButton();
        loadingButton();
        savingButton();

        tableNameFiled();
        LoadDbNameField();
        SaveDbNameField();

        layoutbox.getChildren().addAll(topgrid,buttonGrid);
        Scene scene0 = new Scene(layoutbox,350,450);
        mainwindow = window0;
        window0.setScene(scene0);
        window0.setTitle("ChaoDB: "+database.getname());
        window0.show();
    }

    private void hoverlisten(Button btn, Table table)
    {   btn.hoverProperty().addListener
        ((ObservableValue<? extends Boolean> observable, Boolean oldhovered, Boolean newhovered)
         -> {   if (newhovered)
                {   if(tableglance!=null)
                    {   if(tableglance.get_table().getname().equals(table.getname()))
                        return;
                        layoutbox.getChildren().remove(tableglance.getpanel());
                    }
                    tableglance = new TableHoverPanel(table);
                    layoutbox.getChildren().add(tableglance.getpanel());
                }
            }
        );
    }

    private void tableNameFiled()
    {   tablenameField.setPromptText("Enter table name");
        GridPane.setConstraints(tablenameField,0,0,2,1);
        buttonGrid.getChildren().add(tablenameField);
    }

    private void LoadDbNameField()
    {   LoadDbnameField.setPromptText("Enter Database Name");
        GridPane.setConstraints(LoadDbnameField,0,1,2,1);
        topgrid.getChildren().add(LoadDbnameField);
    }

    private void SaveDbNameField()
    {   SaveDbnameField.setPromptText("Enter Database Name");
        GridPane.setConstraints(SaveDbnameField,0,0,2,1);
        topgrid.getChildren().add(SaveDbnameField);
    }

    private void addingButton()
    {   Button btn = new Button("Add Table");
        btn.setOnAction(e->
        {   add_new_table(tablenameField.getText());
            tablenameField.clear();
        });
        GridPane.setConstraints(btn,2,0);
        buttonGrid.getChildren().add(btn);
    }

    private void loadingButton()
    {   Button btn = new Button("Load From");
        btn.setOnAction(e->
        {   clearTableBtns();
            database.readfrom(LoadDbnameField.getText());
            LoadDbnameField.clear();
            loadfromDatabase();
            updateDatabaseName();
        });
        GridPane.setConstraints(btn,2,1);
        topgrid.getChildren().add(btn);
    }

    private void savingButton()
    {   Button btn = new Button("Save As");
        btn.setOnAction(e->
        {   if(!String_handler.judgeDbName(SaveDbnameField.getText())) return;
            database.setname(SaveDbnameField.getText());
            SaveDbnameField.clear();
            database.savetodisk();
            updateDatabaseName();
        });
        GridPane.setConstraints(btn,2,0);
        topgrid.getChildren().add(btn);
    }

    private void addtogrid(Button btn)
    {   GridPane.setConstraints(btn,x,y);
        positioning();
        topgrid.getChildren().add(btn);
    }

    private void updateDatabaseName()
    {   mainwindow.setTitle("ChaoDB: "+database.getname());
    }

    private void positioning()
    {   if(x>1)
        {   y++;
            x=0;
        }
        else x++;
    }

    private void add_new_table(String name)
    {   if(!database.add_table(name)) return;
        table_button(name,database.get_table(name));
    }

    private Button table_button(String name, Table table)
    {   Button btn = new Button(name);
        tableBtns.add(btn);
        btn.setStyle("-fx-text-fill: rgb(88,144,255); -fx-font-weight: bold;");
        btn.setMaxWidth(120);
        btn.setOnAction(e->
        {   TableWindow newview = new TableWindow(table);
            newview.launch();
        });
        hoverlisten(btn,table);
        addtogrid(btn);
        return btn;
    }

    private void clearTableBtns()
    {   for(Button btn : tableBtns)
        {   topgrid.getChildren().remove(btn);
        }
        x=x0; y=y0;
    }

    private void loadfromDatabase()
    {   for(Table t : database.getAllTables())
        {   table_button(t.getname(),t);
        }
    }

    private void table_operation()
    {   add_new_table("Barbecue");
        table_init(database.get_table("Barbecue"));

        add_new_table("Hong JiaYi");
        table_init2(database.get_table("Hong JiaYi"));
    }

    private void table_init(Table t)
    {   t.add_column("int","lobster","false");
        t.add_column("string","basil","false");
        t.add_column("float","pork","false");
        t.add_column("float","lamb","false");
        t.row_entry("(1,ohlala,3.1415,2.0)");
        t.row_entry("(40,limousine,3.1415,3.0)");
        t.row_entry("(25,steak,3.34415,3.0)");
        t.row_entry("(25,steak,3.34415,3.231)");
    }

    private void table_init2(Table t)
    {   t.add_column("string","Family Name","false");
        t.add_column("string","First Name","false");
        t.add_column("string","description","false");
        t.add_column("boolean","idiot","false");
        t.row_entry("(Hong,Jiayi,is a jerk,true)");
        t.row_entry("(Hong,Jiayi,is a fool,true)");
        t.row_entry("(Hong,Jiayi,has no brain,true)");
        t.row_entry("(Wang,Chao,is a genius,false)");
    }
}
