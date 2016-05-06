
public class Block {

	public int nonce;
	public int winner;
	public Block next;
	
	public Block(int n, int w){
		next = null;
		nonce = n;
		winner = w;
	}
	
	public Block getNext(){
		return next;
	}
	public int getNonce(){
		return nonce;
	}
	public void setNext(Block next){
		this.next = next;
		
	}
	public int getWinner(){
		return winner;
	}
}
