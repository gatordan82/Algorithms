/**
 * 
 */
package boggle;

/**
 * @author Dan
 *
 */
public class BoggleSolver
{

	public BoggleSolver(String[] dictionary)
	{
		
	}
	
	public Iterable<String> getAllValidWords(BoggleBoard board)
	{
		
	}
	
	public int scoreOf(String word)
	{
		int length = word.length();
		int score;
		
		switch (length)
		{
			case 0: case 1: case 2:
				score = 0;
			case 3: case 4:
				score = 1;
			case 5:
				score = 2;
			case 6:
				score = 3;
			case 7:
				score = 5;
			default:
				score = 11;
		}
			
		return score;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
