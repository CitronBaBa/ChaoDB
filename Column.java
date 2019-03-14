import java.util.*;
import java.io.*;

public class Column implements Serializable
{   private static final long serialVersionUID = 1000L;
    private TYPE type;
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
        if( type != String_handler.typeJudge(value) ) return false;

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
    }

    //int out of range problem can be adressed
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
    }

}
