public class Miner {
  	public Random rand;
	public int mainBlockchain = 0; //what this miner believes the main blockchain to be
	public int ownBlockchain = 0;	//amount of blocks known that have not been added
	public int blocksWon = 0;	//amount of blocks that have been added to the main chain. This might get fucky with eclipse attacks
	public int miningPower = 0;
  
  	private Tuple[][] triedTable = new Tuple[64][64];
  	private Tuple[][] newTable = new Tuple[256][64];
	
	private long[] connections = new long[8]; 
	private int connectionCount = 0;
	private final long ID;
  	private String ipAddress;
  
	/* Begin attempting to discover a block on the chain. Each miner
	 *  is constantly guessing a random number (goal). When it hits the correct
	 *  number, it is rewarded with a block.
	 *  This code might actually be contained in the other file
	 */
	public Miner(int miningPower, String IP, Random r){ //TODO: add a unique ID (IP address?)
		this.miningPower = miningPower;
      	this.ipAddress = IP;
      	this.rand = r;
		this.ID = Math.abs((System.currentTimeMillis() ^ 0xffffffffffffffffL) & (long)~miningPower); //minor bitwise hashing; should be sufficient at preventing collisions with a respectable sample size
	}
  
  	public String get16Prefix()
    {
    	return this.ipAddress.split(".")[0]+this.ipAddress.split(".")[1];  
    }
  
  	long l = (addr.getID() ^ (~Long.parseLong(addr.get16Prefix(), 36) / Long.parseLong(src.get16Prefix(), 36))) % 32;
      	int Bucket = new Long(addr.getID() * Long.parseLong(addr.get16Prefix(), 36) * l).hashCode() % 256;
      	boolean check = false;
      	for(int i = 0; i < newTable[Bucket].length; i++)
        {
        	if(newTable[Bucket][i] == null)
            {
            	newTable[Bucket][i] = new Tuple(System.currentTimeMillis(), addr);
              	check = true;
              	break;
            }
        }
      	if(check)
        	return;
      	else //isTerrible + eviction
        {
          	boolean check2 = false;
        	for(int i = 0; i < newTable[Bucket].length; i++)
            {
            	if(System.currentTimeMillis() - newTable[Bucket][i].getTimestamp() > 60000) //if this address has been sitting in here for A WHOLE MINUTE...
                {
                	newTable[Bucket][i] = new Tuple(System.currentTimeMillis(), addr);
                  	check2 = true;
                  	break;
                }
            }
          	if(check2)
              	return;
          	else
            {
            	Tuple temp = new Tuple(0, null);
          		int tmp = 0;
          		for(int i = 0; i < 4; i++)
            	{
             		int j = rand.nextInt(64);
              		Tuple t = newTable[Bucket][j];
              		if(t.getTimestamp() > temp.getTimestamp())
                	{
                  		temp = t;
                  		tmp = j;
                	}
            	}
          		newTable[Bucket][tmp] = new Tuple(System.currentTimeMillis(), addr);
            }
        }
  
  	public void addToTriedTable(Miner m)
    {
    	long l = m.getID() ^ Long.parseLong(m.getIPAddress(), 36) % 4;
      	int Bucket = new Long((m.getID() * Long.parseLong(m.get16Prefix(), 36) * l)).hashCode() % 64;
      	boolean check = false;
      	for(int i = 0; i < triedTable[Bucket].length; i++)
        {
        	if(triedTable[Bucket][i] == null)
            {
              	triedTable[Bucket][i] = new Tuple(System.currentTimeMillis(), m);
              	check = true;
              	break;
            }
        }
      	if(check)
        	return;
      	else //bitcoin eviction
        {
          	Tuple temp = new Tuple(0, null);
          	int tmp = 0;
          	for(int i = 0; i < 4; i++)
            {
             	int j = rand.nextInt(64);
              	Tuple t = triedTable[Bucket][j];
              	if(t.getTimestamp() > temp.getTimestamp())
                {
                  	temp = t;
                  	tmp = j;
                }
            }
          	this.addToNewTable(temp.getMiner(), this);
          	triedTable[Bucket][tmp] = new Tuple(System.currentTimeMillis(), m);
        }
    }
  
	public void mineBlocks(int goal) {
		
	}
	
	public boolean hasAllConnections(){
		if(connectionCount==8){ 
			return true;
		}
		return false;
	}
	
	/* not sure how I want this to work yet
	 * 
	 */
	public void findConnections() {
            
	}
        
    /*
    * Another miner has found this miner on the pool Queue; establish a connection to him/her!
    */
    private void addConnection(Miner m)
    {
    	this.connections[this.connectionCount] = m.getID();
        this.connectionCount++;
    }
	/* announce all found blocks to connections
	 * 
	 */
	public void announceBlocks(){
		
	}
	/* when told of a block by a fellow connection, spread the good news
	 * to all other connections
	 */
	public void spreadAnouncement(){
		
	}
	/*miner has hit the goal
	 * 
	 */
	public void won(){
		
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
        
    public int getConnectionCount()
    {
        return this.connectionCount;
    }
	
	public long[] getConnections()
	{
		return this.connections;
	}
}
