import java.util.*;
import java.io.*;

class Table implements Serializable
{   private List<Column> lib = new LinkedList<>();
    private int size = 0;
    private int width = 0;
    private String name;

    public Table(String name)
    {   this.name = name;
    }
    public int getsize() { return size;}
    public int getwidth() { return width;}
    public String getname() { return name;}

    public String get_col_name(int index)
    {   return lib.get(index).getname();
    }

    public TYPE get_col_type(int index)
    {   return lib.get(index).getype();
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

    public void delete_row(int index)
    {   for(Column c: lib)
        {   c.remove(index);
        }
        size--;
    }

    public boolean add_column(String typechoice, String col_name, String iskeychoice)
    {   if(String_handler.type_judge(iskeychoice)!=TYPE.booleans) return false;
        Boolean iskey = String_handler.toboolean(iskeychoice);

        //empty key field column cannot be added afterwards
        if(iskey && size>0) return false;

        TYPE type = String_handler.type_read(typechoice);
        if(type==null) return false;
        if(type==TYPE.ints)
        {   Int_column id = new Int_column(type,col_name,iskey);  lib.add(id);}
        if(type==TYPE.strings)
        {   String_column id = new String_column(type,col_name,iskey); lib.add(id);}
        if(type==TYPE.floats)
        {   Float_column id = new Float_column(type,col_name,iskey); lib.add(id);}
        if(type==TYPE.booleans)
        {   Boolean_column id = new Boolean_column(type,col_name,iskey);  lib.add(id);}
        width ++;

        //intialize column with null
        for(int i=0;i<size;i++)
        {   lib.get(width-1).add("");
        }
        return true;
    }

    public boolean del_column()
    {   if(width<1) return false;
        lib.remove(width-1);
        width --;
        return true;
    }

    private boolean row_keyconstrain_check()
    {   for(Column c : lib)
        {   if(!c.keyConstrainCheck()) return false;
        }
        return true;
    }

    private boolean row_new_keycheck(String[] values)
    {   for(int i=0;i<width;i++)
        {   Column c = lib.get(i);
            if(!c.newKeyCheck(values[i])) return false;
        }
        return true;
    }

    public boolean row_entry(String values)
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

        if(!row_typecheck(entrys)) return false;
        if(!row_new_keycheck(entrys)) return false;

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
        for(Column c : lib)
        {   c.add(entrys[i]);
            i++;
        }
    }

    public void print_table()
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
        t.add_column("int","lobster","false");
        t.add_column("string","basil","false");
        t.add_column("float","pork","false");

        Scanner scan0 = new Scanner(System.in);
        while(true)
        {   System.out.println("input0, input1, input2, .....");
            String value = scan0.nextLine();
            if(!t.row_entry(value)) System.out.println("row entry failed");
            t.print_table();
        }
    }

}

class Column implements Serializable
{   private TYPE type;
    private String name;
    private boolean iskey;
    private List data;

    Column(TYPE type, String name, boolean iskey)
    {   this.type = type;
        this.name = name;
        this.iskey = iskey;
    }

    public TYPE getype()
    {   return type;
    }

    public String getname()
    {   return name;
    }

    public boolean iskey()
    {   return iskey;
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

// check whether new value can be a unique key
    public boolean newKeyCheck(String value)
    {   if(!iskey)return true;
        if(contains(value)) return false;
        return true;
    }

// check whether the whole column has no duplicate value
    public boolean keyConstrainCheck()
    {   if(!iskey) return true;
        HashSet<String> record = new HashSet<>();
        for(int i=0;i<data.size();i++)
        {   String value;
            if(data.get(i)==null) value = "null";
            value = data.get(i).toString();
            if(!record.add(value)) return false;
        }
        return true;
    }

    public String get(int index){ return null; } ;
    public void add(String vale){};
    public void remove(int index){};
    public boolean contains(String value){ return false;};
}

class String_column extends Column
{   private List<String> data = new ArrayList<>();
    public String_column(TYPE type, String name, boolean iskey)
    {   super(type,name,iskey);
    }
    public String get(int index)
    {   if (data.get(index)==null) return "null";
        return data.get(index);
    }
    public void add(String value)
    {   if(value.equals("")) data.add(null);
        else data.add(value);
    }
    public boolean contains(String value)
    {   if(value.equals("")) return data.contains(null);
        return data.contains(value);
    }
    public void remove(int index)
    {   data.remove(index);
    }
}

class Float_column extends Column
{   private List<Float> data = new ArrayList<>();
    public Float_column(TYPE type, String name, boolean iskey)
    {   super(type,name,iskey);
    }
    public String get(int index)
    {   if (data.get(index)==null) return "null";
        return data.get(index).toString();
    }
    public void add(String value)
    {   if(value.equals("")) data.add(null);
        else data.add(Float.valueOf(value));
    }
    public boolean contains(String value)
    {   if(value.equals("")) return data.contains(null);
        return data.contains(Float.valueOf(value));
    }
    public void remove(int index)
    {   data.remove(index);
    }
}

class Boolean_column extends Column
{   private List<Boolean> data = new ArrayList<>();
    public Boolean_column(TYPE type, String name, boolean iskey)
    {   super(type,name,iskey);
    }
    public String get(int index)
    {   if (data.get(index)==null) return "null";
        return data.get(index).toString();
    }
    public void add(String value)
    {   if(value.equals("")) data.add(null);
        else data.add(String_handler.toboolean(value));
    }
    public boolean contains(String value)
    {   if(value.equals("")) return data.contains(null);
        return data.contains(String_handler.toboolean(value));
    }
    public void remove(int index)
    {   data.remove(index);
    }
}

//int out of range problem can be adressed
class Int_column extends Column
{   private List<Integer> data = new ArrayList<>();
    public Int_column(TYPE type, String name, boolean iskey)
    {   super(type,name,iskey);
    }
    public String get(int index)
    {   if (data.get(index)==null) return "null";
        return data.get(index).toString();
    }
    public void add(String value)
    {   if(value.equals("")) data.add(null);
        else data.add(Integer.decode(value));
    }
    public boolean contains(String value)
    {   if(value.equals("")) return data.contains(null);
        return data.contains(Integer.decode(value));
    }
    public void remove(int index)
    {   data.remove(index);
    }
}
