import java.util.*;
import java.io.*;

/*  Column has all the information about one column in the table
    Types are implemented using inheritance, and a TYPE enum
    each children has a arraylist of data with its specific type
    so that the actual figure is stored in real int/float/etc.. instead of just string
    it also has TYPE enum to specifiy the type of data it is storing
    so later inputs' type compliance can be checked
*/

public class Column implements Serializable
{   private static final long serialVersionUID = 1000L;
    private TYPE type;
    private String name;
    private boolean iskey;

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

//check whether the input has the same type with this coulmn's type
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
        if( type != String_handler.typeJudge(value) ) return false;

        return true;
    }

// check whether new value is unique if there is a key constrain
    public boolean newKeyCheck(String value)
    {   if(!iskey)return true;
        if(contains(value)) return false;
        return true;
    }

// check whether the whole column has no duplicate value, if there is a key constrain
    public boolean keyConstrainCheck()
    {   List datalist = giveData();
        if(!iskey) return true;
        HashSet<String> record = new HashSet<>();
        for(int i=0;i<datalist.size();i++)
        {   String value;
            if(datalist.get(i)==null) value = "null";
            value = datalist.get(i).toString();
            if(!record.add(value)) return false;
        }
        return true;
    }

/* giveData() method is only used to override and to give different list of data for
   key constrain check within the column class, should never be used by outside class
   it is a way around to "override parent field" */
   
    public List giveData(){   return null; };
    public String get(int index){ return null; } ;
    public void add(String vale){};
    public void remove(int index){};
    public boolean contains(String value){ return false;};

    public static void main(String[] args)
    {   Column testing = new Column(null,null,false);
        testing.test();
        System.out.println("test passed");
    }

    private void test()
    {   StringColumn testCol = new StringColumn(TYPE.strings,"testCol",true);
        testCol.add("fish");testCol.add("food");testCol.add("steak");
        testCol.add("lemon");testCol.add("barbecue");testCol.add("lobster");
        testCol.add("lobster");
        assert(testCol.contains("fish"));
        assert(testCol.keyConstrainCheck()==false);
        testCol.remove(6);
        assert(testCol.keyConstrainCheck()==true);
        assert(testCol.newKeyCheck("fish")==false);
        assert(testCol.newKeyCheck("oyster")==true);
    }

// each class implements a data type and modify the way it deals with string input

    static class StringColumn extends Column
    {   private static final long serialVersionUID = 1000L;
        private List<String> data = new ArrayList<>();
        public StringColumn(TYPE type, String name, boolean iskey)
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
        public List giveData(){   return this.data; };
    }

    static class FloatColumn extends Column
    {   private static final long serialVersionUID = 1000L;
        private List<Float> data = new ArrayList<>();
        public FloatColumn(TYPE type, String name, boolean iskey)
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
        public List giveData(){   return this.data; };
    }

    static class BooleanColumn extends Column
    {   private static final long serialVersionUID = 1000L;
        private List<Boolean> data = new ArrayList<>();
        public BooleanColumn(TYPE type, String name, boolean iskey)
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
        public List giveData(){   return this.data; };
    }

    static class IntColumn extends Column
    {   private static final long serialVersionUID = 1000L;
        private List<Integer> data = new ArrayList<>();
        public IntColumn(TYPE type, String name, boolean iskey)
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
        public List giveData(){   return this.data;};
    }

}
