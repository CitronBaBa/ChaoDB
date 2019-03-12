import java.util.*;

class Table
{   private List<Column<String>> lib = new LinkedList<>();
    private int size = 0;
    private int width = 0;

    public static void main(String[] args)
    {   Table t = new Table();
        t.add_column("int","lobster");
        t.add_column("string","basil");
        t.add_column("float","pork");

        Scanner scan0 = new Scanner(System.in);
        while(true)
        {   System.out.println("input0, input1, input2, .....");
            String value = scan0.nextLine();
            if(!t.row_entry(value)) System.out.println("row entry failed");
            t.print_table();
            /*
            List<String> q = t.query(0);
            for(int i=0;i<q.size();i++) System.out.println(q.get(i));*/
        }
    }

    public int getsize() { return size;}
    public int getwidth() { return width;}

    public String get_col_name(int index)
    {   return lib.get(index).getname();
    }

    public List<String> query(int index)
    {   if(index>size)
        {   System.out.println("query index is out of range");
            System.exit(0);
        }
        List<String> answer = new ArrayList<String>();
        for(int i=0;i<lib.size();i++)
        {   if(lib.get(i).get(index)==null) answer.add("");
            else answer.add(lib.get(i).get(index).toString());
        }
        return answer;
    }

    public void delete_row(int index)
    {   for(Column c: lib)
        {   c.remove(index);
        }
        size--;
    }

    public boolean add_column(String userchoice, String col_name)
    {   TYPE type = String_handler.type_read(userchoice);
        if(type==null) return false;
        Column<String> id = new Column<String>(type,col_name);
        lib.add(id);
        width ++;
        for(int i=0;i<size;i++)
        {   lib.get(width-1).add(null);
        }
        return true;
    }

    public boolean del_column()
    {   if(width<1) return false;
        lib.remove(width-1);
        width --;
        return true;
    }

//regex mechanism could be refined, and could be moved to String_handler
    public boolean row_entry(String values)
    {   // nasty way to process (1,ss,1.0) like string input
        if(values.charAt(0)!='(' || values.charAt(values.length()-1)!=')') return false;
        String[] entrys = values.split(",");
        int len = entrys.length;
        entrys[0] = entrys[0].substring(1);
        entrys[len-1] = entrys[len-1].substring(0,entrys[len-1].length()-1);

        if(entrys.length!=lib.size())
        {   System.out.println("Wrong number inputs for one row of this table");
            return false;
        }

        if(!row_typecheck(entrys)) return false;
        row_write(entrys);
        size++;
        return true;
    }

    private boolean row_typecheck(String entrys[])
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

    private void row_write(String entrys[])
    {   int i = 0;
        for(Column<String> c : lib)
        {   col_write(c,entrys[i]);
            i++;
        }
    }

    private void col_write(Column<String> c,String value)
    {   if(value.equals(""))
        {   c.add(null);
            return;
        }
        c.add(value);
    }
    public void print_table()
    {   for(int i=0;i<size;i++)
        {   System.out.print("  ");
            for(Column<String> c : lib)
            {    if(i<c.size()) System.out.print(c.get(i)+"  ");
                 else System.out.print("null"+"  ");
            }
            System.out.print("\n");
        }
    }
}
class<T EXTENDS type>
class Column<T> extends ArrayList<T>
{   private TYPE type;
    private String name;
    Column(TYPE type, String name)
    {   this.type = type;
        this.name = name;
    }

    public TYPE getype()
    {   return type;
    }

    public String getname()
    {   return name;
    }

    public boolean typecheck(String value)
    {   // null item handling
        if(value.equals(""))
        {   return true;
        }

        //string col always gets entered
        if(type==TYPE.strings)
        {   return true;
        }

        //other types
        if( type != String_handler.type_judge(value) ) return false;

        return true;
    }

}
