package BPP2D;

import java.sql.Array;
import java.util.*;

public class Container {

    int HEIGHT, WIDTH;
    ArrayList<Node> nodes;
    ArrayList<ArrayList<Integer>> open_sp, taken_point;

    // Constructor for BPP2D.Container
    public Container(int height, int width) {
        HEIGHT = height;
        WIDTH = width;
        nodes = new ArrayList<>();
        open_sp = new ArrayList<>(); // avialable starting points
        taken_point = new ArrayList<>(); // not available

        // Set default corners
        ArrayList<Integer> corner1 = new ArrayList<>(); // bottom-left
        corner1.add(0);
        corner1.add(0);

        ArrayList<Integer> corner2 = new ArrayList<>(); // upper-left
        corner2.add(0);
        corner2.add(this.HEIGHT);

        ArrayList<Integer> corner3 = new ArrayList<>(); // bottom-right
        corner3.add(this.WIDTH);
        corner3.add(0);

        ArrayList<Integer> corner4 = new ArrayList<>(); // upper right
        corner4.add(this.WIDTH);
        corner4.add(this.HEIGHT);

        open_sp.add(corner1);
        open_sp.add(corner2);
        open_sp.add(corner3);
        open_sp.add(corner4);
    }

    public void insertNode(int x, int y, Node temp_node){
        // Clear trials before permanent insertion
        temp_node.bottom_left.clear();
        temp_node.upper_left.clear();
        temp_node.bottom_right.clear();
        temp_node.upper_right.clear();

        temp_node.setPositions(x, y); // set position
        this.nodes.add(temp_node); // add to nodes list
    }

    public ArrayList<Integer> bestPosition (Node a) {
        ArrayList<Integer> optimal_pos = new ArrayList<>(); // initialize empty 2d array

        if (this.nodes.isEmpty()) {
            // no packed
            optimal_pos = this.open_sp.get(0); // 0, 0 position
        } else {
            // iterate over open_sp and check fittings
            for (int i = 0; i < this.open_sp.size(); i++) {
                ArrayList<Integer> coordinate = this.open_sp.get(i);
                //System.out.println(this.open_sp.get(i));

                // iterate over nodes to check for overlapping positions
                for (int elem=0; elem < this.nodes.size(); elem++) {
                    int end_x = coordinate.get(0) + a.WIDTH;
                    int end_y = coordinate.get(1) + a.HEIGHT;
                    // starting point is open corner
                    // either end point should not overlapping and exceeding container
                    if ((end_x > this.nodes.get(elem).bottom_left.get(0) && end_x < this.nodes.get(elem).upper_right.get(0)) ||
                            (end_y > this.nodes.get(elem).bottom_left.get(1) && end_y < this.nodes.get(elem).upper_left.get(1)) ||
                            (end_x > this.WIDTH || end_y > this.HEIGHT)) {
                        // overlapping, since x or y is greater than starting point
                        // do nothing

                        /*
                        // For debugging purposes
                        System.out.println(end_x > this.nodes.get(elem).bottom_left.get(0));
                        System.out.println(end_x < this.nodes.get(elem).upper_right.get(0));
                        System.out.println(end_y > this.nodes.get(elem).bottom_left.get(1));
                        System.out.println(end_y < this.nodes.get(elem).upper_left.get(1));
                        System.out.println(end_x > this.WIDTH);
                        System.out.println(end_y > this.HEIGHT);
                        System.out.println("Something went wrong");
                        */

                        continue;
                    } else {
                        // non-overlapping insert coordinate to optimal pos
                        int max_fit = 0;
                        int fit = fitness(a, coordinate);

                        System.out.println(fit + " : " + coordinate);
                        if (max_fit <= fit) {
                            max_fit = fit;
                            optimal_pos.clear();
                            optimal_pos.add(coordinate.get(0)); // x value
                            optimal_pos.add(coordinate.get(1)); // y value
                        }
                    }
                }
            }
        }
        // System.out.println(optimal_pos);

        return optimal_pos;
    }

    public void update_corners(Node a) {

        // TODO: Find open corners and update this.open_sp

//        for (int i = 0; i < this.nodes.size(); i++) {
//            Node primaryNode = this.nodes.get(i); // Get a primary node for comparison
//            ArrayList<ArrayList<Integer>> node_corners = new ArrayList<>(); // Create a ist of corners of current node
//            node_corners.add(primaryNode.bottom_left); // add corners to arraylist
//            node_corners.add(primaryNode.upper_left);
//            node_corners.add(primaryNode.bottom_right);
//            node_corners.add(primaryNode.upper_right);
//
//            for (int index = 0; index < node_corners.size(); index++) {
//                this.open_sp = filter_corners(this.open_sp, node_corners.get(index));
//            }
//        }


        Node primaryNode = a; // Get a primary node for comparison
        ArrayList<ArrayList<Integer>> node_corners = new ArrayList<>(); // Create a ist of corners of current node
        node_corners.add(primaryNode.bottom_left); // add corners to arraylist
        node_corners.add(primaryNode.upper_left);
        node_corners.add(primaryNode.bottom_right);
        node_corners.add(primaryNode.upper_right);

        for (int index = 0; index < node_corners.size(); index++) {
            this.open_sp = filter_corners(this.open_sp, node_corners.get(index));
        }

        // For debugging purposes
        // System.out.println(this.open_sp);

    }

    private int difference(int initial, int secondary) {
        return Math.abs(initial - secondary);
    }

    private ArrayList<ArrayList<Integer>> filter_corners(ArrayList<ArrayList<Integer>> cornersList, ArrayList<Integer> corner) {
        if (cornersList.contains(corner) && !(this.taken_point.contains(corner))) {
            this.taken_point.add(corner);
            cornersList.remove(corner); // remove corner if already in cornersList
        } else if (this.taken_point.contains(corner) && cornersList.contains(corner)) {
            cornersList.remove(corner);
        } else {
            cornersList.add(corner); // if not in corners list add
        }

        return cornersList;
    }

    private int touch_length (Node a, Node b) {
        int tl = 0;

        /*
        // For debugging purposes
        System.out.println(a.bottom_left + " " + b.bottom_right);
        System.out.println(a.upper_left + " " + b.upper_right);
        System.out.println(a.bottom_right + " " + b.bottom_left);
        System.out.println(a.upper_right + " " + b.upper_left);
        System.out.println(a.bottom_right + " " + b.bottom_left);
        System.out.println(a.upper_left + " " + b.bottom_left);
        System.out.println(a.upper_right + " " + b.bottom_right);
        */

        // variations
        // left && right side -- upward ; downward
        // bottom && top side -- rightward : leftward

        // left upward
        if (a.bottom_left == b.bottom_right) {
            // comparison on y-axis
            tl += difference(a.upper_left.get(1), b.upper_right.get(1));
        }
        // left downward
        else if (a.upper_left == b.upper_right) {
            // comparison on y-axis
            tl += difference(a.bottom_left.get(1), b.bottom_right.get(1));
        }
        // right upward
        else if (a.bottom_right == b.bottom_left) {
            // comaprison on y-axis
            tl += difference(a.upper_right.get(1), b.upper_left.get(1));
        }
        // right downward
        else if (a.upper_right == b.upper_left) {
            // comparison on y-axis
            tl += difference(a.bottom_right.get(1), b.bottom_left.get(1));
        }
        // bottom rightward
        else if (a.bottom_left == b.upper_left) {
            // compatison on x-axis
            tl += difference(a.bottom_right.get(0), b.upper_right.get(0));
        }
        // bottom leftward
        else if (a.bottom_right == b.upper_right) {
            // comaprison on x-axis
            tl += difference(a.bottom_left.get(0), b.upper_left.get(0));
        }
        // top rightward
        else if (a.upper_left == b.bottom_left) {
            // comparison on x-axis
            tl += difference(a.upper_right.get(0), b.bottom_right.get(0));
        }
        // top rightward
        else if (a.upper_right == b.bottom_right) {
            // comparison on x-axis
            tl += difference(a.upper_left.get(0), b.bottom_left.get(0));
        }

        else if (a.bottom_left.get(0) == 0 || a.upper_right.get(0) == this.WIDTH) {
            tl += a.HEIGHT;
        }

        else if (a.bottom_left.get(1) == 0 ||a.upper_right.get(1) == this.HEIGHT) {
            tl += a.WIDTH;
        }

        return tl;
    }

    public int fitness(Node a, ArrayList<Integer> position) {
        int ttl = 0;

        // Set position
        a.setPositions(position.get(0), position.get(1));

        // TODO: iterate over each nodes and find length of touching edges and corner
        for (int i = 0; i < this.nodes.size(); i++) {
            Node temp_node = this.nodes.get(i);
            int tl = touch_length(a, temp_node);
            ttl += tl;
        }
        /*
        System.out.println(a.bottom_left);
        System.out.println(a.bottom_right);
        System.out.println(a.upper_left);
        System.out.println(a.upper_right);
        */
        a.bottom_left.clear();
        a.bottom_right.clear();
        a.upper_right.clear();
        a.upper_left.clear();
        return ttl;
    }
}
