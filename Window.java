import java.util.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.value.*;

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

    private void hoverlisten(Button btn, Table table)
    {   btn.hoverProperty().addListener
        ((ObservableValue<? extends Boolean> observable, Boolean oldhovered, Boolean newhovered)
         -> {   if (newhovered)
                {   hoverListen(table);
                }
            }
        );
    }

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

    private void gridSetUp()
    {   topgrid.setPadding(new Insets(5,5,5,5));
        topgrid.setVgap(5);
        topgrid.setHgap(5);
        buttonGrid.setPadding(new Insets(10,5,10,5));
        buttonGrid.setVgap(5);
        buttonGrid.setHgap(5);
        buttonGrid.setId("GridPane_button");
    }

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

    private void addingButton()
    {   Button btn = new Button("Add Table");
        btn.setOnAction(e->
        {   if(addNewTable(tablenameField.getText()))
            tablenameField.clear();
        });
        GridPane.setConstraints(btn,2,0);
        buttonGrid.getChildren().add(btn);
    }

    private void deleteButton()
    {   Button btn = new Button("Delete Table");
        btn.setOnAction(e->
        {   if(delTable(delTablenameField.getText()))
            {   removeCurrentTablePanle();
                tablenameField.clear();
            }
        });
        GridPane.setConstraints(btn,2,1);
        buttonGrid.getChildren().add(btn);
    }

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

    private void updateDatabaseName()
    {   mainwindow.setTitle("ChaoDB: "+database.getname());
    }

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

    private boolean addNewTable(String name)
    {   if(!database.addTable(name)) return false;
        tableButton(name,database.getTable(name));
        return true;
    }

    private boolean delTable(String name)
    {   if(database.removeTable(name))
        {   clearTableBtns();
            loadfromDatabase();
            return true;
        }
        return false;
    }

    private Button tableButton(String name, Table table)
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

    private void clearTableBtns()
    {   for(Button btn : tableBtns)
        {   buttonGrid.getChildren().remove(btn);
        }
        x=x0; y=y0;
    }

    private void loadfromDatabase()
    {   for(Table t : database.getAllTables())
        {   tableButton(t.getname(),t);
        }
    }

    private void tableDemoInit()
    {   if(!database.readfrom("demo")) return;
        loadfromDatabase();
        updateDatabaseName();
    }
}
