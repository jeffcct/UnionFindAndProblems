import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WeightedQuickUnionUFWithCompression {
    private int[] parents;
    private int[] length;
    private int n;

    public WeightedQuickUnionUFWithCompression(int n) {
        this.n = n;
        this.parents = new int[n];
        this.length = new int[n];
        for (int i = 0; i < n; i++) {
            this.parents[i] = i;
        }
        for (int i = 0; i < n; i++) {
            this.length[i] = 1;
        }
    }

    private int getFinalParent(int parent) throws Exception {
        if (parent < 0 || parent >= n) {
            throw new IllegalArgumentException("getFinalParent must read something between 1 and n.");
        }
        // keeps track of children seen. This is to compress long pathways.
        ArrayList<Integer> visitedChildren = new ArrayList<Integer>();
        // find the root parent
        while (this.parents[parent] != parent) {
            visitedChildren.add(parent);
            parent = this.parents[parent];
        }
        // compress paths of visited parents
        for (int child : visitedChildren) {
            this.parents[child] = parent;
        }
        return parent;
    }

   public int find(int node) throws Exception {
    return this.getFinalParent(node);
   }

    private boolean isConnected(int node1, int node2) throws Exception {
        int parent1 = this.getFinalParent(node1);
        int parent2 = this.getFinalParent(node2);
        return parent1 == parent2;
    }

    public void union(int node1, int node2) throws Exception {
        int parent1 = this.getFinalParent(node1);
        int parent2  =this.getFinalParent(node2);
        if (parent1 != parent2) {
            if (this.length[parent1] >= this.length[parent2]) {
                this.parents[parent2] = parent1;
                this.length[parent2] += this.length[parent1];
            } else {
                this.parents[parent1] = parent2;
                this.length[parent1] += this.length[parent2];
            }
        }
    }

    public HashMap<Integer, ArrayList<Integer>> connectedComponents() throws Exception {
        HashMap<Integer, ArrayList<Integer>> components = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < this.parents.length; i++) {
            int parent = this.getFinalParent(i);
            if (components.containsKey(parent)) {
                components.get(parent).add(i);
            } else {
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(i);
                components.put(parent, list);
            }
        }
        return components;

    }

    public String toString() {
        try {
            HashMap<Integer, ArrayList<Integer>> components = this.connectedComponents();
            return String.format("Parent Addresses: %s%nComponents: %s", Arrays.toString(this.parents), this.connectedComponents().toString());
        } catch (Exception e) {
            return String.format("Parent Addresses: %s", Arrays.toString(this.parents));
        }
    }
}
