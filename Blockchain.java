import java.util.Random;

public class Blockchain {

	public int length = 0;
	
	public int nonce; //in our simulation, the nonce is a random number. Each miner guesses numbers until one of them finds the nonce.
	//no hashing or proof of work involved
	
	public void newNonce(){
		Random rand = new Random();
		nonce = rand.nextInt(10000000);
	}
	public void newBlock(){
		length++;
	}
}
