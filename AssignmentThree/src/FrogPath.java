public class FrogPath {
    private Pond pond; // The pond in which freddie navigates.
    
    /**
     * Initializes the pond with the provided txt file.
     * @param filename The name of the file containing txt .
     */
    public FrogPath(String filename) {
        try {
            this.pond = new Pond(filename);
        } catch (Exception e) {
            // Handle exceptions as needed
        	System.err.println(e.getMessage());
        }
    }

    /**
     * Finds the best Hexagon cell to move to from the current cell, taking into account various criteria like avoiding mud cells,
     * preferring certain types of cells over others, and special handling for lily pad cells and their second-degree neighbors.
     * @param currCell The current Hexagon cell.
     * @return The best next Hexagon cell to move to, or null if no suitable cell is found.
     */
    public Hexagon findBest(Hexagon currCell) {
        ArrayUniquePriorityQueue<Hexagon> priorityQueue = new ArrayUniquePriorityQueue<>();

        // Process direct neighbors for movement options
        evaluateDirectNeighbors(currCell, priorityQueue);

        // Special handling for lily pad cells to consider second-degree neighbors
        if (currCell.isLilyPadCell()) {
            evaluateLilyPadNeighbors(currCell, priorityQueue);
        }

        return priorityQueue.isEmpty() ? null : priorityQueue.peek();
    }

    private void evaluateDirectNeighbors(Hexagon cell, ArrayUniquePriorityQueue<Hexagon> queue) {
        for (int i = 0; i <= 5; i++) {
            Hexagon neighbor = cell.getNeighbour(i);
            if (isNeighborValid(neighbor)) {
                evaluateAndQueueNeighbor(neighbor, queue, 0.0); // No adjustment for direct neighbors
            }
        }
    }

    private void evaluateLilyPadNeighbors(Hexagon lilyPadCell, ArrayUniquePriorityQueue<Hexagon> queue) {
        for (int i = 0; i <= 5; i++) {
            Hexagon firstDegreeNeighbor = lilyPadCell.getNeighbour(i);
            if (firstDegreeNeighbor != null) {
                for (int j = 0; j <= 5; j++) {
                    double priorityAdjustment = (i == j) ? 0.5 : 1.0;
                    Hexagon secondDegreeNeighbor = firstDegreeNeighbor.getNeighbour(j);
                    if (isNeighborValid(secondDegreeNeighbor)) {
                        evaluateAndQueueNeighbor(secondDegreeNeighbor, queue, priorityAdjustment);
                    }
                }
            }
        }
    }

    private void evaluateAndQueueNeighbor(Hexagon neighbor, ArrayUniquePriorityQueue<Hexagon> queue, double adjustment) {
        boolean neighborHasAlligator = checkForAlligator(neighbor);
        double basePriority = calculateNeighborPriority(neighbor, neighborHasAlligator);
        if (basePriority >= 0) { // Only queue neighbors with valid priority
            queue.add(neighbor, basePriority + adjustment);
        }
    }

    private boolean isNeighborValid(Hexagon neighbor) {
        return neighbor != null && !neighbor.isMudCell() && !neighbor.isMarked();
    }

    private boolean checkForAlligator(Hexagon cell) {
        for (int i = 0; i <= 5; i++) {
            Hexagon neighbor = cell.getNeighbour(i);
            if (neighbor != null && neighbor.isAlligator()) {
                return true;
            }
        }
        return false;
    }

    private double calculateNeighborPriority(Hexagon cell, boolean hasAlligator) {
        if (hasAlligator && cell.isReedsCell()) {
            return 10.0; // Reeds cell with neighboring alligator gets highest priority
        } else if (!hasAlligator) {
            if (cell.isReedsCell()) {
                return 5.0; // Preference for reeds cells without alligators
            }
            if (cell.isWaterCell()) {
                return 6.0; // Water cells have a higher priority than reeds without alligators
            }
            if (cell.isLilyPadCell()) {
                return 4.0; // Lily pads are considered moderately favorable
            }
            if (cell.isEnd()) {
                return 3.0; // End cells are least favorable, but still an option
            }
            if (cell instanceof FoodHexagon) {
                // Special handling for FoodHexagon cells based on the number of flies
                FoodHexagon foodCell = (FoodHexagon) cell;
                int numFlies = foodCell.getNumFlies();
                switch (numFlies) {
                    case 1: return 2.0; // Single fly cells are low priority
                    case 2: return 1.0; // Cells with two flies are even lower priority
                    case 3: return 0.0; // Cells with three flies are the least priority
                }
            }
        }
        return -1; // No valid priority could be assigned based on the conditions
    }

    public String findPath() {
        // Initialize a stack to keep track of the path taken by the frog.
        ArrayStack<Hexagon> S = new ArrayStack<>();
        // Start from the beginning of the pond.
        S.push(pond.getStart());
        // Mark the starting cell as part of the path.
        pond.getStart().markInStack();
        // Initialize the count of flies eaten.
        int fliesEaten = 0;
        // Initialize a string to describe the path taken.
        String emptyString = "";

        // Continue as long as there are cells in the stack.
        while (!S.isEmpty()) {
            // Peek at the current cell without removing it from the stack.
            Hexagon currCell = S.peek();
            // Append the current cell to the path description.
            emptyString = emptyString + currCell.toString() + " ";

            // If the current cell is the end of the pond, return the path description and flies eaten.
            if (currCell.isEnd()) {
                return emptyString + "ate " + fliesEaten + " flies";
            // If the current cell contains food, increase the flies eaten and remove the flies from the cell.
            } else if (currCell instanceof FoodHexagon) {
                FoodHexagon foodCell = (FoodHexagon) currCell;
                fliesEaten += foodCell.getNumFlies();
                foodCell.removeFlies();
            }

            // Find the best next cell to move to from the current cell.
            Hexagon nexCell = findBest(currCell);

            // If there is no suitable next cell, pop the current cell from the stack and mark it as not part of the path.
            if (nexCell == null) {
                S.pop();
                currCell.markOutStack();
            // Otherwise, push the next cell onto the stack and mark it as part of the path.
            } else {
                S.push(nexCell);
                nexCell.markInStack();
            }
        }
        // If no path to the end is found, return "No solution".
        return "No solution";
    }
    
    // copied from the assignment for testing purposes only
    public static void main (String[] args) {
    	 if (args.length != 1) {
    	 System.out.println("No map file specified in the arguments");
    	 return;
    	 }
    	 FrogPath fp = new FrogPath(args[0]);
    	 Hexagon.TIME_DELAY = 500; // Change this time delay as desired.
    	 String result = fp.findPath();
    	 System.out.println(result);
    }
}

