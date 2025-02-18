public class LetterCrush {
	// 2D character array representing the game grid.
    private char[][] grid;
    
    // Constant representing an empty cell in the grid.
    public static final char EMPTY = ' ';
    
    /**
     * Constructor for creating a new LetterCrush game.
     * @param width Width of the game grid.
     * @param height Height of the game grid.
     * @param initial A string to initialize the grid with. Extra characters are filled with EMPTY.
     */
    public LetterCrush(int width, int height, String initial) {
        grid = new char[height][width];
        int stringIndex = 0;
        
     // Populate the grid with characters from the initial string or EMPTY.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (stringIndex < initial.length()) {
                    grid[i][j] = initial.charAt(stringIndex++);
                } else {
                    grid[i][j] = EMPTY;
                }
            }
        }
    }
    
    /**
     * Converts the current state of the grid to a string representation.
     * @return String representing the grid.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("LetterCrush\n");
        for (int i = 0; i < grid.length; i++) {
            sb.append('|');
            for (int j = 0; j < grid[i].length; j++) {
                sb.append(grid[i][j]);
            }
            sb.append('|').append(i).append('\n');
        }
        sb.append('+');
        for (int i = 0; i < grid[0].length; i++) {
            sb.append(i % 10); // Use mod 10 to keep single digits
        }
        sb.append('+');
        return sb.toString();
    }
    
    /**
     * Checks if the grid is stable (no empty spaces below filled ones in any column).
     * @return True if the grid is stable, false otherwise.
     */
    public boolean isStable() {
        for (int col = 0; col < grid[0].length; col++) {
            boolean emptyEncountered = false;
            for (int row = grid.length - 1; row >= 0; row--) {
                if (grid[row][col] == EMPTY) {
                    emptyEncountered = true;
                } else if (emptyEncountered) {
                    // Found a non-EMPTY cell above an EMPTY cell
                    return false;
                }
            }
        }
        return true; // No unstable conditions were found
    }
    
    /**
     * Applies gravity to the grid, making all characters fall to the bottom 
     * if there is an EMPTY space below.
     */
    public void applyGravity() {
        for (int col = 0; col < grid[0].length; col++) {
            for (int row = grid.length - 1; row > 0; row--) {
                if (grid[row][col] == EMPTY) {
                    // Move tiles down by swapping the EMPTY tile with the one above it
                    grid[row][col] = grid[row - 1][col];
                    grid[row - 1][col] = EMPTY;
                }
            }
        }
    }
    
    /**
     * Removes a line from the grid and replaces it with EMPTY spaces.
     * @param theLine The line to be removed.
     * @return True if the line is successfully removed, false otherwise.
     */
    public boolean remove(Line theLine) {
        int[] start = theLine.getStart();
        int length = theLine.length();
        boolean isHorizontal = theLine.isHorizontal();
        
        int endRow = isHorizontal ? start[0] : start[0] + length - 1;
        int endCol = isHorizontal ? start[1] + length - 1 : start[1];
        
        // Validate line within grid bounds
        if (start[0] < 0 || start[1] < 0 || endRow >= grid.length || endCol >= grid[0].length) {
            return false;
        }

        // Remove line from grid
        if (isHorizontal) {
            for (int col = start[1]; col <= endCol; col++) {
                grid[start[0]][col] = EMPTY;
            }
        } else {
            for (int row = start[0]; row <= endRow; row++) {
                grid[row][start[1]] = EMPTY;
            }
        }
        return true;
    }
    
    
    /**
     * Generates a string representation of the grid highlighting a specific line.
     * @param theLine The line to highlight.
     * @return String representation of the grid with the specified line highlighted.
     */
    public String toString(Line theLine) {
        StringBuilder sb = new StringBuilder("CrushLine\n");
        for (int i = 0; i < grid.length; i++) {
            sb.append("|");
            for (int j = 0; j < grid[i].length; j++) {
                char c = grid[i][j];
                // Check if the current position is part of the line and not empty
                if (theLine != null && theLine.inLine(i, j) && c != EMPTY) {
                    c = Character.toLowerCase(c);
                }
                sb.append(c);
            }
            // Adjusted spacing to match expected output
            sb.append("|").append(i).append('\n');
        }
        sb.append('+');
        for (int i = 0; i < grid[0].length; i++) {
            sb.append(i % 10);
        }
        sb.append('+');
        return sb.toString();
    }

    /**
     * Finds the longest contiguous line of the same character in the grid.
     * @return The longest line found in the grid, null if no line is longer than 2 characters.
     */
    public Line longestLine() {
    	// Initialization of variables to track the longest line.
        Line longestLine = null;
        int longestLength = 0;

        // Scan rows from bottom to top for horizontal lines
        for (int row = grid.length - 1; row >= 0; row--) {
            int count = 1;
            char letter = grid[row][0];
            for (int col = 1; col < grid[row].length; col++) {
                if (grid[row][col] == letter && letter != EMPTY) {
                    count++;
                } else {
                    if (count > longestLength) {
                        longestLength = count;
                        longestLine = new Line(row, col - count, true, count);
                    }
                    letter = grid[row][col];
                    count = 1;
                }
            }
            if (count > longestLength) {
                longestLength = count;
                longestLine = new Line(row, grid[row].length - count, true, count);
            }
        }

        // Scan columns from left to right for vertical lines
        for (int col = 0; col < grid[0].length; col++) {
            int count = 1;
            char letter = grid[grid.length - 1][col];
            for (int row = grid.length - 2; row >= 0; row--) {
                if (grid[row][col] == letter && letter != EMPTY) {
                    count++;
                } else {
                    if (count > longestLength) {
                        longestLength = count;
                        longestLine = new Line(row + 1, col, false, count);
                    }
                    letter = grid[row][col];
                    count = 1;
                }
            }
            if (count > longestLength) {
                longestLength = count;
                longestLine = new Line(0, col, false, count);
            }
        }
        // Return the longest line found, or null if no line is long enough.
        if (longestLength > 2) {
            return longestLine;
        } else {
            return null;
        }
    }
    
    /**
     * Cascades the removal of lines and applies gravity until the grid is stable.
     */
    public void cascade() {
        Line longestLine;
        do {
            longestLine = longestLine(); // Finds the longest line
            if (longestLine != null && longestLine.length() >= 3) {
                remove(longestLine); // Removes the longest line from the grid
                applyGravity(); // Applies gravity to the grid
            }
        } while (isStable() && longestLine != null && longestLine.length() >= 3);
    }
    
}
