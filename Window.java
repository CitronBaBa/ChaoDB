import java.util.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;

/* the main window of the program
   has the ability to: save/load to/from a database
                       add/delete table
                       have a peek on any table
   the layout is organized by a VBox of:
            topgrid - buttongrid - tableview(when a table button is hovered)
   when launched it is set to load a "demo" database by default
*/

// some objects are made global for others to retrieve information and do operations
public class Window extends Application
{   private Control database = new Control("Untitled");
    private VBox layoutbox = new VBox();
    private GridPane topgrid = new GridPane();
    private GridPane buttonGrid = new GridPane();

    private TextField tablenameField = new TextField();
    private TextField delTablenameField = new TextField();
    private TextField LoadDbnameField = new TextField();
    private TextField SaveDbnameField = new TextField();
    private ArrayList<Button> tableBtns = new ArrayList<>();

    private TableHoverPanel tableglance;
    private Stage mainwindow;

// used to auto-organized the table buttons in the buttongrid
    private int x0 = 0, y0 = 2;
    private int x = 0, y = 2;

    public static void main(String[] args)
    {   launch(args);
    }

    public void start(Stage window0)
    {   gridSetUp();

        addingButton();
        deleteButton();
        loadingButton();
        savingButton();

        tableNameFiled();
        delTableNameFiled();
        LoadDbNameField();
        SaveDbNameField();

        layoutbox.getChildren().addAll(topgrid,buttonGrid);
        Scene scene0 = new Scene(layoutbox,350,450);
        scene0.getStylesheets().add("./style.css");
        mainwindow = window0;
        window0.setScene(scene0);
        updateDatabaseName();
        window0.show();

     // display demo database
        tableDemoInit();
    }

// add a hover listener to a table button
    private void hoverlisten(Button btn, Table table)
    {   btn.hoverProperty().addListener
        ((ObservableValue<? extends Boolean> observable, Boolean oldhovered, Boolean newhovered)
         -> {   if (newhovered)
                {   hoverListen(table);
                }
            }
        );
    }

// remove the old one if a different table is hovered, and display the new one
    private void hoverListen(Table table)
    {   if(tableglance!=null)
        {   if(tableglance.getTable().equals(table)) return;
            removeCurrentTablePanle();
        }
        displayTableHoverPanle(table);
    }

    private void removeCurrentTablePanle()
    {   layoutbox.getChildren().remove(tableglance.getpanel());
    }

    private void displayTableHoverPanle(Table table)
    {   tableglance = new TableHoverPanel(table);
        layoutbox.getChildren().add(tableglance.getpanel());
    }

// grid set-up
    private void gridSetUp()
    {   topgrid.setPadding(new Insets(5,5,5,5));
        topgrid.setVgap(5);
        topgrid.setHgap(5);
        buttonGrid.setPadding(new Insets(10,5,10,5));
        buttonGrid.setVgap(5);
        buttonGrid.setHgap(5);
        buttonGrid.setId("GridPane_button");
    }

// further abstraction could be implemented
    private void tableNameFiled()
    {   tablenameField.setPromptText("Enter table name");
        GridPane.setConstraints(tablenameField,0,0,2,1);
        buttonGrid.getChildren().add(tablenameField);
    }

    private void delTableNameFiled()
    {   delTablenameField.setPromptText("Enter table name");
        GridPane.setConstraints(delTablenameField ,0,1,2,1);
        buttonGrid.getChildren().add(delTablenameField );
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

// try add a table when clikced using the name entered
    private void addingButton()
    {   Button btn = new Button("Add Table");
        btn.setOnAction(e->
        {   if(addNewTable(tablenameField.getText()))
            tablenameField.clear();
        });
        GridPane.setConstraints(btn,2,0);
        buttonGrid.getChildren().add(btn);
    }

// try deleting the table when clikced
// remove the peek view of the table in a special situation
// where the table is being displayed and then deleted at the same time
    private void deleteButton()
    {   Button btn = new Button("Delete Table");
        btn.setOnAction(e->
        {   String name = delTablenameField.getText();
            if(delTable(name))
            {   if(tableglance.getTable().getname().equals(name)) removeCurrentTablePanle();
                delTablenameField.clear();
            }
        });
        GridPane.setConstraints(btn,2,1);
        buttonGrid.getChildren().add(btn);
    }

// try load the table specified by the name entered by user
// if successful, rewind and reload all the new information to the window
    private void loadingButton()
    {   Button btn = new Button("Load From");
        btn.setOnAction(e->
        {   if(!database.readfrom(LoadDbnameField.getText())) return;
            clearTableBtns();
            loadfromDatabase();
            updateDatabaseName();
            LoadDbnameField.clear();
        });
        GridPane.setConstraints(btn,2,1);
        topgrid.getChildren().add(btn);
    }

// try save the database to disk using the entered name, if successful update the window name
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

//update window name
    private void updateDatabaseName()
    {   mainwindow.setTitle("ChaoDB: "+database.getname());
    }

//try deleting table in model & view, the latter is done using a shortcut by redo everything
    private boolean delTable(String name)
    {   if(database.removeTable(name))
        {   clearTableBtns();
            loadfromDatabase();
            return true;
        }
        return false;
    }

//try adding a new table with the name entered
    private boolean addNewTable(String name)
    {   if(!database.addTable(name)) return false;
        addtableButton(name,database.getTable(name));
        return true;
    }

    private Button addtableButton(String name, Table table)
    {   Button btn = new Button(name);
        tableBtns.add(btn);
        btn.setStyle("-fx-text-fill: rgb(88,144,255); -fx-font-weight: bold;");
        btn.setPrefWidth(90);
        btn.setOnAction(e->
        {   TableWindow newview = new TableWindow(table);
            newview.launch();
            // refrash the table hover panel
            removeCurrentTablePanle();
            displayTableHoverPanle(table);
        });
        hoverlisten(btn,table);
        addtogrid(btn);
        return btn;
    }

// add table button to the grid, automatically adjust the position
    private void addtogrid(Button btn)
    {   GridPane.setConstraints(btn,x,y);
        positioning();
        buttonGrid.getChildren().add(btn);
    }

    private void positioning()
    {   if(x>1)
        {   y++;
            x=0;
        }
        else x++;
    }

// clear up the window, remove all the table buttons
    private void clearTableBtns()
    {   for(Button btn : tableBtns)
        {   buttonGrid.getChildren().remove(btn);
        }
        x=x0; y=y0;
    }

//load all the tables to table buttons
    private void loadfromDatabase()
    {   for(Table t : database.getAllTables())
        {   addtableButton(t.getname(),t);
        }
    }

// load demo database
    private void tableDemoInit()
    {   if(!database.readfrom("demo")) return;
        loadfromDatabase();
        updateDatabaseName();
    }
}
