import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] requiredOpenings;
    private int trials;
    private double val = 1.96;

    public PercolationStats(int n, int trials) throws Exception {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("must have at least 1 trial and n in percolation stats.");
        }

        this.requiredOpenings = new double[trials];
        this.trials = trials;
        for (int i = 0; i < trials; i++) {
            int numSites = runPercolationInstance(n);
            this.requiredOpenings[i] = ((double) numSites) / (n*n);
        }
    }

    public double mean() {
        double averageRequiredOpenings = StdStats.mean(this.requiredOpenings);
        return averageRequiredOpenings;

    }

    public double stddev() {
        double standardDeviation = StdStats.stddev(this.requiredOpenings);
        return standardDeviation;
    }

    public double confidenceLo() {
        return this.mean() - val * this.stddev() / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        return this.mean() + val * this.stddev() / Math.sqrt(this.trials);

    }
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            return;
        }

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        
        StdOut.printf("%-23s = %f%n", "mean", ps.mean());
        StdOut.printf("%-23s = %f%n", "stddev", ps.stddev());
        StdOut.printf("%-23s = [%f, %f]", "95% confidence interval", ps.confidenceLo(), ps.confidenceHi());
    }

    private static int runPercolationInstance(int n) throws Exception {
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniformInt(n) + 1;
            int col = StdRandom.uniformInt(n) + 1;
            percolation.open(row, col);
        }
        int numSites = percolation.numberOfOpenSites();
        return numSites;
    }
}
