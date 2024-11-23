import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private int n;
    private WeightedQuickUnionUFWithCompression groups;
    private boolean[] opened;
    private boolean[] pathTop;
    private boolean[] pathBottom;
    private int numOpened;
    private boolean hasPercolated;


    public Percolation(int n) throws Exception {
        if (n <= 0) {
            throw new IllegalArgumentException("n cannot be below 0.");
        }
        this.n = n;
        this.numOpened = 0;
        this.groups = new WeightedQuickUnionUFWithCompression(n*n);
        this.opened = new boolean[n*n];
        this.pathTop = new boolean[n*n];
        this.pathBottom = new boolean[n*n];
        this.hasPercolated = false;
        for (int i = 0; i < n*n; i++) {
            this.opened[i] = false;
        }
        for (int i = 0; i < n; i++) {
            this.pathTop[i] = true;
        }
        for (int i = n; i < n*n; i++) {
            this.pathTop[i] = false;
        }
        for (int i = 0; i < n*(n-1); i++) {
            this.pathBottom[i] = false;
        }
        for (int i = n*(n-1); i < n*n; i++) {
            this.pathBottom[i] = true;
        }
        
    }

    private int getIndex(int row, int col) throws Exception {
        row -= 1; 
        col -= 1;
        if (row < 0 || row >= this.n || col < 0 || col >= this.n) {
            throw new IllegalArgumentException("can only open an entry within the grid [debug in getIndex private method]");
        }
        return row * this.n + col;
    }

    private int getIndex(int row, int col, String methodName) throws Exception {
        row -= 1; 
        col -= 1;
        if (row < 0 || row >= this.n || col < 0 || col >= this.n) {
            throw new IllegalArgumentException(String.format("can only open an entry within the grid [%s]", methodName));
        }
        return row * this.n + col;
    }

    private void union(int first, int second) throws Exception {
        int firstParent = this.groups.find(first);
        int secondParent = this.groups.find(second);
        this.groups.union(first, second);
        if (this.pathTop[firstParent] || this.pathTop[secondParent]) {
            int parent = this.groups.find(first);
            this.pathTop[parent] = true;
            this.pathTop[firstParent] = true;
            this.pathTop[secondParent] = true;
        }
        if (this.pathBottom[firstParent] || this.pathBottom[secondParent]) {
            int parent = this.groups.find(first);
            this.pathBottom[parent] = true;
            this.pathBottom[firstParent] = true;
            this.pathBottom[secondParent] = true;
        }
    }

    public void open(int row, int col) throws Exception {
        int index = getIndex(row, col, "debug in open public method");
        if (this.opened[index]) {
            return;
        }
        this.numOpened += 1;
        this.opened[index] = true;
        if (row > 1 && this.opened[getIndex(row - 1, col)]) {
            this.union(index, getIndex(row - 1, col));
        } 
        if (col > 1 && this.opened[getIndex(row, col - 1)]) {
            this.union(index, getIndex(row, col - 1));
        }
        if (row < this.n && this.opened[getIndex(row + 1, col)]) {
            this.union(index, getIndex(row + 1, col));
        } 
        if (col < this.n && this.opened[getIndex(row, col + 1)]) {
            this.union(index, getIndex(row, col + 1));
        }
        int parent = this.groups.find(index);
        if (this.pathBottom[parent] && this.pathTop[parent]) {
            this.hasPercolated = true;
        }

    }
    
    public boolean isOpen(int row, int col) throws Exception {
        return this.opened[getIndex(row, col, "debug in isOpen public method")];

    }
    public boolean isFull(int row, int col) throws Exception {
        if (!isOpen(row, col)) {
            return false;
        }
        int parent = this.groups.find(getIndex(row, col));
        return this.pathTop[parent];
    }

    public int numberOfOpenSites() {
        return this.numOpened;
    }

    public boolean percolates() {
        return this.hasPercolated;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            return;
        }
        int n = Integer.parseInt(args[0]);
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            int row = StdRandom.uniformInt(n) + 1;
            int col = StdRandom.uniformInt(n) + 1;
            percolation.open(row, col);
        }
        int numSites = percolation.numberOfOpenSites();
        StdOut.println(String.format("Percolated after opening %d sites. This opened %.2f%% of all sites.", numSites, 100.0 * numSites / (n*n)));
    }
}
