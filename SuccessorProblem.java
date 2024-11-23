package Final;

public class SuccessorProblem {
    WeightedQuickUnionUFWithCompression components;
    int[] maximums;
    boolean[] removed;
    int n;
    
    public SuccessorProblem(int n) {
        this.n = n;
        this.components = new WeightedQuickUnionUFWithCompression(n);
        this.maximums = new int[n];
        this.removed = new boolean[n];
        for (int i = 0; i < n; i++) {
            this.maximums[i] = i;
        }
        for (int i = 0; i < n; i++) {
            this.removed[i] = false;
        }
    }

    public void union(int val1, int val2) throws Exception {
        int parent1 = components.find(val1);
        int parent2 = components.find(val2);
        int newMaximum = Math.max(this.maximums[parent1], this.maximums[parent2]);
        this.maximums[parent1] = newMaximum;
        this.maximums[parent2] = newMaximum;
        this.components.union(parent1, parent2);
    }

    public void remove(int val) throws Exception {
        if (val < 0 || val >= this.n) {
            throw new IllegalArgumentException("n must be below the successor threshold");
        }
        if (this.removed[val]) {
            return;
        }

        this.removed[val] = true;
        if (val > 0 && this.removed[val - 1]) {
            this.union(val, val - 1);
        }
        if (val < this.n - 1 && this.removed[val + 1]) {
            this.union(val, val - 1);
        }
    }

    public int successor(int val) throws Exception {
        if (val < 0 || val >= this.n) {
            throw new IllegalArgumentException("n must be below the successor threshold");
        }
        if (!this.removed[val]) {
            return val;
        }
        else {
            int parent = components.find(val);
            return this.maximums[parent];
        }
    }
}
