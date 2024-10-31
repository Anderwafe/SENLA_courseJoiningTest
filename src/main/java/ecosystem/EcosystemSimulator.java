package ecosystem;

import ecosystem.entities.Animal;
import ecosystem.entities.Plant;
import ecosystem.utils.*;


import java.nio.file.Paths;
import java.util.*;

public class EcosystemSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Ecosystem ecosystem = null;
        LogFormer.cleanLogFile();
        System.out.println("Welcome to the Ecosystem Simulator.");

        while (true) {
            System.out.println("1. Create a new simulation");
            System.out.println("2. Load an existing simulation");
            System.out.println("3. Exit");
            System.out.print("Please enter your choice: ");
            String choice = scanner.next();
            String filePath;

            switch (choice) {
                case "1":
                    ecosystem = ObjectsCreator.createNewEcosystem(scanner);
                    break;

                case "2":
                    System.out.print("Enter the path to the simulation file: ");
                    filePath = scanner.next();
                    ecosystem = FileManager.loadEcosystem(filePath);
                    if (ecosystem == null) {
                        System.out.println("Please correct the errors in the file and try load it again.");
                    } else {
                        System.out.println("Simulation successfully loaded.");
                    }
                    break;

                case "3":
                    System.out.println("Exiting the application.");
                    return;

                default:
                    System.out.println("Please enter a valid option.");
            }
            if(ecosystem != null) break;
        }

        while(true){
            System.out.println("Menu:");
            System.out.println("1. Work with the ecosystem");
            System.out.println("2. Save the current ecosystem");
            System.out.println("3. Create a new ecosystem");
            System.out.println("4. Load an existing ecosystem");
            System.out.println("5. Predict ecosystem development");
            System.out.println("6. Clear log file");
            System.out.println("7. Exit");
            System.out.print("Please enter your choice: ");
            String choice = scanner.next();
            switch (choice){
                case "1":
                    if(ecosystem == null){
                        System.out.println("Please load an existing ecosystem or create a new one first.");
                    }else{
                        workWithEcosystem(ecosystem, scanner);
                    }
                    break;

                case "2":
                    System.out.println("Enter a name for the ecosystem file:");
                    String name = scanner.next();
                    FileManager.saveEcosystem(ecosystem, Paths.get(System.getProperty("user.dir"), "savedEcosystems", name + ".txt").toString());
                    break;

                case "3":
                    System.out.println("If you haven't saved this ecosystem, creating a new one will lead to its loss.");
                    while (true){
                        System.out.println("1. Go back and save the ecosystem");
                        System.out.println("2. Create a new ecosystem");
                        choice = scanner.next();
                        if(choice.equals("1")) break;
                        else if (choice.equals("2")) {
                            ecosystem = ObjectsCreator.createNewEcosystem(scanner);
                            break;
                        }
                    }
                    break;

                case "4":
                    System.out.println("If you haven't saved this ecosystem, loading a new one will lead to its loss.");
                    while (true){
                        System.out.println("1. Go back and save the ecosystem");
                        System.out.println("2. Load a new ecosystem");
                        choice = scanner.next();
                        if(choice.equals("1")) break;
                        else if (choice.equals("2")) {
                            System.out.print("Enter the path to the simulation file: ");
                            String filePath = scanner.next();
                            ecosystem = FileManager.loadEcosystem(filePath);
                            if (ecosystem == null) {
                                System.out.println("Please correct the errors in the file or the file path and try loading again.");
                            } else {
                                System.out.println("Simulation successfully loaded.");
                                break;
                            }
                        }
                    }
                    break;

                case "5":
                    if (ecosystem == null){
                        System.out.println("Please load an existing ecosystem or create a new one first.");
                    }else Prediction.predictPopulationChange(ecosystem);
                    break;

                case "6":
                    LogFormer.cleanLogFile();
                    System.out.println("Log file cleared.");
                    break;

                case "7": return; // Exit the application
                default: System.out.println("Please enter a valid option.");
            }
        }
    }

    /**
     * Manages interactions with the specified ecosystem, allowing users to modify its properties
     * and perform various actions like adding animals or plants, conducting simulation cycles, etc.
     *
     * @param ecosystem the Ecosystem object to work with
     * @param scanner   the Scanner object for user input
     */
    public static void workWithEcosystem(Ecosystem ecosystem, Scanner scanner){
        Simulation simulation = new Simulation();
        while (true){
            System.out.println("Menu");
            System.out.println("1. Change temperature");
            System.out.println("2. Change humidity");
            System.out.println("3. Change amount of water");
            System.out.println("4. Add an animal");
            System.out.println("5. Add a plant");
            System.out.println("6. Conduct an action cycle for animals");
            System.out.println("7. Conduct a growth cycle for plants");
            System.out.println("8. Start the simulation");
            System.out.println("9. Return to the previous menu");
            System.out.print("Please enter your choice: ");
            String choice = scanner.next();
            switch(choice){
                case "1":
                    ecosystem.setTemperature(UserInput.getValidIntInput(
                            "Enter new temperature (from -30 to 38): ", -30, 38));
                    break;
                case "2":
                    ecosystem.setHumidity(UserInput.getValidIntInput(
                            "Enter new humidity (from 0 to 100): ", 0, 100));
                    break;
                case "3":
                    ecosystem.setWaterAmount(UserInput.getValidIntInput(
                            "Enter new available water amount (from 0 to 100000): ", 0, 100000));
                    break;
                case "4":
                    List<Animal> createdAnimals = ecosystem.getAnimals();
                    createdAnimals.addAll(ObjectsCreator.createAnimal(scanner));
                    ecosystem.setAnimals(createdAnimals);
                    break;
                case "5":
                    List<Plant> createdPlants = ecosystem.getPlants();
                    createdPlants.addAll(ObjectsCreator.createPlant(scanner));
                    ecosystem.setPlants(createdPlants);
                    break;
                case "6":
                    simulation.removeOldAnimals(ecosystem);
                    simulation.removeDeadAnimals(ecosystem);
                    ecosystem.actAllAnimals(ecosystem);
                    break;
                case "7":
                    ecosystem.growAllPlants(ecosystem);
                    break;
                case "8":
                    int simulationTime = UserInput.getValidIntInput("Enter simulation time (integer number of hours from 0 to 10000):", 0, 10000);
                    simulation.simulate(ecosystem, simulationTime);
                    break;

                case "9": return;
                default: System.out.println("Please enter a valid option.");
            }
        }
    }
}
