package ecosystem.utils;

import ecosystem.Ecosystem;
import ecosystem.entities.*;
import java.util.*;

public class ObjectsCreator {

    /**
     * Creates an Animal based on user input.
     *
     * @param scanner Scanner object for user input.
     * @return An instance of Animal or null if invalid input.
     */
    public static Animal createAnimal(Scanner scanner) {
        Set<String> validSpecies = new HashSet<>(Arrays.asList("herbivore", "carnivore", "omnivore"));
        Map<String, Integer> foodChainLevels = Map.of(
                "primary", 1,
                "secondary", 2,
                "tertiary", 3
        );

        System.out.println("Enter the name of the animal: ");
        String name = scanner.next();

        String species = UserInput.getValidStringInput(scanner, "Enter species (Herbivore/Carnivore/Omnivore): ",
                validSpecies, "Error: invalid species. Please try again.");

        String levelStr = UserInput.getValidStringInput(scanner, "Enter the food chain level (Primary/Secondary/Tertiary): ",
                foodChainLevels.keySet(), "Error: invalid level. Please try again.");
        int foodChainLevel = foodChainLevels.get(levelStr.toLowerCase());

        int energy = UserInput.getValidIntInput("Enter energy (value from 0 to 180): ", 0, 180);

        int lifetime = UserInput.getValidIntInput("Enter lifetime (number of years, from 1 to 100): ", 1, 100);

        int currentLifeTime = UserInput.getValidIntInput("Enter current lifetime (number of years, from 1 to 100): ", 1, 100);

        // Create and return the appropriate animal instance
        return switch(species.toLowerCase()){
            case "herbivore" -> new Herbivore(name, energy, foodChainLevel, lifetime, currentLifeTime);
            case "carnivore" -> new Carnivore(name, energy, foodChainLevel, lifetime, currentLifeTime);
            case "omnivore" -> new Omnivore(name, energy, foodChainLevel, lifetime, currentLifeTime);
            default -> null; // This should never occur due to prior validation
        };

    }

    /**
     * Method to create a Plant based on user input.
     *
     * @param scanner Scanner object for user input.
     * @return An instance of Plant.
     */
    public static Plant createPlant(Scanner scanner) {

        System.out.println("Enter the name of the plant: ");
        String name = scanner.next();

        int growthLevel = UserInput.getValidIntInput("Enter growth level (number from 0 to 18):", 0, 18);

        int waterNeeds = UserInput.getValidIntInput("Enter the amount of water needed for growth (number from 0 to 15):", 0, 15);

        int optimalTemperature = UserInput.getValidIntInput("Enter optimal temperature (number from -30 to 38):", -30, 38);

        return new Plant(name, growthLevel, waterNeeds, optimalTemperature);
    }

    /**
     * Method to initialize and configure a new ecosystem with user-defined parameters.
     * This method prompts the user to enter ecosystem attributes, such as temperature, humidity,
     * and available water, then allows the user to add animals and plants interactively.
     * Once configuration is complete, it returns a fully instantiated Ecosystem object.
     *
     * @param scanner A Scanner object for reading user input.
     * @return A new Ecosystem object initialized with user-specified attributes and entities.
     */
    public static Ecosystem createNewEcosystem(Scanner scanner) {

        // Prompt user for initial environmental conditions
        int temperature = UserInput.getValidIntInput("Enter the initial temperature (from -30 to 38): ", -30, 38);
        int humidity = UserInput.getValidIntInput("Enter the initial humidity (from 0 to 100): ", 0, 100);
        int waterAmount = UserInput.getValidIntInput("Enter the amount of available water (from 0 to 1000000): ", 0, 1000000);

        // Initialize lists to store animals and plants
        List<Animal> animals = new ArrayList<>();
        List<Plant> plants = new ArrayList<>();

        // Interactive loop to allow user to add animals and plants
        System.out.println("Add animals and plants to the ecosystem: ");
        while (true){
            System.out.println("1. Add an animal");
            System.out.println("2. Add a plant");
            System.out.println("3. Complete ecosystem creation");
            String choice = scanner.next();
            switch (choice){
                case "1":
                    // Add a new animal based on user input
                    animals.add(createAnimal(scanner));
                    break;
                case "2":
                    // Add a new plant based on user input
                    plants.add(createPlant(scanner));
                    break;
                case "3": return new Ecosystem(temperature, humidity, waterAmount, animals, plants);
                default:
                    System.out.println("Please enter a valid option");
                    break;
            }
        }
    }
}
