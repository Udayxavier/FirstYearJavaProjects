
public class testClass {
	
	public static void main(String[] args) {
	    // Initialize a LetterCrush object with a given string
	    LetterCrush testCrush = new LetterCrush(5, 6, "BCAABBBACABCABCCCCAAACCCACCABC");
	    // Create a Line object representing a horizontal line starting at (2,1) with length 4
	    Line testLine = new Line(2, 1, true, 4);
	    // Get the string representation of the grid with the Line highlighted
	    String crushString = testCrush.toString(testLine);
	    // Print the string representation to check it
	    System.out.println(crushString);
	}
}
