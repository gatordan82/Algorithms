

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver 
{
	private final double edgeEnergy = 1000;		// value of energy at picture edge
	
	private int         width; 
	private int         height;
	private int[][]     picColors;					// Current (altered) picture
	private double[][]  energy;
	private boolean[][] shift;
	private boolean     isTransposed;
	
	/**
     * Makes a deep copy of the image for alteration.
     *
     * @param picture the image to use for seam carving.
     */
	public SeamCarver(Picture picture)
	{
		width  = picture.width();
		height = picture.height();
		
		picColors    = new int[height][width];
		energy       = new double[height][width];
		shift        = new boolean[height][width];
		isTransposed = false;
		
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				picColors[y][x] = picture.get(x, y).getRGB();
				shift[y][x]  = false;
			}
		
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				energy[y][x] = energy(x, y);
	}
	
	/**
	 * Current instance of the picture
	 * 
	 * @return the current altered picture
	 */
	public Picture picture()
	{
		if (isTransposed) transposePicAndEnergy();
		
		Picture current = new Picture(width, height);
		
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				current.set(x, y, new Color(picColors[y][x]));
		
		return current;				
	}
	
	/**
	 * Width of current picture instance
	 * 
	 * @return width of the current picture in pixels
	 */
	public int width()
	{
		if (!isTransposed) return width;
		else return height;
	}
	
	/**
	 * Height of current picture instance
	 * 
	 * @return height of the current picture in pixels
	 */
	public int height()
	{
		if (!isTransposed) return height;
		else return width;
	}
	
	/**
	 * Calculate the energy of a pixel using the dual gradient energy function
	 * 
	 * @param x horizontal coordinate in pixels
	 * @param y vertical coordinate in pixels
	 * @return width of the current picture in pixels
	 */
	public double energy(int x, int y)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IndexOutOfBoundsException();
		
		if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
			return edgeEnergy;
		
		double dX2 = xGradient(x, y);
		double dY2 = yGradient(x, y);
		
		return Math.sqrt(dX2 + dY2);
	}
	
	private double gradient(Color tr, Color bl)
	{
		double dR = (double) tr.getRed()   - bl.getRed();
		double dG = (double) tr.getGreen() - bl.getGreen();
		double dB = (double) tr.getBlue()  - bl.getBlue();
		
		return Math.pow(dR, 2) + Math.pow(dG, 2) + Math.pow(dB, 2);
	}
	
	private double xGradient(int x, int y)
	{
		Color right = new Color(picColors[y][x + 1]);
		Color left  = new Color(picColors[y][x - 1]);
		
		return gradient(right, left);
	}
	
	private double yGradient(int x, int y)
	{
		Color top     = new Color(picColors[y + 1][x]);
		Color bottom  = new Color(picColors[y - 1][x]);
		
		return gradient(top, bottom);
	}
	
	public int[] findHorizontalSeam()
	{
		if (!isTransposed) transposePicAndEnergy();
		return findSeam();		
	}

	private void transposePicAndEnergy()
	{				
		int[][] tempPic 	  = new int[width][height];
		double[][] tempEnergy = new double[width][height];
		boolean[][] tempShift = new boolean[width][height];
		
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
	            tempPic[x][y]    = picColors[y][x];
	            tempEnergy[x][y] = energy[y][x];
	            tempShift[x][y]  = shift[y][x];
			}
		
		picColors = tempPic;
		energy    = tempEnergy;
		shift     = tempShift;
		
		int temp = width;
		width    = height;
		height   = temp;
		
		isTransposed = !isTransposed;
	}
	
	private void relaxPixels(double[][] distTo, int[][] edgeTo)
	{
		// Fill the top row of distTo with the energy
		for (int x = 0; x < width; x++)
			distTo[0][x] = energy[0][x];
		
		/** 
		 *  Go through each row, and relax the path for each pixel
		 *  from the path above it.
		 */
		for (int y = 1; y < height - 1; y++)
			for (int x = 0; x < width; x++)
			{
				if (x > 0) 
				{
					distTo[y][x] = distTo[y - 1][x - 1] + energy[y][x];
					edgeTo[y][x] = x - 1;
				}
				if (x == 0 || distTo[y - 1][x] + energy[y][x] < distTo[y][x])
				{
					distTo[y][x] = distTo[y - 1][x] + energy[y][x];
					edgeTo[y][x] = x;
				}
				if (x < width - 1 && distTo[y - 1][x + 1] + energy[y][x] < distTo[y][x])
				{
					distTo[y][x] = distTo[y - 1][x + 1] + energy[y][x];
					edgeTo[y][x] = x + 1;
				}
			}
	}
	
	private int[] constructSeam(double[][] distTo, int[][] edgeTo)
	{
		int[] seam = new int[height];
		
		if (height <= 2)
		{
			seam[0] = width - 1;
			if (height == 2)
				seam[1] = width - 1;
		}
		else
		{
			double bottomMin = distTo[height - 2][0];
			for (int x = 0; x < width; x++)
				if (distTo[height - 2][x] < bottomMin)
				{
					bottomMin = distTo[height - 2][x];
					seam[height - 1] = x;
				}
			seam[height - 2] = seam[height - 1];
			
			for (int y = height - 3; y > 0; y--)
			{
				seam[y] = edgeTo[y + 1][seam[y + 1]];
			}
	
			seam[0] = seam[1];
		}
		return seam;
	}
	
	public int[] findVerticalSeam()
	{	
		if (isTransposed) transposePicAndEnergy();
		return findSeam();
	}
	
	private int[] findSeam()
	{
		double[][] distTo = new double[height][width];
		int[][]    edgeTo = new int[height][width];
		
		relaxPixels(distTo, edgeTo);		
		
		return constructSeam(distTo, edgeTo);
	}
	
	public void removeHorizontalSeam(int[] seam)
	{
		checkValidSeam(seam);
		if (height <= 1) throw new IllegalArgumentException();
		if (!isTransposed) transposePicAndEnergy();
		removeSeam(seam, width, height);
		width--;
	}
	
	private void checkValidSeam(int[] seam)
	{
		if (seam.length != height) throw new IllegalArgumentException();
		for (int y = 0; y < height; y++)
		{
			if (seam[y] < 0 || seam[y] > width) throw new IllegalArgumentException();
			if (y > 0 && Math.abs(seam[y] - seam[y - 1]) > 1) throw new IllegalArgumentException();
		}
		
	}
	
	public void removeVerticalSeam(int[] seam)
	{
		checkValidSeam(seam);
		if (width <= 1) throw new IllegalArgumentException();
		if (isTransposed) transposePicAndEnergy();
		removeSeam(seam, width, height);
		width--;
	}
	
	private void removeSeam(int[] seam, int w, int h)
	{	
		for (int y = 0; y < h; y++)
		{
			int seamX = seam[y];
			shift[y][seamX] = true;
			System.arraycopy(picColors[y], seamX + 1, picColors[y], seamX, w - seamX - 1);
			System.arraycopy(energy[y], seamX + 1, energy[y], seamX, w - seamX - 1);
		}
		
		recalculateEnergy();
		
		for (int y = 0; y < h; y++)
			shift[y][seam[y]] = false;
	}
	
	private void recalculateEnergy()
	{
		for (int y = 0; y < height - 1; y++)
			for (int x = 0; x < width - 1; x++)
				if ((x > 0 && shift[y][x - 1]) 
					|| shift[y][x] 
					|| (x < width - 1 && shift[y][x + 1]))
					energy[y][x] = energy(x, y);
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}
