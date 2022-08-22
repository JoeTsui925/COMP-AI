package BPP2D;

import java.util.ArrayList;

public class Node {

    int HEIGHT, WIDTH;
    ArrayList<Integer> bottom_left, upper_right, upper_left, bottom_right;

    // Constructor for Node
    public Node(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        bottom_left = new ArrayList<>(); // starting point
        upper_left = new ArrayList<>(); // above starting point
        upper_right = new ArrayList<>(); // end point
        bottom_right = new ArrayList<>(); // below end point
    }

    public void setPositions(int x_start, int y_start) {
        // index 0 is equivalent to x
        // index 1 id equivalent to y

        // set starting points
        this.bottom_left.add(x_start);
        this.bottom_left.add(y_start);

        // set end points
        this.upper_right.add(x_start + this.WIDTH);
        this.upper_right.add(y_start + this.HEIGHT);

        // set above starting
        this.upper_left.add(x_start);
        this.upper_left.add(y_start + this.HEIGHT);

        // set below endpoint
        this.bottom_right.add(x_start + this.WIDTH);
        this.bottom_right.add(y_start);
    }

}
