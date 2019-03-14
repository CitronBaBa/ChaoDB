import java.util.*;
import java.io.*;

// using Object/Object[] to make this class independent
// from the object type, avoid dependecies

class FileSystem
{   private String root = "./data/";

    public void saveDatabase(String foldername, ArrayList<Object> tables, ArrayList<String> tablenames)
    {   File dir = new File(root+foldername);
        dir.mkdir();
        for (int i=0;i<tables.size();i++)
        {   writeto(root+foldername+"/"+tablenames.get(i),tables.get(i));
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
        Person cecile = new Person();
        Person cecile2 = new Person();
        ArrayList<Object> people = new ArrayList<>();
        people.add(cecile); people.add(cecile2);
        ArrayList<String> names = new ArrayList<>();
        names.add("p1");names.add("p2");

        handler.saveDatabase("people",people,names);
        Object[] result = handler.readDatabase("people");
        Person[] listofpeople = new Person[result.length];
        for(int i=0;i<listofpeople.length;i++)
        {   listofpeople[i] = (Person)result[i];
            System.out.println(listofpeople[i].name);
        }

        handler.writeto("file2.txt",cecile);
        Person cecilia = (Person)handler.readfrom("file2.txt");
        System.out.println(cecilia.brain.name);
    }
}

// class for testing
class Person implements Serializable
{   public String name = "Cecile";
    public Brain brain = new Brain();
}
class Brain implements Serializable
{   public String name = "inteli";
}
