/**
 * Represents a priority queue implementation using arrays.
 *
 * @param <T> the type of elements in the priority queue
 */
public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T> {

    private T[] queue;
    private double[] priority;
    private int count;

    /**
     * Constructs a new ArrayUniquePriorityQueue with an initial capacity of 10.
     */
    public ArrayUniquePriorityQueue() {
        final int INITIAL_CAPACITY = 10;
        queue = (T[]) new Object[INITIAL_CAPACITY];
        priority = new double[INITIAL_CAPACITY];
        count = 0;
    }

    /**
     * Adds an element to the priority queue with the specified priority, if it's not already present.
     *
     * @param data the element to add
     * @param prio the priority of the element
     */
    public void add(T data, double prio) {
        if (contains(data)) {
            return;
        }

        if (count == queue.length) {
            T[] newQueue = (T[]) new Object[queue.length * 2];
            double[] newPriority = new double[priority.length * 2];
            System.arraycopy(queue, 0, newQueue, 0, count);
            System.arraycopy(priority, 0, newPriority, 0, count);
            queue = newQueue;
            priority = newPriority;
        }
        int index = 0;
        while (index < count && priority[index] < prio) {
            index++;
        }
        while (index < count && priority[index] == prio) {
            index++;
        }

        for (int i = count - 1; i >= index; i--) {
            queue[i + 1] = queue[i];
            priority[i + 1] = priority[i];
        }

        queue[index] = data;
        priority[index] = prio;
        count++;
    }

    /**
     * Checks if the priority queue contains the specified element.
     *
     * @param data the element to check for
     * @return true if the element is present, false otherwise
     */
    public boolean contains(T data) {
        for (int i = 0; i < count; i++) {
            if (queue[i].equals(data)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the element with the highest priority from the priority queue without removing it.
     *
     * @return the element with the highest priority
     * @throws CollectionException if the priority queue is empty
     */
    public T peek() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException("PQ is empty");
        }

        int minIndex = 0;
        for (int i = 1; i < count; i++) {
            if (priority[i] < priority[minIndex]) {
                minIndex = i;
            }
        }

        return queue[minIndex];
    }

    /**
     * Removes and returns the element with the highest priority from the priority queue.
     *
     * @return the element with the highest priority
     * @throws CollectionException if the priority queue is empty
     */
    public T removeMin() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException("PQ is empty");
        }

        int minIndex = 0;
        for (int i = 1; i < count; i++) {
            if (priority[i] < priority[minIndex]) {
                minIndex = i;
            }
        }

        T minItem = queue[minIndex];

        for (int i = minIndex; i < count - 1; i++) {
            queue[i] = queue[i + 1];
            priority[i] = priority[i + 1];
        }

        count--;

        return minItem;
    }

    /**
     * Updates the priority of an element in the priority queue.
     *
     * @param data    the element whose priority needs to be updated
     * @param newPrio the new priority of the element
     * @throws CollectionException if the specified element is not found in the priority queue
     */
    public void updatePriority(T data, double newPrio) throws CollectionException {
        if (!contains(data)) {
            throw new CollectionException("Item not found in PQ");
        } else {
            int index = 0;

            for (int i = 0; i < count; i++) {
                if (queue[i].equals(data)) {
                    index = i;
                    break;
                }
            }

            priority[index] = newPrio;

            T tempData = queue[index];

            for (int i = index; i < count - 1; i++) {
                priority[i] = priority[i + 1];
                queue[i] = queue[i + 1];
            }

            count--;

            add(tempData, newPrio);
        }
    }

    /**
     * Checks if the priority queue is empty.
     *
     * @return true if the priority queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in the priority queue.
     *
     * @return the number of elements in the priority queue
     */
    public int size() {
        return count;
    }

    /**
     * Returns the current capacity of the priority queue.
     *
     * @return the current capacity of the priority queue
     */
    public int getLength() {
        return queue.length;
    }

    /**
     * Returns a string representation of the priority queue.
     *
     * @return a string representation of the priority queue
     */
    public String toString() {
        if (isEmpty()) {
            return "The PQ is empty";
        }

        String result = "";

        for (int i = 0; i < count; i++) {
            result += queue[i] + " [" + priority[i] + "]";

            if (i < count - 1) {
                result += ", ";
            }
        }

        return result;
    }
}