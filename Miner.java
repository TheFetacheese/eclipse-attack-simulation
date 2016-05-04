public class Miner {
	public int mainBlockchain = 0; //what this miner believes the main blockchain to be
	public int ownBlockchain = 0;	//amount of blocks known that have not been added
	public int blocksWon = 0;	//amount of blocks that have been added to the main chain. This might get fucky with eclipse attacks
	public int miningPower = 0;
	
	private long[] connections = new long[8]; //arbitrarily set to have each miner have at most 6 connections. IDs of other Miners will be stored in here.
	private int connectionCount = 0;
	private final long ID;
	/* Begin attempting to discover a block on the chain. Each miner
	 *  is constantly guessing a random number (goal). When it hits the correct
	 *  number, it is rewarded with a block.
	 *  This code might actually be contained in the other file
	 */
	public Miner(int miningPower){ //TODO: add a unique ID (IP address?)
		this.miningPower = miningPower;
		for (int i=0; i < miningPower; i++) {
			
		}
		this.ID = Math.abs((System.currentTimeMillis() ^ 0xffffffffffffffffL) & (long)~miningPower); //minor bitwise hashing; should be sufficient at preventing collisions with a respectable sample size
	}
	public void mineBlocks(int goal) {
		
	}
	
	public boolean hasAllConnections(){
		if(true){ //TODO: all connections must be filled
			return true;
		}
		return false;
	}
	
	/* not sure how I want this to work yet
	 * 
	 */
	public void findConnections(Queue pool) {
            if(connectionCount < 8 && pool.peek() != null && pool.peek() != this) {
                Miner m = pool.peek();
                //search for this Miner node in our present connections
                boolean newNode = true;
                for(long l : this.connections)
                {
                    if(m.getID() == l)
                    {
                        newNode = false;
                        break;
                    }
                }
                pool.dequeue();
                if(newNode)
                {
                    this.connections[this.connectionCount] = m.getID();
                    this.connectionCount++;
                    m.addConnection(this);
                }
                else
                    findConnections(pool);
            }
	}
        
        /*
        * Another miner has found this miner on the pool Queue; establish a connection to him/her!
        */
        public void addConnection(Miner m)
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
        
        public int getConnectionCount()
        {
            return this.connectionCount;
        }
	
	public long[] getConnections()
	{
		return this.connections;
	}
}
