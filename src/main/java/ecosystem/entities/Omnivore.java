package ecosystem.entities;

import ecosystem.Ecosystem;
import ecosystem.utils.LogFormer;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

public class Omnivore extends Animal {

    public Omnivore(String name, int energy, int foodChainLevel, int lifeTime, int currentLifeTime) {
        super(name, energy, foodChainLevel, lifeTime, currentLifeTime);
        this.waterNeeds = switch (foodChainLevel){
            case 1,2 -> 4;
            case 3 -> 6;
            default -> 0; // This should never happen
        };
    }

    /**
     * Executes the animal's daily actions within the ecosystem, including aging, energy expenditure,
     * search for water, and hunting or eating plants if energy levels are low.
     *
     * @param ecosystem The ecosystem in which the animal acts, providing access to other animals
     *                  for potential hunting.
     */
    @Override
    public void act(Ecosystem ecosystem) {
        SecureRandom secureRandom = new SecureRandom();

        setCurrentLifeTime(getCurrentLifeTime() + 24);

        if(ecosystem.getWaterAmount() < waterNeeds){
            LogFormer.writeLogFile(getName() + " cannot find water.");
            energy -= 10;
        }else ecosystem.setWaterAmount(ecosystem.getWaterAmount() - waterNeeds);

        double probability = switch (foodChainLevel) {
            case 1 -> 0.3;
            case 2 -> 0.25;
            case 3 -> 0.2   ;
            default -> 0.5; // Default case for safety
        };
        if(energy < 100){
            LogFormer.writeLogFile(getName() + " explores the area in search of food.");
            if (secureRandom.nextDouble() > probability) {
                eatPlant(ecosystem);
            } else {
                huntAnimals(ecosystem);
            }
        }else energy -= 5;



    }

    /**
     * Attempts to hunt animals within the ecosystem based on the animal's food chain level.
     *
     * @param ecosystem The ecosystem containing animals to hunt.
     */
    private void huntAnimals(Ecosystem ecosystem) {
        SecureRandom secureRandom = new SecureRandom();
        // Count the number of potential prey that can be hunted
        long countCanBeHunted = ecosystem.getAnimals().stream()
                .filter(animal -> !Objects.equals(animal.getName(), getName()) && animal.getFoodChainLevel() <= foodChainLevel && !animal.isEaten())
                .count();

        // If no prey is available, log a message and reduce energy
        if (countCanBeHunted == 0) {
            LogFormer.writeLogFile("The hunt was unsuccessful (no prey available).");
            energy -= 15;
            return;
        }

        // Randomly select a target for hunting
        int animalNumber = secureRandom.nextInt(ecosystem.getAnimals().size());

        // Ensure the selected target is valid (not self, within food chain level, and not already eaten)
        while (Objects.equals(ecosystem.getAnimals().get(animalNumber).getName(), getName()) ||
                ecosystem.getAnimals().get(animalNumber).getFoodChainLevel() > foodChainLevel) {
            animalNumber = secureRandom.nextInt(ecosystem.getAnimals().size());
        }

        // Attempt to hunt the selected target, and log the result
        int energyGain = getEnergyGain(foodChainLevel);
        if (secureRandom.nextDouble() > 0.2) { // 80% success rate for hunting
            LogFormer.writeLogFile(getName() + " successfully hunts for " + ecosystem.getAnimals().get(animalNumber).getName());
            ecosystem.getAnimals().get(animalNumber).setEaten(true);
            energy += energyGain;

        } else {
            LogFormer.writeLogFile("The hunt was unsuccessful (the prey escaped).");
            energy -= 10;
        }
    }


    /**
     * Attempts to eat a plant from the ecosystem. The method checks for available plants and selects one to consume.
     *
     * @param ecosystem The ecosystem from which plants can be eaten, providing access to available plants.
     */
    private void eatPlant(Ecosystem ecosystem) {
        SecureRandom secureRandom = new SecureRandom();

        // Check if there are any available plants
        if (ecosystem.getPlants().isEmpty()) {
            LogFormer.writeLogFile(getName() + " cannot find plants for food.");
            energy -= 10; // Уменьшаем энергию при отсутствии растений
            return;
        }

        // Count the number of uneaten plants
        long countCanBeEaten = ecosystem.getPlants().stream().filter(plant -> !plant.isEaten()).count();
        if(countCanBeEaten == 0) {
            LogFormer.writeLogFile(getName() + " cannot find plants for food.");
            energy -= 10; // Уменьшаем энергию при отсутствии растений
            return;
        }

        // Randomly select a plant to eat
        int plantNumber = secureRandom.nextInt(ecosystem.getPlants().size());

        // Ensure the selected plant is valid (not already eaten)
        while (ecosystem.getPlants().get(plantNumber).isEaten()){
            plantNumber = secureRandom.nextInt(ecosystem.getPlants().size());
        }

        // Log the plant being eaten and update its state
        LogFormer.writeLogFile(getName() + " feeds on the plant " + ecosystem.getPlants().get(plantNumber).getName());
        ecosystem.getPlants().get(plantNumber).setEaten(true);
        energy += 50;
    }

    /**
     * Attempts to reproduce a new animal of the same species within the ecosystem.
     * The method checks if the conditions for reproduction are met, including sufficient energy,
     * lifetime criteria, chance of reproducing based on food chain level, and the presence of the same species.
     *
     * @param ecosystem The ecosystem in which the reproduction occurs, providing access to existing animals.
     * @return A new Animal instance of the same species if reproduction is successful; otherwise, null.
     */
    public Animal reproduce(Ecosystem ecosystem) {
        final SecureRandom secureRandom = new SecureRandom();
        List<Animal> animals = ecosystem.getAnimals();

        // Check if the animal has enough energy, has lived long enough, is not eaten,
        // and meets the chance of reproducing based on food chain level
        if(energy >= 100 && currentLifeTime >= 1000  && secureRandom.nextDouble() > getReproduceChance(foodChainLevel)){

            // Count the number of existing animals of the same species
            long countSameSpecies = animals.stream()
                    .filter(animal -> Objects.equals(animal.getName(), getName()))
                    .count();

            // If there are at least two of the same species, proceed to reproduce
            if (countSameSpecies >= 2) {
                int energyCost = getEnergyCost(getFoodChainLevel());
                energy -= energyCost; // Уменьшаем энергию на 40
                return new Omnivore(getName(), 60, getFoodChainLevel(), getLifeTime(), 0);
            }
        }
        return null;
    }

    /**
     * Returns the chance of reproduction based on the food chain level of the animal.
     *
     * @param foodChainLevel The food chain level of the animal, determining its reproductive success rate.
     * @return A double value representing the chance of reproduction as a percentage.
     */
    private double getReproduceChance(int foodChainLevel){
        return switch (foodChainLevel){
            case 1 -> 0.6;
            case 2 -> 0.8;
            case 3 -> 0.9;
            default -> 0; // No chance for invalid levels
        };
    }

    /**
     * Calculates the energy cost required for reproduction based on the food chain level of the animal.
     *
     * @param foodChainLevel The food chain level of the animal, which determines the energy cost for reproduction.
     * @return An integer representing the energy cost for reproduction.
     */
    private int getEnergyCost(int foodChainLevel) {
        return switch (foodChainLevel) {
            case 1 -> 50;
            case 2 -> 55;
            case 3 -> 60;
            default -> 0; // No chance for invalid levels
        };
    }

    /**
     * Calculates the energy gain obtained from hunting based on the food chain level of the animal.
     *
     * @param foodChainLevel The food chain level of the animal, which determines the energy gain from food.
     * @return An integer representing the energy gain from food consumption.
     */
    private int getEnergyGain(int foodChainLevel) {
        return switch (foodChainLevel) {
            case 1 -> 35;
            case 2, 3 -> 45;
            default -> 0;
        };
    }

}