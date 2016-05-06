import java.util.Random;

public class Blockchain<T extends Block> {

	public int length = 0;
	public Block head;
	public int hashingValue = 0; //yes this is dumb
	public int nonce; //in our simulation, the nonce is a random number. Each miner guesses numbers until one of them finds the nonce.
	//no hashing or proof of work involved
	
	
	public Blockchain(){
		head = new Block(42,42); //genesis block
	}
	public void newNonce(){
		Random rand = new Random();
		nonce = rand.nextInt(10000000);
	}
	public void newBlock(){
		length++;
	}
	
	public int size(){
		return length;
	}
	
	public int getHash(){
		
		
		return hashingValue; //"hash"
	}
	
	public void addBlock(int n, int w){
		length++;
		hashingValue += (n * w) % 1200; //number isn't high enough for a real system but it should do here
		Block temp = new Block(n, w);
		Block current = head;
		while(current.getNext() != null){
			current = current.getNext();
			
		}
		current.setNext(temp);
	}
}
