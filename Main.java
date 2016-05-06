import java.util.Random;

public class Main {
    static Random rand = new Random(123456);
    public static void main(String[] args){
    	int temprand;
    	int i;
    	long start;
    	long stop;
        Miner[] miners = new Miner[1050];
        for(i = 0; i < 150; i++)
        {	
            miners[i] = new Miner(1, rand.nextInt(256)+"."+rand.nextInt(256)+"."+rand.nextInt(256)+"."+rand.nextInt(256), rand);
        }
        for(i = 150; i < 1050; i++){
        	miners[i] = new AttackerNode(1, rand.nextInt(256)+"."+rand.nextInt(256)+"."+rand.nextInt(256)+"."+rand.nextInt(256), rand);
        }
        for(i = 0; i < miners.length; i++){
        	start = System.currentTimeMillis();
        	stop = start + 3000; //some poor nodes don't get 8 friends, this silences their hopes and dreams
        	while (miners[i].hasAllConnections() == false){
        		if (System.currentTimeMillis() < stop) {
        		temprand = rand.nextInt(miners.length);
        	if (temprand != i)
        		miners[i].connect(miners[temprand]);
        		}
        		else
        			break;
        	}
        	
        }
        for (int q=0; q < 10000; q++){
            for(int j = 0; j < i; j++)
       		{
            miners[j].mineBlocks(rand.nextInt(50));
          	}
        }
    }
    }
