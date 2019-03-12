public class ColumnEntry
{   private String name;
    private String type;
    private int size;
    public ColumnEntry()
    {   this.name = "";
        this.type = "";
        this.size = 0;
    }
    public ColumnEntry(String name, String type, int size)
    {   this.name = name;
        this.type = type;
        this.size = size;
    }
    public String getName() {   return name;}
    public String getType() {   return type;}
    public int getSize() {   return size;}
    public void setName(String name){   this.name = name;}
    public void setType(String type){   this.type = type;}
    public void setSize(int size){   this.size = size;}
}
