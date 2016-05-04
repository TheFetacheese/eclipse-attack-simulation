
public class Block {

	public int nonce;
	public int winner;
	
	public BlockchainNode(int n, int w){
		nonce = n;
		winner = w;
	}
	
	public int getNonce(){
		return nonce;
	}
	
	public int getWinner(){
		return winner;
	}
}
