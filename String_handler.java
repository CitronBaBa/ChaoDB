class String_handler
{   public static void main(String[] args)
    {   if(typeJudge(args[0])==TYPE.ints) System.out.println("it is a integer");
        if(typeJudge(args[0])==TYPE.floats) System.out.println("it is a float");
        if(typeJudge(args[0])==TYPE.booleans) System.out.println("it is a boolean");
        if(typeJudge(args[0])==TYPE.strings) System.out.println("it is a string");
    }

    public static TYPE typeRead(String userchoice)
    {   if(userchoice.equals("int")) return TYPE.ints;
        if(userchoice.equals("float")) return TYPE.floats;
        if(userchoice.equals("string")) return TYPE.strings;
        if(userchoice.equals("boolean")) return TYPE.booleans;
        return null;
    }

    public static String typeToString(TYPE type)
    {   if(type==TYPE.ints) return "int";
        if(type==TYPE.floats) return "float";
        if(type==TYPE.strings) return "string";
        if(type==TYPE.booleans) return "boolean";
        return null;
    }

    public static boolean judgeDbName(String name)
    {   return judgeName(name);
    }

    public static boolean judgeTbName(String name)
    {   return judgeName(name);
    }

    public static boolean judgeName(String name)
    {   if(name.startsWith(" ")) return false;
        if(name.equals("")) return false;
        return true;
    }

    // Reminder:
    // no support for long
    // no support for double, ".0" "0." are illegal

    public static TYPE typeJudge(String value)
    {   boolean num = true;
        boolean inte = true;
        boolean decimal = false;
        int decimalcount=0;
        char a;

        if(value.equals("true")) return TYPE.booleans;
        if(value.equals("false")) return TYPE.booleans;

        // numbers
        if(value.charAt(0)=='+' || value.charAt(0)=='-' )
        {   value = value.substring(1);
        }
        for(int i=0;i<value.length() && num;i++)
        {   a = value.charAt(i);
            if(a>'9' || a<'0' && a!='.') num = false;
            if(a=='.') { inte = false; decimalcount++; }
            if(a=='.' && decimalcount==1 && i!=0 && i!=value.length()-1) decimal = true;
        }
        if(num && decimal && decimalcount==1) return TYPE.floats;
        if(num && inte) return TYPE.ints;

        return TYPE.strings;
    }

    public static boolean toboolean(String value)
    {   if(value.equals("true")) return true;
        else return false;
    }

}
