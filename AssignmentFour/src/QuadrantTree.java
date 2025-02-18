public class QuadrantTree {
    private QTreeNode root;

    // Constructor: 
    public QuadrantTree(int[][] thePixels) {
    	// calling on buildtree
        root = buildTree(thePixels, 0, 0, thePixels.length);
    }

    /**
     * Builds a quadtree from a 2D array of pixel colors. Each node represents a quadrant, with leaf nodes
     * corresponding to individual pixels. The tree is built recursively, dividing the image into smaller
     * quadrants until reaching single pixels.
     * 
     * @param pixels Color values of pixels in a 2D array.
     * @param x X-coordinate; quadrant's upper left corner in the array.
     * @param y Y-coordinate; quadrant's upper left corner in the array.
     * @param size quadrant size, assumes a square shape.
     * @return The root node of the quadtree, with each node's color either being the average of its children
     *         or the actual pixel color for leaves.
     */
    private QTreeNode buildTree(int[][] pixels, int x, int y, int size) {
        if (size == 1) {
            // Base case; matrix size:1, create and return a leaf node
            return new QTreeNode(null, x, y, size, pixels[y][x]);
        } else {
            // Recursive case; matrix size larger than 1
            int newSize = size / 2;
            QTreeNode[] children = new QTreeNode[4];

            // UpperLeft quadrant
            children[0] = buildTree(pixels, x, y, newSize);

            // UpperRight quadrant
            children[1] = buildTree(pixels, x + newSize, y, newSize);

            // BottomLeft quadrant
            children[2] = buildTree(pixels, x, y + newSize, newSize);

            // BottomRight quadrant
            children[3] = buildTree(pixels, x + newSize, y + newSize, newSize);

            // average color for this node
            int color = calculateAverageColor(children);

            // a new QTreeNode that represents the entire current quadrant
            QTreeNode node = new QTreeNode(children, x, y, size, color);

            // Set the parent of the children to this node
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    children[i].setParent(node);
                }
            }
            return node;
        }
    }

    // Utility method to calculate the average color of four children nodes
    private int calculateAverageColor(QTreeNode[] children) {
        int sumColor = 0;
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                sumColor += children[i].getColor();
            }
        }
        return sumColor / children.length;
    }

    // Getter for the root
    public QTreeNode getRoot() {
        return root;
    }
    /**
	 * retrieves every pixel (or QTreeNode) in the quadtree at a given level. The node is returned 
	 * in a list if the level is reached or if it is a leaf. It collects nodes from all valid children 
	 * recursively for non-leaf nodes at deeper levels.
     *
     * @param r The current QTreeNode being examined.
     * @param theLevel The desired level in the quadtree to retrieve nodes from. The root node is at level 0.
     * @return A ListNode containing QTreeNodes from the specified level. If the input node is null, the level
     *         is invalid, or no nodes found at the desired level, returns null.
     */

    public ListNode<QTreeNode> getPixels(QTreeNode r, int theLevel) {
        if (r == null || theLevel < 0) {
            return null;
        }
        // If the node is a leaf or if we've reached the desired level, return a list with just this node.
        if (r.isLeaf() || theLevel == 0) {
            return new ListNode<>(r);
        } else {
            // Initialize placeholders for the start and end of the list
            ListNode<QTreeNode> head = null, currentTail = null;

            for (int i = 0; i < 4; i++) {
                try {
                    QTreeNode child = r.getChild(i);
                    if (child != null) {
                        ListNode<QTreeNode> childList = getPixels(child, theLevel - 1);
                        if (childList != null) {
                            if (head == null) {
                                head = childList;
                                currentTail = head;
                            } else {
                                currentTail.setNext(childList);
                            }
                            // Update currentTail to point to the last node of the childList
                            while (currentTail.getNext() != null) {
                                currentTail = currentTail.getNext();
                            }
                        }
                    }
                } catch (QTreeException e) {
                   
                }
            }
            return head;
        }
    }

    public Duple findMatching(QTreeNode r, int theColor, int theLevel) {
        // Base case: if the node is null or theLevel is less than 0
        if (r == null || theLevel < 0) {
            return new Duple(); // Return an empty Duple object.
        }

        // If we are at the correct level or the node is a leaf, check the color.
        if (theLevel == 0 || r.isLeaf()) {
            if (Gui.similarColor(r.getColor(), theColor)) {
                // Color matches, return Duple with this node and count of 1.
                return new Duple(new ListNode<>(r), 1);
            } else {
                // Color does not match, return an empty Duple with count of 0.
                return new Duple();
            }
        }

        // Recursive case: to go deeper into the tree.
        Duple result = new Duple();
        for (int i = 0; i < 4; i++) {
            try {
                QTreeNode child = r.getChild(i);
                if (child != null) {
                    Duple childDuple = findMatching(child, theColor, theLevel - 1);
                    result = concatenateDuples(result, childDuple);
                }
            } catch (QTreeException e) {
                // I m leaving this block intentionally blank because
                // If an exception is thrown, it means the child does not exist,
                // which we are already checking with the null condition so that's why.
            }
        }
        return result;
    }

    /**
     * Helper method for findMatching; Concatenates two Duple objects by linking the end of the first Duple's 
     * list to the beginning of the second Duple's list, and updates the count of the first Duple to reflect 
     * the total number of elements in the concatenated list. If the first Duple is empty, it returns the 
     * second Duple, and vice versa.
     *
     * @param d1 The first Duple to concatenate.
     * @param d2 The second Duple to concatenate.
     * @return The concatenated Duple, with updated count and linked lists.
     */
    private Duple concatenateDuples(Duple d1, Duple d2) {
        if (d1.getFront() == null) return d2;
        if (d2.getFront() == null) return d1;

        ListNode<QTreeNode> tail = d1.getFront();
        while (tail.getNext() != null) {
            tail = tail.getNext();
        }
        tail.setNext(d2.getFront());
        d1.setCount(d1.getCount() + d2.getCount());
        return d1;
    }
    /**
     * Finds and returns a QTreeNode at a specified level containing the (x, y) coordinates, performs a
     * recursive search through the quadtree, focusing on quadrants that encompass the coordinates, until
     * the target level is reached or a containing node is found earlier.
     * @param r Starting or root node for the search.
     * @param theLevel Target depth level in the quadtree.
     * @param x X-coordinate of the point to find.
     * @param y Y-coordinate of the point to find.
     * @return The found QTreeNode at specified level, or null if not found.
     */
    public QTreeNode findNode(QTreeNode r, int theLevel, int x, int y) {
        // Base case: if the node is null or we've reached the desired level
        if (r == null || theLevel < 0) {
            return null;
        }
        if (theLevel == 0) {
            // If we're at the correct level, checking if this node contains the point
            if (r.contains(x, y)) {
                return r;
            } else {
                return null;
            }
        }
        // Recursive case: not at the correct level yet
        for (int i = 0; i < 4; i++) {
            try {
                QTreeNode child = r.getChild(i);
                // Calculate the size of the quadrants we're searching
                int childSize = r.getSize() / 2;
                int childX = (i % 2) == 0 ? r.getx() : r.getx() + childSize;
                int childY = (i < 2) ? r.gety() : r.gety() + childSize;

                // If the child node contains the point, make a recursive call
                // to search within that child node
                if (child != null && child.contains(x, y)) {
                    return findNode(child, theLevel - 1, x, y);
                }
            } catch (QTreeException e) {
                // If there's an exception because the child index is out of bounds,
                // continue to the next child
                continue;
            }
        }
        // If none of the children contain the point, returning null
        return null;
    }
}



