class BTreeNode {
    int[] keys;  // Array of keys
    int t;       // Minimum degree (defines the range for number of keys)
    BTreeNode[] children; // Array of child pointers
    int n;       // Current number of keys
    boolean isLeaf; // True if the node is a leaf node

    public BTreeNode(int t, boolean isLeaf) {
        this.t = t;
        this.isLeaf = isLeaf;
        this.keys = new int[2 * t - 1]; // Max keys a node can have is 2*t - 1
        this.children = new BTreeNode[2 * t]; // Max children a node can have is 2*t
        this.n = 0; // Initial number of keys is 0
    }
    
    // Method to traverse nodes
    public void traverse() {
        int i;
        for (i = 0; i < n; i++) {
            if (!isLeaf) {
                children[i].traverse();
            }
            System.out.print(keys[i] + " ");
        }
        if (!isLeaf) {
            children[i].traverse();
        }
    }

    // Method to search a key in the subtree rooted with this node
    public BTreeNode search(int key) {
        int i = 0;
        while (i < n && key > keys[i]) {
            i++;
        }
        if (i < n && keys[i] == key) {
            return this;
        }
        if (isLeaf) {
            return null;
        }
        return children[i].search(key);
    }

    // Method to insert a key into a non-full node
    public void insertNonFull(int key) {
        int i = n - 1;
        if (isLeaf) {
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];
                i--;
            }
            keys[i + 1] = key;
            n++;
        } else {
            while (i >= 0 && keys[i] > key) {
                i--;
            }
            i++;
            if (children[i].n == 2 * t - 1) {
                splitChild(i, children[i]);
                if (keys[i] < key) {
                    i++;
                }
            }
            children[i].insertNonFull(key);
        }
    }

    // Method to split the child y of this node
    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.t, y.isLeaf);
        z.n = t - 1;
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }
        if (!y.isLeaf) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
            }
        }
        y.n = t - 1;
        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;
        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = y.keys[t - 1];
        n++;
    }
}

public class BTree {
    BTreeNode root;
    int t; // Minimum degree

    public BTree(int t) {
        this.root = null;
        this.t = t;
    }

    // Method to traverse the tree
    public void traverse() {
        if (root != null) {
            root.traverse();
        }
    }

    // Method to search a key in the B-tree
    public BTreeNode search(int key) {
        return (root == null) ? null : root.search(key);
    }

    // Insert method
    public void insert(int key) {
        if (root == null) {
            // Create a new root if the tree is empty
            root = new BTreeNode(t, true);
            root.keys[0] = key;
            root.n = 1;
        } else {
            // If the root is full, split it and grow the tree height
            if (root.n == 2 * t - 1) {
                BTreeNode s = new BTreeNode(t, false);
                s.children[0] = root;
                s.splitChild(0, root);
                int i = 0;
                if (s.keys[0] < key) {
                    i++;
                }
                s.children[i].insertNonFull(key);
                root = s;
            } else {
                root.insertNonFull(key);
            }
        }
    }
  
  public static void main(String args[]) {
     BTree btree = new BTree(3); // Minimum degree of 3

        int[] keys = {10, 20, 5, 6, 12, 30, 7, 17};
        for (int key : keys) {
            btree.insert(key);
        }

        System.out.println("Traversal of the constructed B-tree:");
        btree.traverse();
       
        btree.insert(11);
        System.out.println("Inserting: " + 11);
        System.out.println("Traversal of the constructed B-tree:");
        btree.traverse();
        
        btree.insert(32);
        System.out.println("Inserting: " + 32);
        System.out.println("Traversal of the constructed B-tree:");
        btree.traverse();
  }
}