import java.util.Random;

public class Main {
    static Random rand = new Random(123456);
    public static void main(String[] args){
        Miner[] miners = new Miner[45];
        for(int i = 0; i < miners.length; i++)
        {
            miners[i] = new Miner(rand.nextInt(500), rand.nextInt(256)+"."+rand.nextInt(256)+"."+rand.nextInt(256)+"."+rand.nextInt(256), rand);
    	    for(int j = 0; j < i; j++)
       		{
            
          	}
        }
    }
}
