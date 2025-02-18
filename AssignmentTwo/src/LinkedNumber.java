public class LinkedNumber {

	private int base; // The base of the number system for this number (e.g., 2 for binary, 10 for decimal)
    private DLNode<Digit> front; // Reference to the first node in the doubly linked list
    private DLNode<Digit> rear; // Reference to the last node in the doubly linked list

  
    /**
     * Constructs a LinkedNumber from a string representation of a number and a specified base.
     * 
     * @param num     The string representation of the number.
     * @param baseNum The base of the number.
     * @throws LinkedNumberException If no digits are given or other input constraints are violated.
     */
    public LinkedNumber(String num, int baseNum) throws LinkedNumberException {
        // Check for null or empty input string
        if (num == null || num.isEmpty()) {
            throw new LinkedNumberException("no digits given");
        }

        this.base = baseNum; // Set the base of the number

        DLNode<Digit> prevNode = null; // To keep track of the previous node for linking

        char[] digits = num.toCharArray(); // Convert the input string to a char array for easy iteration
        for (char digitChar : digits) {
            // Initialize a Digit object for each character. Does not validate against base here.
            Digit digit = new Digit(digitChar);
            DLNode<Digit> currentNode = new DLNode<>(digit); // Create a new node for the digit

            if (prevNode != null) {
                // If not the first digit, link it with the previous node
                prevNode.setNext(currentNode);
                currentNode.setPrev(prevNode);
            } else {
                // If this is the first digit, initialize 'front' with the current node
                this.front = currentNode;
            }
            prevNode = currentNode; // Move to the next node in the list
        }

        this.rear = prevNode; // The last node processed becomes the 'rear' of the list
    }

 
    /**
     * Constructs a LinkedNumber from an integer, setting up a doubly linked list representation of the number in base 10.
     * 
     * @param num The integer number to be represented.
     * @throws LinkedNumberException If the given number leads to an invalid state (e.g., no digits given).
     */
    public LinkedNumber(int num) throws LinkedNumberException {
        this.base = 10; // For decimal numbers, the base is always 10
        
        // Convert the integer number to a string
        String numStr = String.valueOf(num);
        
        // Check if the string representation is empty
        if (numStr.isEmpty()) {
            throw new LinkedNumberException("no digits given");
        }

        DLNode<Digit> prevNode = null; // Tracks the previous node to link the current node with it
        for (int i = 0; i < numStr.length(); i++) {
            char digitChar = numStr.charAt(i); // Extracts each character (digit) from the string
            Digit digit = new Digit(digitChar); // Wraps the digit character into a Digit object
            DLNode<Digit> currentNode = new DLNode<>(digit); // Creates a new node for the digit

            if (prevNode != null) { // If not the first digit, link it with the previous node
                prevNode.setNext(currentNode); // Set the next of previous node to current node
                currentNode.setPrev(prevNode); // Set the prev of current node to previous node
            } else { // If this is the first digit
                this.front = currentNode; // Initialize the front of the list with the current node
            }
            prevNode = currentNode; // Move to the next node in the list
        }
        this.rear = prevNode; // The last node becomes the rear of the list
    } 
    
    /**
     * Checks if the number represented by the doubly linked list is valid for its base.
     * A number is considered valid if all its digits are within the range allowed by its base.
     *
     * @return true if the number is valid for its base; false otherwise.
     */
    public boolean isValidNumber() {
        DLNode<Digit> currentNode = this.front; // Start with the front node (least significant digit)
        while (currentNode != null) { // Iterate through each node in the list
            char digitChar = currentNode.getElement().toString().charAt(0); // Get the character representation of the digit
            int digitValue = Character.digit(digitChar, this.base); // Convert the character to its numerical value in the specified base
            // This will return -1 if the digitChar is not valid for the base
            if (digitValue == -1) {
                return false; // An invalid digit was found for the specified base, thus the number is invalid
            }
            currentNode = currentNode.getNext(); // Move to the next node (digit) in the list
        }
        return true; // All digits were valid for the specified base, thus the number is valid
    }


    
    /**
     * Returns the base of the number.
     *
     * @return The base in which this number is represented.
     */
    public int getBase() {
        return this.base; // Return the base of the number
    }

    /**
     * Returns the front node of the linked list representing the number.
     * The front node corresponds to the least significant digit.
     *
     * @return The front node of the doubly linked list.
     */
    public DLNode<Digit> getFront() {
        return this.front; // Return the reference to the front node of the list
    }

    /**
     * Returns the rear node of the linked list representing the number.
     * The rear node corresponds to the most significant digit.
     *
     * @return The rear node of the doubly linked list.
     */
    public DLNode<Digit> getRear() {
        return this.rear; // Return the reference to the rear node of the list
    }


    /**
     * Calculates the total number of digits in the linked list.
     *
     * @return The total number of digits (nodes) in this number.
     */
    public int getNumDigits() {
        int count = 0; // Initialize a counter to 0
        DLNode<Digit> currentNode = this.front; // Start from the front of the list
        while (currentNode != null) { // Loop until the end of the list is reached
            count++; // Increment the counter for each node encountered
            currentNode = currentNode.getNext(); // Move to the next node
        }
        return count; // Return the total count of nodes
    }

    @Override
    /**
     * Converts the linked list of digits into a string representation.
     *
     * @return A string that represents the sequence of digits in this number.
     */
    public String toString() {
        StringBuilder numberString = new StringBuilder(); // Create a StringBuilder to accumulate the digits
        DLNode<Digit> currentNode = this.front; // Start from the front of the list
        while (currentNode != null) { // Loop until the end of the list is reached
            numberString.append(currentNode.getElement().toString()); // Append the digit's string representation to the builder
            currentNode = currentNode.getNext(); // Move to the next node
        }
        return numberString.toString(); // Convert the builder to a String and return it
    }

    
    /**
     * Compares this LinkedNumber object with another object for equality.
     *
     * @param obj The object to compare this LinkedNumber against.
     * @return true if the given object represents a LinkedNumber equivalent to this number, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        // Check if the references point to the same object
        if (this == obj) {
            return true;
        }
        // Check if the object is null or if the classes of the two objects differ
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Cast the object to a LinkedNumber
        LinkedNumber other = (LinkedNumber) obj;

        // Check if the bases of the two numbers are different
        if (this.base != other.base) {
            return false;
        }

        // Initialize pointers to traverse both lists from the front
        DLNode<Digit> thisCurrent = this.front;
        DLNode<Digit> otherCurrent = other.front;

        // Traverse both lists simultaneously
        while (thisCurrent != null && otherCurrent != null) {
            // Compare the elements of the two nodes
            if (!thisCurrent.getElement().equals(otherCurrent.getElement())) {
                return false; // Return false if any pair of digits is different
            }
            // Move to the next node in both lists
            thisCurrent = thisCurrent.getNext();
            otherCurrent = otherCurrent.getNext();
        }

        // After the loop, check if both pointers are null (end of both lists)
        // If either list still has elements, the numbers are not equal
        return thisCurrent == null && otherCurrent == null;
    }

    
    /**
     * Converts the current LinkedNumber to a new base.
     *
     * @param newBase The base to which the current number will be converted.
     * @return A new LinkedNumber object representing the number in the new base.
     * @throws LinkedNumberException If the current number is invalid or the conversion cannot be completed.
     */
    public LinkedNumber convert(int newBase) throws LinkedNumberException {
        if (!isValidNumber()) {
            throw new LinkedNumberException("cannot convert invalid number");
        }
        
        int decimalValue = toDecimal(); // Convert the current number to decimal
        LinkedNumber newNumber = fromDecimal(decimalValue, newBase); // Convert from decimal to the new base
        return newNumber;
    }


    /**
     * Converts this LinkedNumber to its decimal equivalent.
     *
     * @return The decimal value of this LinkedNumber.
     */
    private int toDecimal() {
        int decimalValue = 0; // Initialize the decimal value to 0
        DLNode<Digit> current = rear; // Start from the rear (least significant digit)
        int position = 0; // Position of the digit in the number
        
        while (current != null) { // Traverse the linked list
            int digitValue = current.getElement().getValue(); // Get the digit's value
            decimalValue += digitValue * Math.pow(base, position); // Add its contribution to the total decimal value
            position++; // Move to the next digit's position
            current = current.getPrev(); // Move to the next digit in the list
        }
        
        return decimalValue; // Return the computed decimal value
    }


    /**
     * Converts a decimal number to its representation in a specified base and returns it as a LinkedNumber object.
     * This method supports conversion to bases between 2 and 16, inclusive.
     *
     * @param decimalValue The decimal integer to be converted.
     * @param newBase The base to which the decimal number should be converted. Must be between 2 and 16, inclusive.
     * @return A LinkedNumber object representing the number in the new base.
     * @throws LinkedNumberException If the specified base is outside the allowed range (2 to 16, inclusive).
     */
    private LinkedNumber fromDecimal(int decimalValue, int newBase) throws LinkedNumberException {
        // Validate the new base
        if (newBase < 2 || newBase > 16) {
            throw new LinkedNumberException("Base must be between 2 and 16");
        }

        StringBuilder sb = new StringBuilder(); // To build the number in the new base as a string
        // Convert the decimal value to the new base
        while (decimalValue > 0) {
            int remainder = decimalValue % newBase; // Get the remainder (next digit in the new base)
            // Convert the remainder to the corresponding digit character
            sb.append(remainder < 10 ? (char) ('0' + remainder) : (char) ('A' + remainder - 10));
            decimalValue /= newBase; // Reduce the decimal value for the next iteration
        }

        // Handle the case for decimalValue == 0, which wasn't covered in the loop
        if (sb.length() == 0) {
            sb.append('0'); // Represent 0 in the new base
        }

        sb.reverse(); // The conversion process builds the string in reverse, so reverse it to correct the order

        // Create a new LinkedNumber object with the string representation and the specified base
        return new LinkedNumber(sb.toString(), newBase);
    }


    
    /**
     * Adds a new digit to the doubly linked list at the specified position. The list represents a number,
     * and digits are added such that the list navigates from the rear (least significant digit)
     * towards the front (most significant digit).
     * 
     * @param digit The digit to be added to the list. This is wrapped in a DLNode.
     * @param position The zero-based position from the right (rear) where the digit is to be added.
     *                 Position 0 adds the digit as the new least significant digit, and position equal to the
     *                 number of digits in the list adds the digit as the new most significant digit.
     * @throws LinkedNumberException If the position is invalid (less than 0 or greater than the current number of digits).
     */
    public void addDigit(Digit digit, int position) {
        int numDigits = getNumDigits(); // Determine the current size of the list

        // Validate the position parameter
        if (position < 0 || position > numDigits) {
            throw new LinkedNumberException("Invalid position");
        }

        DLNode<Digit> newNode = new DLNode<>(digit); // Create a new node for the digit

        // Handle the addition of the new least significant digit
        if (position == 0) {
            if (rear == null) { // List is empty
                front = newNode; // New node becomes both the front and the rear
                rear = newNode;
            } else { // List is not empty
                rear.setNext(newNode); // Insert new node after the current rear
                newNode.setPrev(rear); // Set the current rear as the previous node of the new node
                rear = newNode; // Update the rear to the new node
            }
        }
        // Handle the addition of the new most significant digit
        else if (position == numDigits) {
            if (front == null) { // List is empty
                front = newNode; // New node becomes both the front and the rear
                rear = newNode;
            } else { // List is not empty
                newNode.setNext(front); // Insert new node before the current front
                front.setPrev(newNode); // Set the new node as the previous node of the current front
                front = newNode; // Update the front to the new node
            }
        }
        // Handle the addition of a digit in the middle of the list
        else {
            DLNode<Digit> current = rear;
            for (int i = 0; i < position; i++) {
                current = current.getPrev(); // Navigate towards the front to the specified insertion point
            }
            newNode.setPrev(current); // Set the current node as the previous node of the new node
            newNode.setNext(current.getNext()); // Set the next node of the current as the next node of the new node
            current.getNext().setPrev(newNode); // Link the next node back to the new node
            current.setNext(newNode); // Link the current node forward to the new node
        }
    }


    /**
     * Removes a digit from a doubly linked list representing a number, based on the given position.
     * The list is navigated from the rear (least significant digit) towards the front (most significant digit).
     * 
     * @param position The zero-based position of the digit to remove from the right (rear) end of the list.
     *                 For example, position 0 would remove the least significant digit.
     * @return The integer value of the digit removed, adjusted for its positional value in the overall number.
     * @throws LinkedNumberException If the given position is invalid (less than 0 or greater than the number of digits).
     */
    public int removeDigit(int position) throws LinkedNumberException {
        // Validate the position parameter
        if (position < 0 || position >= getNumDigits()) {
            throw new LinkedNumberException("Invalid position");
        }

        // Start from the rear of the list and move forward to find the node to remove
        DLNode<Digit> current = rear;
        for (int i = 0; i < position; i++) {
            current = current.getPrev(); // Navigate towards the front to the specified position
        }

        // Calculate the value of the digit being removed, considering its base and position
        int positionalValue = (int) (current.getElement().getValue() * Math.pow(base, position));

        // Handle removal from a single-element list
        if (current == front && current == rear) {
            front = null; // The list becomes empty
            rear = null;
        }
        // Handle removal of the most significant digit
        else if (position == getNumDigits() - 1) {
            front = current.getNext(); // Move front to the next element
            front.setPrev(null); // Detach the new front from the removed node
        }
        // Handle removal of the least significant digit
        else if (position == 0) {
            rear = current.getPrev(); // Move rear to the previous element
            rear.setNext(null); // Detach the new rear from the removed node
        }
        // Handle removal of a middle element
        else {
            current.getPrev().setNext(current.getNext()); // Link previous node to the next, skipping over current
            current.getNext().setPrev(current.getPrev()); // Link next node back to the previous, excluding current
        }

        return positionalValue; // Return the calculated positional value of the removed digit
    }


}
    
    

    
    

   
