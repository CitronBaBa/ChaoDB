import java.util.*;
import java.io.*;

/* Do the job of saving tables to a folder with individual table file
   using Object/Object[] to make this class independent
   from the storing type, making it more reusable
*/

class FileSystem
{   private String root = "./data/";

// save a list of object with each individual file name
// could be improved by using a map instead of two lists
    public void saveDatabase(String foldername, ArrayList<Object> objects, ArrayList<String> tablenames)
    {   File dir = new File(root+foldername);
        dir.mkdir();
        for (int i=0;i<objects.size();i++)
        {   writeto(root+foldername+"/"+tablenames.get(i),objects.get(i));
        }
        // delete file that is not in the lists
        for(File f : dir.listFiles())
        {   if(!tablenames.contains(f.getName()))
            f.delete();
        }
    }

    public Object[] readDatabase(String foldername)
    {   File dir = new File(root+foldername);
        if(!dir.exists()) return null;
        if(foldername.equals("")) return null;
        File[] files = dir.listFiles();
        Object[] result = new Object[files.length];
        for(int i=0;i<files.length;i++)
        {   result[i] = readfrom(dir+"/"+files[i].getName());
        }
        return result;
    }

    public void deleteDatabase(String foldername)
    {   File dir = new File(root+foldername);
        for(File f : dir.listFiles())
        {   f.delete();
        }
        dir.delete();
    }

    public Object readfrom(String filename)
    {   FileInputStream file0 = null;
        Object targetobject = null;
        try
        {   file0 = new FileInputStream(filename);
            ObjectInputStream output = new ObjectInputStream(file0);

            targetobject = output.readObject();

            output.close();
        }
        catch (Exception ex)
        {   System.out.println("Exception: " + ex);
        }
        finally
        {   try{  if (file0!= null) file0.close();}
            catch (IOException e) { System.out.println("Exiterror"+e);}
        }
        return targetobject;
     }

    public <T> void writeto(String filename, T targetobject)
    {   FileOutputStream file0 = null;
        try
        {   File newfile = new File(filename);
            newfile.createNewFile();
            file0 = new FileOutputStream(newfile);
            ObjectOutputStream output = new ObjectOutputStream(file0);
            output.writeObject(targetobject);
            output.close();
            file0.flush();
        }
        catch (Exception ex)
        {   System.out.println("Exception: " + ex);
        }
        finally
        {   try{  if (file0!= null) file0.close();}
            catch (IOException e) { System.out.println("Exiterror"+e);}
        }
     }

    public static void main(String[] args)
    {   FileSystem handler = new FileSystem();
        handler.test();
        System.out.println("test passed");
    }

    private void test()
    {   groupTest();
        singleTest();
    }

    private void singleTest()
    {   Testclass cecile = new Testclass("Cecile");
        writeto("file2",cecile);
        Testclass cecilecopy = (Testclass)readfrom("file2");
        assert(cecilecopy.name.equals("Cecile"));
        assert(cecilecopy.subclass.name.equals("inteli"));
        File testfile = new File("file2");
        testfile.delete();
    }

    private void groupTest()
    {   Testclass[] listofTestclass = testPreparation();
        assert(listofTestclass[0].name.equals("Cecile"));
        assert(listofTestclass[1].name.equals("Daisy"));
        assert(listofTestclass[0].subclass.name.equals("inteli"));
        deleteDatabase("testDbFile");
    }

    private Testclass[] testPreparation()
    {   Testclass cecile = new Testclass("Cecile");
        Testclass daisy = new Testclass("Daisy");
        ArrayList<Object> people = new ArrayList<>();
        people.add(cecile); people.add(daisy);
        ArrayList<String> names = new ArrayList<>();
        names.add("testfile1");names.add("testfile2");

        saveDatabase("testDbFile",people,names);
        Object[] result = readDatabase("testDbFile");
        Testclass[] listofpeople = new Testclass[result.length];
        for(int i=0;i<listofpeople.length;i++)
        {   listofpeople[i] = (Testclass)result[i];
        }
        return listofpeople;
    }
}

// class for testing
class Testclass implements Serializable
{   public String name;
    public Testsubclass subclass = new Testsubclass();
    Testclass(String name)
    {   this.name = name;
    }
}
class Testsubclass implements Serializable
{   public String name = "inteli";
}
