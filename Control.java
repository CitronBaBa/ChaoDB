import java.util.*;

class Control
{   private Map<String,Table> tables = new TreeMap<String,Table>();
    private String name;

    public Control(String name)
    {   this.name = name;
    }
    public String getname() {   return name;}
    public void setname(String name) {   this.name = name;}

    public boolean add_table(String name)
    {   if(tables.containsKey(name)) return false;
        if(!String_handler.judgeTbName(name)) return false;

        Table table = new Table(name);
        tables.put(name,table);
        return true;
    }

    public void remove_table(String name)
    {   tables.remove(name);
    }

    public Table get_table(String name)
    {   return tables.get(name);
    }

    public ArrayList<Table> getAllTables()
    {   ArrayList<Table> tables = new ArrayList<>();
        for(Map.Entry<String, Table> pair : this.tables.entrySet())
        {   tables.add(pair.getValue());
        }
        return tables;
    }

    public void savetodisk()
    {   ArrayList<Object> tables = new ArrayList<>();
        ArrayList<String> tablenames = new ArrayList<>();
        for (Map.Entry<String, Table> pair : this.tables.entrySet())
        {   tablenames.add(pair.getKey());
            tables.add(pair.getValue());
        }

        FileSystem filehandler = new FileSystem();
        filehandler.saveDatabase(name,tables,tablenames);
    }

// return false when there is no such filename
    public boolean readfrom(String databaseName)
    {   FileSystem filehandler = new FileSystem();

        Object[] result = filehandler.readDatabase(databaseName);
        if(result==null) return false;
        Table[] readtables = new Table[result.length];
        for(int i=0;i<result.length;i++)
        {   readtables[i] = (Table)result[i];
        }

        Map<String,Table> newtables = new TreeMap<String,Table>();
        for(int i=0;i<readtables.length;i++)
        {   Table t = readtables[i];
            newtables.put(t.getname(),t);
        }

        this.tables = newtables;
        this.name = databaseName;
        return true;
    }

}
