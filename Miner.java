import java.util.Arrays;
import java.util.Random;

public class Miner {
  	public Random rand;
	public int mainBlockchain = 0; //what this miner believes the main blockchain to be
	public int ownBlockchain = 0;	//amount of blocks known that have not been added
	public int blocksWon = 0;	//amount of blocks that have been added to the main chain. This might get fucky with eclipse attacks
	public int miningPower = 0;
	public Blockchain myBlockchain;
	public int currentNonce;
	
	public final double networkNoise = 0.0001;
  
  	private Tuple[][] triedTable = new Tuple[64][64];
  	private int triedCount = 0;
  	private Tuple[][] newTable = new Tuple[256][64];
  	private int newCount = 0;
  	
  	private boolean isTrash = false;
	
	private Miner[] connections = new Miner[8]; 
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
      		myBlockchain = new Blockchain<Block>();
		this.ID = Math.abs((System.currentTimeMillis() ^ 0xffffffffffffffffL) & (long)~miningPower -r.nextInt(93710)); //minor bitwise hashing; should be sufficient at preventing collisions with a respectable sample size
	}
  
  	public String get16Prefix()
    	{
    		return this.ipAddress.split(".")[0]+this.ipAddress.split(".")[1];  
    	}
  
  	public void addToNewTable(Miner addr, Miner src)
    	{
    		long l = (addr.getID() ^ (~Long.parseLong(addr.get16Prefix(), 36) / Long.parseLong(src.get16Prefix(), 36))) % 32;
      		int Bucket = new Long(addr.getID() * Long.parseLong(addr.get16Prefix(), 36) * l).hashCode() % 256;
      		boolean check = false;
      		for(int i = 0; i < newTable[Bucket].length; i++)
        	{
        		if(newTable[Bucket][i] == null)
            		{
            			newTable[Bucket][i] = new Tuple(System.currentTimeMillis(), addr);
              			check = true;
              			newCount++;
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
              			triedCount++;
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
  	public void setBlockchain(Blockchain<Block> b){
		myBlockchain = b;
	}

	public void mineBlocks(int goal) { //TODO star of the show here
		currentNonce = goal;
		Random rand = new Random();
		for (int i=0; i < miningPower; i++){
			if (rand.nextInt(200) == goal){
				won();
				break;
			}
		}
	announceBlocks();
	}
	
	public boolean hasAllConnections(){
		if(connectionCount==8){ 
			return true;
		}
		return false;
	}
	
	public void findConnections()
	{
        	if(this.hasAllConnections())
        		return;
        	for(int i = connectionCount; i < connections.length; i++)
			findConnectionsHelper(0);
        }
        
        public void findConnectionsHelper(int r)
        {
        	int rejections = r;
		double ratio = Math.sqrt((double)triedCount/(double)newCount);
            	boolean useTried = rand.nextDouble() < ((ratio*(9-connectionCount))/((connectionCount + 1) + (ratio * (9-connectionCount))));
            	if(useTried)
            	{
            		while(true)
            		{
            			int randBucket = rand.nextInt(64);
            			boolean canUse = false;
            			for(int i = 0; i < triedTable[randBucket].length; i++)
            			{
            				if(triedTable[randBucket][i] != null)
            				{
            					canUse = true;
            					break;
            				}
            			}
            			if(canUse)
            			{
            				int pos = rand.nextInt(64);
            				if(triedTable[randBucket][pos] != null)
            				{
            					double difference = (System.currentTimeMillis() - triedTable[randBucket][pos].getTimestamp())/1000L;
            					boolean reject = rand.nextDouble() > Math.min(1, Math.pow(1.2, rejections)/(1+difference));
            					if(reject)
            					{
            						findConnectionsHelper(rejections++);
            						return;
            					}
            					else
            					{
            						boolean connect = establishConnection(triedTable[randBucket][pos].getMiner());
            						if(!connect)
            						{
            							findConnectionsHelper(rejections);
            						}
            						else
            							return;
            					}
            				}
            			}
            		}
            	}
            	else //use new table; main difference being that new table has 256 buckets and if we successfully connect we add the entry to the tried table, removing it from the new table
            	{
            		while(true)
            		{
            			int randBucket = rand.nextInt(256);
            			boolean canUse = false;
            			for(int i = 0; i < newTable[randBucket].length; i++)
            			{
            				if(newTable[randBucket][i] != null)
            				{
            					canUse = true;
            					break;
            				}
            			}
            			if(canUse)
            			{
            				int pos = rand.nextInt(64);
            				if(newTable[randBucket][pos] != null)
            				{
            					double difference = (System.currentTimeMillis() - newTable[randBucket][pos].getTimestamp())/1000L;
            					boolean reject = rand.nextDouble() > Math.min(1, Math.pow(1.2, rejections)/(1+difference));
            					if(reject)
            					{
            						findConnectionsHelper(rejections++);
            						return;
            					}
            					else
            					{
            						boolean connect = establishConnection(newTable[randBucket][pos].getMiner());
            						if(!connect)
            						{
            							findConnectionsHelper(rejections);
            						}
            						else
            						{
            							Tuple t = newTable[randBucket][pos];
            							newTable[randBucket][pos] = null;
            							newCount--;
            							this.addToTriedTable(t.getMiner());
            							return;
            						}
            					}
            				}
            			}
            		}
            	}
        }
        
        public boolean establishConnection(Miner m)
        {
        	if(m.isTrash() || rand.nextDouble() < networkNoise) //1:10000 chance of failing due to network error
        		return false;
        	this.connections[connectionCount++] = m;
        	return true;
        }
        
        public boolean isTrash()
        {
        	return this.isTrash;
        }
        
        public void reset()
        {
        	for(int i = 0; i < connections.length; i++)
        		connections[i] = null;
        	connectionCount = 0;
        	findConnections();
        }
        
    	/* announce all found blocks to connections
	 * 
	 */
        
    public Blockchain<Block> getBlockchain(){
    	return myBlockchain;
    }
	public void announceBlocks(){

		for(int i=0; i< connections.length; i++){
			if(connections[i] != null){
		 if (connections[i].getBlockchain().hashingValue != myBlockchain.hashingValue){
			 if (myBlockchain.length > connections[i].getBlockchain().length){
				 connections[i].setBlockchain(myBlockchain);
				 System.out.println(connections[i].ID + " now has hash: " + connections[i].getBlockchain().hashingValue);
			 }
		 }
			}
		}
	}
	/* when told of a block by a fellow connection, spread the good news
	 * to all other connections
	 */
	public void alertPartner(){
		/* if (my blockchain > their blockchain)
		 *  update their blockchain
		 */
		
	}
	/*miner has hit the goal
	 * 
	 */
	public void won(){
		myBlockchain.addBlock(currentNonce, (int)ID);
		ownBlockchain++;
		blocksWon++;//TODO:remove this at some point
		System.out.println(ID +" won and has hash: " +myBlockchain.getHash());
		System.out.println("Blockchain is now: " + myBlockchain.length);
		announceBlocks();
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
    
    public void connect(Miner target){
    	if (Arrays.asList(connections).contains(target) == false && target.hasAllConnections() == false/*&& Arrays.asList(target.connections).contains(this) == false*/){
    		connections[connectionCount] = target;
    		target.connections[target.connectionCount] = this;
    		connectionCount++;
    		target.connectionCount++;
    	}
    }
	
	/*public long[] getConnections() //TODO IS THIS SUPPOSED TO BE A LONG OR A MINER
	{
		return this.connections;
	}*/
}
