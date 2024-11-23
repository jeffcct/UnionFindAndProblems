import java.util.Scanner;

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
        System.out.printf("%d was unioned with %d.%n", val1, val2);
        int parent1 = components.find(val1);
        int parent2 = components.find(val2);
        int newMaximum = Math.max(this.maximums[parent1], this.maximums[parent2]);
        this.maximums[parent1] = newMaximum;
        this.maximums[parent2] = newMaximum;
        this.components.union(parent1, parent2);
        System.out.printf("New maximum is %d.%n", this.maximums[components.find(val1)]);
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
            this.union(val, val + 1);
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
            return this.maximums[parent] + 1;
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        int n = Integer.parseInt(args[0]);
        SuccessorProblem sp = new SuccessorProblem(n);
        System.out.printf("Welcome to Successor Problem with n = %d!%n", n);
        System.out.println("Valid arguments are 'remove' that will remove a given number, 'succ' that finds the smallest number still in the set that's at least as big and 'fin' to terminate the program.");
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.printf("Please enter an argument: ");
            String input = in.nextLine();
            String[] tokens = input.split(" ");
            if (tokens[0] == "fin") {
                return;
            }
            if (tokens.length > 2) {
                System.out.println("Please enter 'fin' or two words, first either 'remove' or 'succ' then a number");
                continue;
            }
            if (!isNumeric(tokens[1])) {
                System.out.println("Please enter a number for the second argument.");
                continue;
            }
            int secondArgument = Integer.parseInt(tokens[1]);
            switch (tokens[0]) {
                case "remove":
                    sp.remove(secondArgument);
                    System.out.printf("%d was removed.%n", secondArgument);
                    break;
                case "succ":
                    int next = sp.successor(secondArgument);
                    System.out.printf("The next number after %d is %d.%n", secondArgument, next);
                    break;
                default:
                    System.out.println("For the first argument, please enter one of 'remove', 'succ' or 'fin' and a number for the second argument.");

            }
        }
    }
}
