package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	private double[] threshold;
	private double alpha = 1.96;
	private int T;
	private double sites;
	
	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T)
	{
	    if (N <= 0) throw new IllegalArgumentException("Size of Percolation grid isn't > 0");
	    if (T < 1) throw new IllegalArgumentException("Number of experiments, T isn't >= 1");
	    
	    this.T = T;
	    this.threshold = new double[T];
	    this.sites = N * N;
	    
	    for (int trial = 0; trial < T; trial++)
	    {
	        if (N == 1) threshold[trial] = 1;
	        else
	        {
	            Percolation perc = new Percolation(N);
	            double sitesOpened = 0;
	            
	            while (!perc.percolates())
	            {
	                int i = StdRandom.uniform(1, N + 1);
	                int j = StdRandom.uniform(1, N + 1);
	                
	                if (!perc.isOpen(i, j))
	                {
	                    perc.open(i, j);
	                    sitesOpened++;
	                }
	            }
	            threshold[trial] = sitesOpened / sites;
	        }
	    }
	}
	
	// sample mean of percolation threshold
	public double mean()
	{
		return StdStats.mean(this.threshold);
	}
	
	// sample standard deviation of percolation threshold
	public double stddev()
	{
	    if (T == 1) return Double.NaN;
		return StdStats.stddev(this.threshold);
	}
	
	// low end point of 95% confidence interval
	public double confidenceLo()
	{
		return mean() - alpha * stddev() / Math.sqrt(T);
	}
	
	// high end point of 95% confidence interval
	public double confidenceHi()
	{
		return mean() + alpha * stddev() / Math.sqrt(T);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    PercolationStats ps = new PercolationStats(10, 10);
	    System.out.println(ps.mean());
	    System.out.println(ps.stddev());

	}

}
