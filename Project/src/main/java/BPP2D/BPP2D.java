package BPP2D;

import java.util.*;
import java.lang.*;

public class BPP2D {

    public static void main(String[] args) {

        ArrayList<Container> pack_bin = new ArrayList<>(); // List of pack_bin
        ArrayList<Node> items = new ArrayList<>(); // List of generated items for packing sequence

        int container_width = 10, container_height = 10; // container size
        Random rand_size = new Random(); // Randomizer for size of node
        Scanner user = new Scanner(System.in); // Scanner for user input
        boolean running = true; // true until all sequences are packed
        int generate = 0; // User input value


        // Creation of Packing Sequence -- for Testing
        System.out.println("Generate a specific number of sequences");
        System.out.print("Enter an integer: ");
        // User Input
        try {
            generate = user.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println(generate); // For debugging purposes -- uncomment to see result
        // Generate nodes equal to number of input
        for (int i=0; i < generate; i++) {
            int size_w = rand_size.nextInt(5); // random width size
            int size_h = rand_size.nextInt(5); // random height size

            while (size_h == 0 || size_w == 0){
                size_w = rand_size.nextInt(5);
                size_h = rand_size.nextInt(5);
            }

            Node item = new Node(size_w, size_h); // Create an instance of an object
            items.add(item);
        }
        // System.out.println(packing_sequence.size()); // For debugging purposes : must be a match to user input -- uncomment to see result

        // TODO: Main Algorithm here **********
        // check bins if empty ? yes, open a bin : no, proceed to packing
        // while items is not empty -> proceed to packing
        // item packed ? yes, remove from items : no, repeat
        if (pack_bin.isEmpty()) {
            // open a bin
            Container bin = new Container(container_height, container_width);
            pack_bin.add(bin);
            System.out.println("Bin is Added");
        } // otherwise do nothing proceed to packing

        int counter = 0;
        while (!items.isEmpty()) {
            Node current_item = items.get(counter); // Get Item to be packed in bin

            // For debugging purposes
            // System.out.println(current_item);

            ArrayList<Integer> bin_numbers = new ArrayList<>(); // corresponding bin number
            ArrayList<ArrayList<Integer>> best_positions = new ArrayList<>(); // corresponding best positions

            // best positions every bin
            for (int num = 0; num < pack_bin.size(); num++) {
                Container temp_bin = pack_bin.get(num);
                ArrayList<Integer> bin_best_position = temp_bin.bestPosition(current_item);
                bin_numbers.add(num);
                best_positions.add( bin_best_position );

                // For debugging purposes
                // System.out.println(num);
                // System.out.println(bin_best_position);
            }

            // For debugging purposes
            // System.out.println(best_positions);

            // best position is empty ? true, open new bin : false, find best posiition in every bin
            if (best_positions.isEmpty() || best_positions.get(0).isEmpty()) {
                // no available space
                Container new_bin = new Container(container_height, container_width); // Create an new bin
                new_bin.insertNode(0, 0, current_item); // Insert current item to newly created bin
                pack_bin.add(new_bin); // Add new bin to pack bin list

                // For debugging purposes
                System.out.println("New bin Added and Item inserted");

            } /*else if (best_positions.size() == 1) {
                int best_fitting_bin = bin_numbers.get(best_positions.indexOf(best_positions.get(0))); // Set bin id of best position in a bin
                pack_bin.get(best_fitting_bin).insertNode(best_positions.get(0).get(0), best_positions.get(0).get(1), current_item); // insert node in the specific bin
                pack_bin.get(best_fitting_bin).update_corners(); // update corners when inserted

                // For debugging purposes
                // System.out.println("Item Added\n");
            }*/
             else {
                // find maximal fit each position
                ArrayList<Integer> best_fitting_position = new ArrayList<>();
                int fitting = 0;

                for (int i = 0; i < best_positions.size(); i++) {
                    // position and best fitting
                    int bin_id = bin_numbers.get(i);
                    int tl = pack_bin.get(bin_id).fitness(current_item, best_positions.get(i));
                    if (fitting <= tl) {
                        fitting = tl; // Update fitting by touch length
                        best_fitting_position.clear();
                        best_fitting_position = best_positions.get(i);
                    }
                }

                // For debugging purposes
                // System.out.println(best_fitting_position);
                // System.out.println(best_positions.indexOf(best_fitting_position));

                int best_fitting_bin = bin_numbers.get(best_positions.indexOf(best_fitting_position)); // Set bin id of best position in a bin
                pack_bin.get(best_fitting_bin).insertNode(best_fitting_position.get(0), best_fitting_position.get(1), current_item); // insert node in the specific bin
                pack_bin.get(best_fitting_bin).update_corners(current_item); // update corners when inserted
            }

            items.remove(current_item); // Remove inserted item in a bin
        }

        pack_bin.forEach((bin) -> {
            for (Node item : bin.nodes) {
                System.out.format(bin + " : " + item.bottom_left + " " + item.upper_right + "\n");
            }
        });

    }
    /*
    private static int lower_bounds(int w, int h, ArrayList<Node> packing_sequence) {
        int numerator = 0;
        int denominator = w * h;

        for (int i=0; i < packing_sequence.size(); i++) {
            numerator += packing_sequence.get(i).WIDTH * packing_sequence.get(i).HEIGHT;
        }

        return numerator / denominator;
    }

     */
}
