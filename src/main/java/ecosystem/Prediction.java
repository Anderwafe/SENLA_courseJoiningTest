package ecosystem;

import ecosystem.entities.*;

public class Prediction {

    /**
     * Predicts the potential changes in populations of herbivores, omnivores, carnivores, and plants within the ecosystem.
     *
     * @param ecosystem The ecosystem to analyze for population changes.
     */
    public static void predictPopulationChange(Ecosystem ecosystem) {
        System.out.println("Population Change Predictions:");

        // Initialize counters for each type of animal
        int herbivoreCount = 0, omnivoreCount = 0, carnivoreCount = 0;

        // Get the current counts of plants and animals
        int plantsCount = ecosystem.getPlants().size();
        int animalsCount = ecosystem.getAnimals().size();

        // Count the number of each type of animal in the ecosystem
        for (Animal animal : ecosystem.getAnimals()){
            switch (animal.getClass().getSimpleName()){
                case "Omnivore":
                    omnivoreCount++;
                    break;
                case "Herbivore":
                    herbivoreCount++;
                    break;
                case "Carnivore":
                    carnivoreCount++;
                    break;
            }
        }

        // Predict changes in the herbivore population
        if ((float) herbivoreCount / animalsCount > 0.5 && (herbivoreCount + omnivoreCount) > plantsCount){
            System.out.println("The herbivore population is likely to increase.");
        }else if ((float) herbivoreCount / animalsCount > 0.5 || (herbivoreCount + omnivoreCount) > plantsCount){
            System.out.println("The herbivore population is likely to remain stable.");
        }else System.out.println("The herbivore population is likely to decrease.");

        // Predict changes in the omnivore population
        if ((float) omnivoreCount / animalsCount > 0.25 && (herbivoreCount + omnivoreCount) > plantsCount){
            System.out.println("The omnivore population is likely to increase.");
        }else if ((float) omnivoreCount / animalsCount > 0.25 || (herbivoreCount + omnivoreCount) > plantsCount){
            System.out.println("The omnivore population is likely to remain stable.");
        }else System.out.println("The omnivore population is likely to decrease.");

        // Predict changes in the carnivore population
        if ((float) carnivoreCount / animalsCount > 0.25 && (float) carnivoreCount / animalsCount < 0.8){
            System.out.println("The carnivore population is likely to increase.");
        } else if ((float) carnivoreCount / animalsCount < 0.25) {
            System.out.println("The carnivore population is likely to remain stable.");
        }else System.out.println("The carnivore population is likely to decrease.");

        // Predict changes in the plant population
        if ((float) plantsCount / (herbivoreCount + omnivoreCount) > 2){
            System.out.println("The plant population is likely to increase.");
        } else if ((float) plantsCount / (herbivoreCount + omnivoreCount) < 2 && (float) plantsCount / (herbivoreCount + omnivoreCount) > 1) {
            System.out.println("The plant population is likely to remain stable.");
        }else System.out.println("The plant population is likely to decrease.");

    }
}
