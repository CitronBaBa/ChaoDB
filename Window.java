import java.util.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;
//// TODOLIST
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
        tableOperation();
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
                    {   if(tableglance.getTable().equals(table))
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
        {   if(!addNewTable(tablenameField.getText())) return;
            tablenameField.clear();
        });
        GridPane.setConstraints(btn,2,0);
        buttonGrid.getChildren().add(btn);
    }

    private void loadingButton()
    {   Button btn = new Button("Load From");
        btn.setOnAction(e->
        {   if(!database.readfrom(LoadDbnameField.getText())) return;
            clearTableBtns();
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
        {   if(!database.savetodisk(SaveDbnameField.getText())) return;
            SaveDbnameField.clear();
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

    private boolean addNewTable(String name)
    {   if(!database.addTable(name)) return false;
        tableButton(name,database.getTable(name));
        return true;
    }

    private Button tableButton(String name, Table table)
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
        {   tableButton(t.getname(),t);
        }
    }

    private void tableOperation()
    {   addNewTable("Barbecue");
        tableInit(database.getTable("Barbecue"));

        addNewTable("Hong JiaYi");
        tableInit2(database.getTable("Hong JiaYi"));
    }

    private void tableInit(Table t)
    {   t.addColumn("int","lobster","false");
        t.addColumn("string","basil","false");
        t.addColumn("float","pork","false");
        t.addColumn("float","lamb","false");
        t.rowEntry("(1,ohlala,3.1415,2.0)");
        t.rowEntry("(40,limousine,3.1415,3.0)");
        t.rowEntry("(25,steak,3.34415,3.0)");
        t.rowEntry("(25,steak,3.34415,3.231)");
    }

    private void tableInit2(Table t)
    {   t.addColumn("string","Family Name","false");
        t.addColumn("string","First Name","false");
        t.addColumn("string","description","false");
        t.addColumn("boolean","idiot","false");
        t.rowEntry("(Hong,Jiayi,is a jerk,true)");
        t.rowEntry("(Hong,Jiayi,is a fool,true)");
        t.rowEntry("(Hong,Jiayi,has no brain,true)");
        t.rowEntry("(Wang,Chao,is a genius,false)");
    }
}
