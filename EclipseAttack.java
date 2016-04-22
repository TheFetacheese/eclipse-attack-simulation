import java.util.Random;
public class EclipseAttack{
public static int blockchain = 0; //"main blockchain": this might not make sense since there will be multiple at once


/*create a new miner
 * 
 */
public Miner newMiner() {
	Miner newMiner = new Miner(1);
	return newMiner;
}


}
