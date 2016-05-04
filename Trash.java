import java.util.Random;
public class Trash extends Miner{
	private boolean isTrash = true;
    private final long ID;
    private String ipAddress;
    private Random rand;
    
    public Trash(String IP, Random r)
    {
        this.ipAddress = IP;
        this.rand = r;
        this.ID = (System.currentTimeMillis() ^ 0xffffffffffffffffL) & ~Long.parseLong(IP, 36);
    }
    
    public String get16Prefix()
    {
    	return this.ipAddress.split(".")[0]+this.ipAddress.split(".")[1];  
    }
    
    public boolean isTrash()
    {
    	return this.isTrash;
    }
    	
    /**
	* @return the identification number of this miner
	*/
	public long getID()
	{
		return this.ID;
	}
  
  	public String getIPAddress()
    {
    	return this.ipAddress;  
    }
}
