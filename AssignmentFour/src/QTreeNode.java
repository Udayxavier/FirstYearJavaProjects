/**
 * class that in a quadtree structure represents a node. In a quadtree, every node 
 * has exactly four children; employed to divide a two-dimensional
 * area into four quadrants or areas by recursively subdividing it.
 */

public class QTreeNode {
    private int x, y, size, color;
    private QTreeNode parent;
    private QTreeNode[] children;

    /**
     * Default constructor initializes a node at the origin (0,0) with no size or color.
     * It's a leaf node by default, with null children.
     */
    public QTreeNode() {
        this.x = 0;
        this.y = 0;
        this.size = 0;
        this.color = 0;
        this.parent = null;
        this.children = new QTreeNode[4];
    }
    /**
     * Constructs a node with specified children, location, size, and color.
     * @param theChildren Array of child nodes, null if it is a leaf node.
     * @param xcoord X-coordinate node.
     * @param ycoord Y-coordinate node.
     * @param theSize Size of the region covered by the node.
     * @param theColor Color/value of node.
     */
    public QTreeNode(QTreeNode[] theChildren, int xcoord, int ycoord, int theSize, int theColor) {
        this.x = xcoord;
        this.y = ycoord;
        this.size = theSize;
        this.color = theColor;
        this.parent = null;
        // Ensure children array is always initialized, even for leaf nodes.
        if (theChildren == null) {
            this.children = new QTreeNode[4]; // All elements null by default
        } else {
            this.children = theChildren;
        }
    }
    /**
     * Checks if a point is contained within the node's region.
     * @param xcoord X-coordinate of point.
     * @param ycoord Y-coordinate of point.
     * @return True if the point is within the node's region, otherwise false.
     */
    public boolean contains(int xcoord, int ycoord) {
        return xcoord >= x && xcoord < x + size && ycoord >= y && ycoord < y + size;
    }
    
    // getters for the node properties // returns their properties as what getters do
    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public int getColor() {
        return color;
    }

    public QTreeNode getParent() {
        return parent;
    }

    public QTreeNode getChild(int index) throws QTreeException {
        if (index < 0 || index > 3) {
            throw new QTreeException("Index out of bounds for children array");
        }
        return children[index];
    }
    // setters for the node properties
    public void setx(int newx) {
        this.x = newx;
    }

    public void sety(int newy) {
        this.y = newy;
    }

    public void setSize(int newSize) {
        this.size = newSize;
    }

    public void setColor(int newColor) {
        this.color = newColor;
    }

    public void setParent(QTreeNode newParent) {
        this.parent = newParent;
    }

    public void setChild(QTreeNode newChild, int index) throws QTreeException {
    	// exception thrown for out of bounds for children array
        if (index < 0 || index > 3) {
            throw new QTreeException("Index out of bounds for children array");
        }
        this.children[index] = newChild;
    }
    /**
     * Checks if node a leaf, i.e., it has no children.
     * @return True if the node is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        for (int i = 0; i < children.length; i++) {
            if (children[i] != null) {
                return false;
            }
        }
        return true;
    }

}