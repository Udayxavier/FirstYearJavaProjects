public class Line {
    // Coordinates of the starting point of the line
    private int[] start;
    // Coordinates of the ending point of the line
    private int[] end;

    /**
     * Constructor to create a Line object.
     * @param row The starting row coordinate.
     * @param col The starting column coordinate.
     * @param horizontal Determines if the line is horizontal. 
     *                   If false, the line is considered vertical.
     * @param length The length of the line.
     */
    public Line(int row, int col, boolean horizontal, int length) {
        // Initializing the start coordinates.
        this.start = new int[]{row, col};

        // Depending on the orientation, calculate the end coordinates.
        if (horizontal) {
            // For horizontal lines, the row remains the same, and the column changes.
            this.end = new int[]{row, col + length - 1};
        } else {
            // For vertical lines, the column remains the same, and the row changes.
            this.end = new int[]{row + length - 1, col};
        }
    }

    /**
     * Gets the start coordinates of the line.
     * @return A new array containing the start coordinates to avoid direct modification.
     */
    public int[] getStart() {
        return new int[]{this.start[0], this.start[1]};
    }

    /**
     * Calculates the length of the line.
     * @return The length of the line.
     */
    public int length() {
        // The length is computed based on the difference in coordinates,
        // considering both horizontal and vertical distances.
        return Math.abs(this.start[0] - this.end[0]) + Math.abs(this.start[1] - this.end[1]) + 1;
    }

    /**
     * Checks if the line is horizontal.
     * @return True if the line is horizontal, false otherwise.
     */
    public boolean isHorizontal() {
        // A line is horizontal if the row coordinates are the same.
        return this.start[0] == this.end[0];
    }

    /**
     * Determines if a given point is on the line.
     * @param row The row coordinate of the point.
     * @param col The column coordinate of the point.
     * @return True if the point is on the line, false otherwise.
     */
    public boolean inLine(int row, int col) {
        if (isHorizontal()) {
            // For horizontal lines, check if the point's row matches the line's row
            // and its column is between the start and end columns.
            return row == this.start[0] && col >= this.start[1] && col <= this.end[1];
        } else {
            // For vertical lines, check if the point's column matches the line's column
            // and its row is between the start and end rows.
            return col == this.start[1] && row >= this.start[0] && row <= this.end[0];
        }
    }

    /**
     * Converts the line's coordinates to a string representation.
     * @return A string representing the line's start and end coordinates.
     */
    public String toString() {
        return "Line:[" + start[0] + "," + start[1] + "]->[" + end[0] + "," + end[1] + "]";
    }

}
