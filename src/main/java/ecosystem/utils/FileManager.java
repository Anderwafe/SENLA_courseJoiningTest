package ecosystem.utils;

import ecosystem.Ecosystem;
import ecosystem.entities.*;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {

    /**
     * Saves the state of the ecosystem to a file, including ecosystem parameters,
     * details of animals and plants. Translates classification of animals and their
     * food chain levels from code terminology to user-friendly terms.
     *
     * @param ecosystem The ecosystem to be saved.
     * @param filePath  The file path where the ecosystem data will be saved.
     */
    public static void saveEcosystem(Ecosystem ecosystem, String filePath) {

        // Maps to translate food chain levels
        Map<Integer, String> foodChainLevels = Map.of(
                1, "Primary",
                2, "Secondary",
                3, "Tertiary"
        );

        // Check if ecosystem exists
        if(ecosystem == null){
            System.out.println("The ecosystem does not exist.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Saving general ecosystem parameters
            writer.write("Temperature: " + ecosystem.getTemperature());
            writer.newLine();
            writer.write("Humidity: " + ecosystem.getHumidity());
            writer.newLine();
            writer.write("Water amount: " + ecosystem.getWaterAmount());
            writer.newLine();

            // Saving animals
            writer.write("Animals:");
            writer.newLine();
            int number = 0;
            if(ecosystem.getAnimals() != null){
                for (Animal animal : ecosystem.getAnimals()) {
                    number++;
                    String animalClass = animal.getClass().getSimpleName();
                    writer.write(number + ". Name: " + animal.getName() + ", Species: " + animalClass +
                            ", Food Chain Level: " + foodChainLevels.get(animal.getFoodChainLevel()) +
                            ", Energy: " + animal.getEnergy() + ", Average Lifespan: " + animal.getLifeTime() +
                            " hours, Age: " + animal.getCurrentLifeTime() + " hours, Count: 1");
                    writer.newLine();
                }
            }else System.out.println("No animals in the ecosystem.");


            // Saving plants
            writer.write("Plants:");
            writer.newLine();
            number = 0;
            if (ecosystem.getPlants() != null){
                for (Plant plant : ecosystem.getPlants()) {
                    number++;
                    writer.write(number + ". Name: " + plant.getName() +
                            ", Growth Level: " + plant.getGrowthLevel() +
                            ", Water Needs: " + plant.getWaterNeeds() +
                            " per day, Optimal Temperature: " + plant.getOptimalTemperature() +
                            " degrees, Count: 1");
                    writer.newLine();
                }
            }else System.out.println("No plants in the ecosystem.");

            System.out.println("Ecosystem saved successfully.");
        } catch (IOException e) {
            System.out.println("Error while saving data.");
        }
    }

    /**
     * Loads an ecosystem from a file, parsing temperature, humidity, water amount,
     * and lists of animals and plants with error checks on each property.
     *
     * @param filePath The file path from which the ecosystem data will be loaded.
     * @return An Ecosystem object if loaded successfully; otherwise, null.
     */
    public static Ecosystem loadEcosystem(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Animal> animals = new ArrayList<>();
            List<Plant> plants = new ArrayList<>();
            int temperature, humidity, waterAmount;
            String line;
            int errorsCount = 0; // Tracks errors in file format
            int plantOrAnimalNumber;

            // Read and validate temperature
            line = reader.readLine();
            temperature = extractTemperature(line);
            if(temperature < -30 || temperature > 38){
                System.out.println("Error in temperature format");
                errorsCount++;
            }

            // Read and validate humidity
            line = reader.readLine();
            humidity = extractHumidity(line);
            if(humidity < 0 || humidity > 100){
                System.out.println("Error in humidity format");
                errorsCount++;
            }

            // Read and validate water amount
            line = reader.readLine();
            waterAmount = extractWaterAmount(line);
            if(waterAmount < 0 || waterAmount > 1000000){
                System.out.println("Error in water amount format");
                errorsCount++;
            }

            // Proceed to reading animals and plants section
            line = reader.readLine();
            switch (line.toLowerCase()){
                case "animals:": // Parse animals first, followed by plants
                    plantOrAnimalNumber = 0;
                    while ((line = reader.readLine()) != null && !line.equalsIgnoreCase("plants:")) {
                        plantOrAnimalNumber++;
                        List<Animal> animal = extractAnimal(line.toLowerCase(), plantOrAnimalNumber);
                        if (animal != null) {
                            animals.addAll(animal);
                        } else {
                            errorsCount++;
                        }
                    }

                    // Proceed to reading animals and plants section
                    if(line == null && errorsCount == 0) return new Ecosystem(temperature, humidity, waterAmount, animals, plants);
                    plantOrAnimalNumber = 0;
                    while ((line = reader.readLine()) != null){
                        plantOrAnimalNumber++;
                        List<Plant> plant = extractPlant(line.toLowerCase(), plantOrAnimalNumber);
                        if (plant != null) {
                            plants.addAll(plant);
                        } else{
                            errorsCount++;
                        }
                    }
                    if(errorsCount == 0){
                        return new Ecosystem(temperature, humidity, waterAmount, animals, plants);
                    }else{
                        System.out.println("Please correct the errors above and reload the file.");
                        return null;
                    }


                case "plants:": // Parse plants first, followed by animals
                    plantOrAnimalNumber = 0;
                    while ((line = reader.readLine()) != null && !line.equalsIgnoreCase("animals:")) {
                        plantOrAnimalNumber++;
                        List<Plant> plant = extractPlant(line.toLowerCase(), plantOrAnimalNumber);
                        if (plant != null) {
                            plants.addAll(plant);
                        } else{
                            errorsCount++;
                        }
                    }

                    // Parse animals if plants section was successful
                    if(line == null && errorsCount == 0) return new Ecosystem(temperature, humidity, waterAmount, animals, plants);
                    while ((line = reader.readLine()) != null){
                        plantOrAnimalNumber++;
                        List<Animal> animal = extractAnimal(line.toLowerCase(), plantOrAnimalNumber);
                        if (animal != null) {
                            animals.addAll(animal);
                        } else{
                            errorsCount++;
                        }
                    }
                    if(errorsCount == 0){
                        return new Ecosystem(temperature, humidity, waterAmount, animals, plants);
                    }else{
                        System.out.println("Please correct the errors above and reload the file.");
                        return null;
                    }
                default: return null;

            }
        } catch (IOException e) {
            System.out.println("Error: File format is incorrect.");
            return null;
        }
    }

    /**
     * Extracts the temperature value from a given line of text based on a predefined pattern.
     *
     * @param line The line containing temperature data.
     * @return The extracted temperature as an integer.
     */
    public static int extractTemperature(String line){
        line = line.toLowerCase();
        String temperaturePattern = "temperature:\\s*([^,]+)";
        return extractIntData(line, temperaturePattern);
    }

    /**
     * Extracts the humidity value from a given line of text based on a predefined pattern.
     *
     * @param line The line containing humidity data.
     * @return The extracted humidity as an integer.
     */
    public static int extractHumidity(String line){
        line = line.toLowerCase();
        String humidityPattern = "humidity:\\s*([^,]+)";
        return extractIntData(line, humidityPattern);
    }

    /**
     * Extracts the water amount value from a given line of text based on a predefined pattern.
     *
     * @param line The line containing water amount data.
     * @return The extracted water amount as an integer.
     */
    public static int extractWaterAmount(String line){
        line = line.toLowerCase();
        String waterAmountPattern = "water\\s*amount:\\s*([^,]+)";
        return extractIntData(line, waterAmountPattern);
    }

    /**
     * Extracts and validates animal parameters from a line of text.
     * If parameters are valid, it creates a list of animal instances based on the specified quantity.
     *
     * @param line The input line containing animal data.
     * @param n The animal number for error reporting.
     * @return A list of Animal objects if valid, or null if any validation fails.
     */
    public static List<Animal> extractAnimal(String line, int n){

        // Define valid species types and food chain levels
        Set<String> validSpecies = new HashSet<>(Arrays.asList("herbivore", "carnivore", "omnivore"));
        Map<String, Integer> foodChainLevels = Map.of(
                "primary", 1,
                "secondary", 2,
                "tertiary", 3
        );
        List<Animal> animals = new ArrayList<>();

        // Regular expressions for each parameter in the animal data
        String namePattern = "name:\\s*([^,]+)";
        String speciesPattern = "species:\\s*([^,]+)";
        String levelPattern = "food\\s*chain\\s*level:\\s*([^,]+)";
        String energyPattern = "energy:\\s*([^,]+)\\s*";
        String lifespanPattern = "average\\s*lifespan:\\s*(\\d+)\\s*(hour|hours)\\s*";
        String agePattern = "age:\\s*(\\d+)\\s*(hour|hours)\\s*";
        String quantityPattern = "quantity:\\s*([^,]+)";

        // Extract individual data fields from the line
        String name = extractData(line, namePattern);
        String species = extractData(line, speciesPattern);
        String level = extractData(line, levelPattern);
        int energy = extractIntData(line, energyPattern);
        int lifetime = extractIntData(line, lifespanPattern);
        int age = extractIntData(line, agePattern);
        int quantity = extractIntData(line, quantityPattern);

        if(name == null){
            System.out.println("Invalid name for animal number: " + n);
            return null;
        }
        if(species == null || !validSpecies.contains(species.toLowerCase())){
            System.out.println("Invalid species for animal number: " + n);
            return null;
        }
        if(level == null || !foodChainLevels.containsKey(level.toLowerCase())){
            System.out.println("Invalid food chain level for animal number: " + n);
            return null;
        }
        if(energy < 0 || energy > 180){
            System.out.println("Invalid energy value for animal number: " + n);
            return null;
        }
        if(lifetime < 0){
            System.out.println("Invalid average lifespan for animal number: " + n);
            return null;
        }
        if(age < 0){
            System.out.println("Invalid age for animal number: " + n);
            return null;
        }
        if(quantity < 0){
            System.out.println("Invalid quantity for animal number: " + n);
            return null;
        }

        // Convert food chain level to integer
        int foodChainLevel = foodChainLevels.get(level);

        // Create the specified number of Animal instances
        for(int i = 0; i < quantity; i++){
            switch(species.toLowerCase()){
                case "herbivore" -> animals.add(new Herbivore(name, energy, foodChainLevel, lifetime, age));
                case "carnivore" -> animals.add(new Carnivore(name, energy, foodChainLevel, lifetime, age));
                case "omnivore" -> animals.add(new Omnivore(name, energy, foodChainLevel, lifetime, age));
            }
        }
        System.out.println(animals);
        return animals;
    }

    /**
     * Extracts and validates plant parameters from a line of text.
     * If parameters are valid, it creates a list of plant instances based on the specified quantity.
     *
     * @param line The input line containing plant data.
     * @param n The plant number for error reporting.
     * @return A list of Plant objects if valid, or null if any validation fails.
     */
    public static List<Plant> extractPlant(String line, int n){
        List<Plant> plants = new ArrayList<>();

        // Regular expressions for each parameter in the plant data
        String namePattern = "name:\\s*([^,]+)";
        String growthPattern = "growth\\s*level:\\s*([^,]+)";
        String waterNeedsPattern = "water\\s*needs:\\s*(\\d+)\\s*per\\s*day";
        String temperaturePattern = "optimal\\s*temperature:\\s*([^,]+)\\s*degrees";
        String quantityPattern = "quantity:\\s*([^,]+)";

        // Extract individual data fields from the line
        String name = extractData(line, namePattern);
        int growthLevel = extractIntData(line, growthPattern);
        int waterNeeds = extractIntData(line, waterNeedsPattern);
        int optimalTemperature = extractIntData(line, temperaturePattern);
        int quantity = extractIntData(line, quantityPattern);

        if(name == null){
            System.out.println("Invalid name for plant number: " + n);
            return null;
        }
        if(0 > growthLevel || growthLevel > 18){
            System.out.println("Invalid growth level for plant number: " + n);
            return null;
        }
        if(waterNeeds < 0){
            System.out.println("Invalid water needs for plant number: " + n);
            return null;
        }
        if(-30 > optimalTemperature || optimalTemperature > 38){
            System.out.println("Invalid temperature for plant number: " + n);
            return null;
        }
        if(quantity < 0){
            System.out.println("Invalid quantity for plant number: " + n);
            return null;
        }

        // Create the specified number of Plant instances
        for(int i = 0; i < quantity; i++){
            plants.add(new Plant(name, growthLevel, waterNeeds, optimalTemperature));
        }
        return plants;
    }

    /**
     * Extracts a substring from the provided input string based on the specified regex pattern.
     *
     * @param input the string from which to extract data
     * @param pattern the regex pattern used for extraction
     * @return the first captured group from the matching string if found; otherwise, returns null
     */
    public static String extractData(String input, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        if (m.find()) {
            return m.group(1).trim();  // Возвращаем первую группу
        }
        return null;
    }

    /**
     * Extracts an integer from the input string based on the specified regex pattern.
     *
     * @param input the string from which to extract an integer value
     * @param pattern the regex pattern used for extraction
     * @return the extracted integer if a match is found; otherwise, returns -1
     */
    public static int extractIntData(String input, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        if (m.find()) {
            return parseIntWithErrorHandling(m.group(1));
        }
        return -1;
    }

    /**
     * Converts a string to an integer with error handling.
     *
     * @param value the string to be converted to an integer
     * @return the parsed integer if successful; otherwise, returns -1
     */
    public static int parseIntWithErrorHandling(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}