public class Tuple {
 	 private long timestamp;
  	 private Miner min;
  	 
  	 public Tuple(long l, Miner m)
    {
   	 this.timestamp = l;
   	 this.min = m;
    }
  	 public long getTimestamp()
    {
    	return this.timestamp;  
    }
  	 public Miner getMiner()
    {
     return this.min; 
    }
}
