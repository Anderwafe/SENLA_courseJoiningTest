package ecosystem.entities;

import ecosystem.Ecosystem;
import ecosystem.utils.LogFormer;

import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;

public class Herbivore extends Animal {
    private final SecureRandom secureRandom = new SecureRandom();

    public Herbivore(String name, int energy, int foodChainLevel, int lifeTime, int currentLifeTime) {
        super(name, energy, foodChainLevel, lifeTime, currentLifeTime);
        this.waterNeeds = switch (foodChainLevel){
            case 1 -> 10;
            case 2 -> 19;
            case 3 -> 25;
            default -> 0; // This should never happen
        };
    }

    /**
     * Executes the animal's daily actions within the ecosystem, including aging, energy expenditure,
     * and eating plants if energy levels are low.
     *
     * @param ecosystem The ecosystem in which the animal acts, providing access to other animals
     *                  for potential hunting.
     */
    @Override
    public void act(Ecosystem ecosystem) {
        setCurrentLifeTime(getCurrentLifeTime() + 24);

        if(ecosystem.getWaterAmount() < waterNeeds){
            energy -= 30;
        }else ecosystem.setWaterAmount(ecosystem.getWaterAmount() - waterNeeds);

        if(energy < 100){
            LogFormer.writeLogFile(getName() + " исследует территорию в поисках пищи.");
            eatPlant(ecosystem);
        }else energy -= 5;

    }

    /**
     * Attempts to eat a plant from the ecosystem. The method checks for available plants and selects one to consume.
     *
     * @param ecosystem The ecosystem from which plants can be eaten, providing access to available plants.
     */
    private void eatPlant(Ecosystem ecosystem) {

        // Check if there are any available plants
        if (ecosystem.getPlants().isEmpty()) {
            LogFormer.writeLogFile(getName() + " не может найти растения для питания.");
            energy -= 5;
            return;
        }

        // Count the number of uneaten plants
        long countCanBeEaten = ecosystem.getPlants().stream().filter(plant -> !plant.isEaten()).count();
        if(countCanBeEaten == 0) {
            LogFormer.writeLogFile(getName() + " не может найти растения для питания.");
            energy -= 5;
            return;
        }

        // Randomly select a plant to eat
        int plantNumber = secureRandom.nextInt(ecosystem.getPlants().size());

        // Ensure the selected plant is valid (not already eaten)
        while (ecosystem.getPlants().get(plantNumber).isEaten()){
            plantNumber = secureRandom.nextInt(ecosystem.getPlants().size());
        }

        // Log the plant being eaten and update its state
        LogFormer.writeLogFile(getName() + " питается растением: " + ecosystem.getPlants().get(plantNumber).getName());
        ecosystem.getPlants().get(plantNumber).setEaten(true);
        energy += 60;
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
        if (energy >= 100 && currentLifeTime >= 1000 && secureRandom.nextDouble() > getReproduceChance(foodChainLevel)) {

            // Count the number of existing animals of the same species
            long countSameSpecies = animals.stream()
                    .filter(animal -> Objects.equals(animal.getName(), getName()))
                    .count();

            // If there are at least two of the same species, proceed to reproduce
            if (countSameSpecies >= 2) {
                int energyCost = getEnergyCost(getFoodChainLevel());
                energy -= energyCost;
                return new Herbivore(getName(), 60, getFoodChainLevel(), getLifeTime(), 0);
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
            case 1 -> 0.4;
            case 2 -> 0.55;
            case 3 -> 0.65;
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
            case 1 -> 10;
            case 2 -> 30;
            case 3 -> 40;
            default -> 0; // No chance for invalid levels
        };
    }

}