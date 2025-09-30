package edu.hsutx;

/**
 * @author Todd Dole
 * @version 1.0
 * Starting Code for the CSCI-3323 Red-Black Tree assignment
 * Students must complete the TODOs and get the tests to pass
 */

/**
 * A Red-Black Tree that takes int key and String value for each node.
 * Follows the properties of a Red-Black Tree:
 * 1. Every node is either red or black.
 * 2. The root is always black.
 * 3. Every leaf (NIL node) is black.
 * 4. If a node is red, then both its children are black.
 * 5. For each node, all simple paths from the node to descendant leaves have the same number of black nodes.
 */
public class RedBlackTree<E> {
    Node root;
    int size;

    final boolean RED = true;
    final boolean BLACK = false;

    protected class Node {
        public String key;
        public E value;
        public Node left;
        public Node right;
        public Node parent;
        public boolean color; // true = red, false = black

        public Node(String key, E value, Node parent, boolean color) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = null;
            this.right = null;
            this.color = color;
        }

        // TODO - add comments as appropriate including a javadoc for each method
        public int getDepth() {
            int depth = 0;
            Node current = this;
            while (current != null) {
                depth++;
                current = current.parent;
            }
            // TODO - calculate the depth of the node and return an int value.
            // Hint: follow parent pointers up to the root and count steps
            return depth;
        }

        public int getBlackDepth() {
            int depth = 0;
            Node current = this;
            while (current != null) {
                if (current.color == BLACK) {
                    depth++;
                }
                current = current.parent;
            }
            // TODO - calculate the depth of the node counting only black nodes and return an int value
            return depth;
        }
    }

    public RedBlackTree() {
        root = null; // Start with an empty tree.  This is the one time we can have a null ptr instead of a null key node
        size = 0;
    }

    public void insert(String key, E value) {
        // TODO - Insert a new node into the tree with key and value
        // You must handle rebalancing the tree after inserting
        // 1. Insert the node as you would in a regular BST.
        // 2. Recolor and rotate to restore Red-Black Tree properties.
        // Make sure to add 1 to size if node is successfully added
        Node z = new Node(key, value, null, false);
        Node y = null;
        Node x = root;

        while (x != null) {
            y = x;
            if (key.compareTo(x.key) < 0) {
                x = x.left;
            }else if (key.compareTo(x.key) > 0) {
                x = x.right;
            }
        }

        z.parent = y;
        if(y == null) {
            root = z;
        } else if(key.compareTo(y.key) < 0) {
            y.left = z;
        }else if(key.compareTo(y.key) > 0) {
            y.right = z;
        }

        z.left = null;
        z.right = null;
        z.color = RED;
        size++;

        fixInsertion(z);
    }

    public void delete(String key) {
        // TODO - Implement deletion for a Red-Black Tree
        // Will need to handle three cases similar to the Binary Search Tree
        // 1. Node to be deleted has no children
        // 2. Node to be deleted has one child
        // 3. Node to be deleted has two children
        // Additionally, you must handle rebalancing after deletion to restore Red-Black Tree properties
        // make sure to subtract one from size if node is successfully added
        Node z = find(key);
        if (z == null) {
            return;
        }
        Node y = z;
        boolean color = y.color;
        Node x;

        if(z.left == null) {
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == null) {
            x = z.left;
            transplant(z, z.left);
        } else{
            y = treeMinimum(z.right);
            color = y.color;
            x = y.right;
            if(y.parent == z){
                if(x != null) x.parent = y;
            }else{
                transplant(y, y.right);
                y.right = z.right;
                if(y.right != null) y.right.parent = y;
            }
            transplant(z,y);
            y.left = z.left;
            if(y.left != null) y.left.parent = y;
            y.color = z.color;
        }
        size--;
        fixDeletion(z);

    }

    private void fixInsertion(Node z) {
        // TODO - Implement the fix-up procedure after insertion
        // Ensure that Red-Black Tree properties are maintained (recoloring and rotations).
        // Hint: You will need to deal with red-red parent-child conflicts
        while(z.parent !=null && isRed(z.parent)){
            if(z.parent == z.parent.parent.left){
                Node y = z.parent.right;
                if(isRed(y)){
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z=z.parent.parent;
                }else{
                    if(z == z.parent.right){
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            }else{
                Node y = z.parent.parent.left;
                if(isRed(y)){
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z=z.parent.parent;
                }else{
                    if(z == z.parent.left){
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    private void fixDeletion(Node x) {
        // TODO - Implement the fix-up procedure after deletion
        // Ensure that Red-Black Tree properties are maintained (recoloring and rotations).
        while(x != root && isBlack(x)){
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(isRed(w)){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w=x.parent.right;
                }
                if(isBlack(w.left) && isBlack(w.right)){
                    w.color = RED;
                    x=x.parent;
                }else{
                    if(isBlack(w.right)){
                        if(w.left != null) w.left.color = BLACK;
                        w.color = RED;
                        rotateRight(w);
                        w=x.parent.right;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    if(w.right != null) w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(isRed(w)){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w=x.parent.left;
                }
                if(isBlack(w.left) && isBlack(w.left)){
                    w.color = RED;
                    x=x.parent;
                }else{
                    if(isBlack(w.left)){
                        if(w.right != null) w.right.color = BLACK;
                        w.color = RED;
                        rotateLeft(w);
                        w=x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    if(w.left!=null) w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        if(x!= null) x.color = BLACK;
    }

    private void rotateLeft(Node x) {
        // TODO - Implement left rotation
        // Left rotation is used to restore balance after insertion or deletion
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if(x.parent == null){
            root = y;
        }else if(x == x.parent.left){
            x.parent.left = y;
        }else{
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node x) {
        // TODO - Implement right rotation
        // Right rotation is used to restore balance after insertion or deletion
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if(x.parent == null){
            root = y;
        }else if(x == x.parent.right){
            x.parent.right = y;
        }else{
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    Node find(String key) {
        // TODO - Search for the node with the given key
        // If the key exists in the tree, return the Node where it is located
        // Otherwise, return null
        Node c = root;
        while (c != null) {
            int cKey = key.compareTo(c.key);
            if (cKey == 0) {
                return c;
            } else if (cKey < 0) {
                c = c.left;
            } else if (cKey > 0) {
                c = c.right;
            }
        }
        return null;
    }

    public E getValue(String key) {
        // TODO - Use find() to locate the node with the given key and return its value
        // If the key does not exist, return null
        Node node = find(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    // returns the depth of the node with key, or 0 if it doesn't exist
    public int getDepth(String key) {
        Node node = find(key);
        if (node != null) return node.getDepth();
        return 0;
    }

    // Helper methods to check the color of a node
    private boolean isRed(Node node) {
        return node != null && node.color == true; // Red is true
    }

    private boolean isBlack(Node node) {
        return node == null || node.color == false; // Black is false, and null nodes are black
    }
    public int getSize() {
        return size;
    }

    // Do not alter this method
    public boolean validateRedBlackTree() {
        // Rule 2: Root must be black
        if (root == null) {
            return true; // An empty tree is trivially a valid Red-Black Tree
        }
        if (isRed(root)) {
            return false; // Root must be black
        }

        // Start recursive check from the root
        return validateNode(root, 0, -1);
    }

    // Do not alter this method
    // Helper method to check if the current node maintains Red-Black properties
    private boolean validateNode(Node node, int blackCount, int expectedBlackCount) {
        // Rule 3: Null nodes (leaves) are black
        if (node == null) {
            if (expectedBlackCount == -1) {
                expectedBlackCount = blackCount; // Set the black count for the first path
            }
            return blackCount == expectedBlackCount; // Ensure every path has the same black count
        }

        // Rule 1: Node is either red or black (implicit since we use a boolean color field)

        // Rule 4: If a node is red, its children must be black
        if (isRed(node)) {
            if (isRed(node.left) || isRed(node.right)) {
                return false; // Red node cannot have red children
            }
        } else {
            blackCount++; // Increment black node count on this path
        }

        // Recurse on left and right subtrees, ensuring they maintain the Red-Black properties
        return validateNode(node.left, blackCount, expectedBlackCount) &&
                validateNode(node.right, blackCount, expectedBlackCount);
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) v.parent = u.parent;
    }

    private Node treeMinimum(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }
}
