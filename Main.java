import java.util.Random;

public class Main {
    static Random rand = new Random(123456);
    public static void main(String[] args){
        Miner[] miners = new Miner[45];
        Queue<Miner> connectionQueue = new Queue<>();
        for(int i = 0; i < miners.length; i++)
            miners[i] = new Miner(rand.nextInt(500));
        int j = 0;
        //this presently runs forever. I'm working on that.
        for(int i = 0; i < miners.length;)
        {
            connectionQueue.enqueue(miners[i]);
            if(j%4 == 0 && i < miners.length - 1)
            {
                connectionQueue.enqueue(miners[++i]);
            }
            j++;
            System.out.println(i);
            miners[i].findConnections(connectionQueue);
        }
    }
}