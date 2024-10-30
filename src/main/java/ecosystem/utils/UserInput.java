package ecosystem.utils;

import java.util.Scanner;
import java.util.Set;

public class UserInput {

    /**
     * Prompts the user to enter an integer within a specified range.
     * Ensures that the input is a valid integer and falls within the
     * range defined by `min` and `max` values.
     *
     * @param prompt The message displayed to prompt the user for input.
     * @param min The minimum acceptable value (inclusive).
     * @param max The maximum acceptable value (inclusive).
     * @return The validated integer input from the user.
     */
    public static int getValidIntInput(String prompt, int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int input;

        // Loop until a valid integer within the specified range is received
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.printf("Error! Please enter a value between %d and %d.%n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! Please enter an integer.");
            }
        }
    }

    /**
     * Prompts the user to enter a string value and validates the input
     * against a set of acceptable values. The input is converted to lowercase
     * to ensure case-insensitive matching. If the input is not in the set of
     * valid values, an error message is displayed, and the prompt repeats.
     *
     * @param scanner      The Scanner object used for user input.
     * @param prompt       The message displayed to prompt the user for input.
     * @param validValues  A set of acceptable string values for validation.
     * @param errorMessage The error message displayed for invalid input.
     * @return The validated string input from the user.
     */
    public static String getValidStringInput(Scanner scanner, String prompt, Set<String> validValues, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next().toLowerCase();
            if (validValues.contains(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
}
