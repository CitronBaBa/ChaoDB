import java.util.*;
import java.io.*;

class Table implements Serializable
{   private static final long serialVersionUID = 1000L;
    private List<Column> lib = new LinkedList<>();
    private int size = 0;
    private int width = 0;
    private String name;

    public Table(String name)
    {   this.name = name;
    }
    public int getsize() { return size;}
    public int getwidth() { return width;}
    public String getname() { return name;}

    public String getColName(int index)
    {   return lib.get(index).getname();
    }

    public TYPE getColType(int index)
    {   return lib.get(index).getype();
    }

    public boolean getColIskey(int index)
    {   return lib.get(index).iskey();
    }

    public List<String> query(int index)
    {   if(index>size)
        {   System.out.println("query index is out of range");
            System.exit(0);
        }
        List<String> answer = new ArrayList<String>();
        for(int i=0;i<lib.size();i++)
        {   String s = lib.get(i).get(index);
            answer.add(s);
        }
        return answer;
    }

    public void deleteRow(int index)
    {   for(Column c: lib)
        {   c.remove(index);
        }
        size--;
    }

    public boolean addColumn(String typechoice, String col_name, String iskeychoice)
    {   if(String_handler.typeJudge(iskeychoice)!=TYPE.booleans) return false;
        Boolean iskey = String_handler.toboolean(iskeychoice);

        //empty key field column cannot be added afterwards
        if(iskey && size>0) return false;

        TYPE type = String_handler.typeRead(typechoice);
        if(type==null) return false;
        if(type==TYPE.ints)
        {   Column.IntColumn id = new Column.IntColumn(type,col_name,iskey);  lib.add(id);}
        if(type==TYPE.strings)
        {   Column.StringColumn id = new Column.StringColumn(type,col_name,iskey); lib.add(id);}
        if(type==TYPE.floats)
        {   Column.FloatColumn id = new Column.FloatColumn(type,col_name,iskey); lib.add(id);}
        if(type==TYPE.booleans)
        {   Column.BooleanColumn id = new Column.BooleanColumn(type,col_name,iskey);  lib.add(id);}
        width ++;

        //intialize column with null
        for(int i=0;i<size;i++)
        {   lib.get(width-1).add("");
        }
        return true;
    }

    public boolean delColumn()
    {   if(width<1) return false;
        lib.remove(width-1);
        width --;
        return true;
    }

    private boolean rowKeyConstrainCheck()
    {   for(Column c : lib)
        {   if(!c.keyConstrainCheck()) return false;
        }
        return true;
    }

    private boolean rowNewKeycheck(String[] values)
    {   for(int i=0;i<width;i++)
        {   Column c = lib.get(i);
            if(!c.newKeyCheck(values[i])) return false;
        }
        return true;
    }

    public boolean rowEntry(String values)
    {   // nasty way to process (1,ss,1.0) like string input
        if(values.charAt(0)!='(' || values.charAt(values.length()-1)!=')') return false;
        String[] entrys = values.split(",");
        int len = entrys.length;
        entrys[0] = entrys[0].substring(1);
        entrys[len-1] = entrys[len-1].substring(0,entrys[len-1].length()-1);

        if(entrys.length!=width)
        {   System.out.println("Wrong number inputs for one row of this table");
            return false;
        }

        if(!rowTypecheck(entrys)) return false;
        if(!rowNewKeycheck(entrys)) return false;

        rowWrite(entrys);
        size++;
        return true;
    }


    private boolean rowTypecheck(String entrys[])
    {   int i = 0;
        for(Column c : lib)
        {   if(!c.typecheck(entrys[i]))
            {   System.out.println("value: '"+entrys[i]+"' inputed "+"has a wrong type");
                return false;
            }
            i++;
        }
        return true;
    }

    private void rowWrite(String entrys[])
    {   int i = 0;
        for(Column c : lib)
        {   c.add(entrys[i]);
            i++;
        }
    }

    public void printTable()
    {   for(int i=0;i<size;i++)
        {   System.out.print("  ");
            for(Column c : lib)
            {    System.out.print(c.get(i)+"  ");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args)
    {   Table t = new Table("lobster");
        t.addColumn("int","lobster","false");
        t.addColumn("string","basil","false");
        t.addColumn("float","pork","false");

        Scanner scan0 = new Scanner(System.in);
        while(true)
        {   System.out.println("input0, input1, input2, .....");
            String value = scan0.nextLine();
            if(!t.rowEntry(value)) System.out.println("row entry failed");
            t.printTable();
        }
    }

}
