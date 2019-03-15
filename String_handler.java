
/* This is a selection of methods used to handle strings in database
   test methods are private non static;
   other methods are static to make it easier for others to call;
*/

class String_handler
{
// converting string input to Type
// only (int float string boolean) are allowed
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

// process input string, such as (1,Lucy,1996,true), into a string array
    public static String[] processRowEntry(String values)
    {   if(values.charAt(0)!='(' || values.charAt(values.length()-1)!=')') return null;
        String[] entrys = values.split(",");
        int len = entrys.length;
        entrys[0] = entrys[0].substring(1);
        entrys[len-1] = entrys[len-1].substring(0,entrys[len-1].length()-1);
        return entrys;
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

    public static TYPE typeJudge(String value)
    {   boolean num = true;
        boolean inte = true;
        boolean decimal = false;
        boolean positive = true;
        int decimalcount=0;
        char a;

        if(value.equals("true")) return TYPE.booleans;
        if(value.equals("false")) return TYPE.booleans;

        // numbers
        if(value.charAt(0)=='+' || value.charAt(0)=='-' )
        {   if(value.charAt(0)=='-') positive = false;
            value = value.substring(1);
        }

        for(int i=0;i<value.length() && num;i++)
        {   a = value.charAt(i);
            if(a>'9' || a<'0' && a!='.') num = false;
            if(a=='.') { inte = false; decimalcount++; }
            if(a=='.' && decimalcount==1 && i!=0 && i!=value.length()-1) decimal = true;
        }
        if(num && decimal && decimalcount==1) return TYPE.floats;
        if(num && inte)
        {   if(intOverFlow(value,positive)) return TYPE.strings;
            return TYPE.ints;
        }
        return TYPE.strings;
    }

    private static boolean intOverFlow(String value, boolean positive)
    {   String max = "2147483647";
        if(!positive) max = "2147483648";
        if(value.length()>10) return true;
        if(value.length()<10) return false;
        for(int i=0;i<value.length();i++)
        {   if(value.charAt(i)>max.charAt(i)) return true;
        }
        return false;
    }

    public static boolean toboolean(String value)
    {   if(value.equals("true")) return true;
        else return false;
    }

    public static void main(String[] args)
    {   String_handler testing = new String_handler();
        testing.test();
        System.out.println("test passed");
    }

    private void test()
    {   testJudgeType();
        testJudgeName();
        testEntryHandling();
    }

    private void testJudgeType()
    {   assert(typeJudge("1")==TYPE.ints);
        assert(typeJudge("+13333333")==TYPE.ints);
        assert(typeJudge("-13333333")==TYPE.ints);
        assert(typeJudge("2147483647")==TYPE.ints);
        assert(typeJudge("-2147483648")==TYPE.ints);
        assert(typeJudge("-2147483649")==TYPE.strings);
        assert(typeJudge("+2147483648")==TYPE.strings);
        assert(typeJudge("1.111")==TYPE.floats);
        assert(typeJudge("0.")==TYPE.strings);
        assert(typeJudge(".0")==TYPE.strings);
        assert(typeJudge("sssss")==TYPE.strings);
    }

    private void testJudgeName()
    {   assert(judgeName("  s")==false);
        assert(judgeName("  ")==false);
        assert(judgeName("")==false);
    }

    private void testEntryHandling()
    {   String[] result = processRowEntry("(33,William,284.03)");
        assert(result[0].equals("33"));
        assert(result[1].equals("William"));
        assert(result[2].equals("284.03"));
    }
}
