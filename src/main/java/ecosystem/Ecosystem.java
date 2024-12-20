package ecosystem;

import ecosystem.entities.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Ecosystem {
    private final SecureRandom secureRandom = new SecureRandom();

    private List<Animal> animals;
    private List<Plant> plants;
    private int temperature;
    private int humidity;
    private int waterAmount;

    public Ecosystem(int temperature, int humidity, int waterAmount, List<Animal> animals, List<Plant> plants) {
        this.animals = animals;
        this.plants = plants;
        this.temperature = temperature;
        this.humidity = humidity;
        this.waterAmount = waterAmount;
    }


    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getWaterAmount() {
        return waterAmount;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }


    public void sortAnimalsByAge(List<Animal> animals) {
        this.animals = animals.stream()
                .sorted(Comparator.comparingInt(Animal::getLifeTime))
                .collect(Collectors.toList());
    }

    public void sortPlantsByWaterNeeds(List<Plant> plants) {
        this.plants = plants.stream()
                .sorted(Comparator.comparingInt(Plant::getWaterNeeds))
                .collect(Collectors.toList());
    }

    public void sortPlantsByGrowthLevel(List<Plant> plants) {
        this.plants = plants.stream()
                .sorted(Comparator.comparingInt(Plant::getGrowthLevel))
                .collect(Collectors.toList());
    }

    /**
     * Method to simulate the effect of calling rain.
     * This method increases humidity and adds a random amount of water to the ecosystem.
     */
    public void callRain(){
        humidity += (secureRandom.nextInt(3));
        waterAmount += 50000 + (secureRandom.nextInt(50000));
        climateIndicatorsLimitation();
    }

    /**
     * Method to simulate the effect of calling a sunny day.
     * This method increases temperature and reduces water amount by a random amount.
     */
    public void callSunnyDay(){
        temperature += (secureRandom.nextInt(2));
        waterAmount -= (secureRandom.nextInt(50000));
        climateIndicatorsLimitation();
    }

    /**
     * Method to simulate the effect of calling a cloudy day.
     * This method decreases temperature, reduces humidity, and increases water amount.
     */
    public void callCloudyDay(){
        temperature -= (secureRandom.nextInt(2));
        humidity -= (secureRandom.nextInt(3));
        waterAmount += (secureRandom.nextInt(20000));
        climateIndicatorsLimitation();
    }

    /**
     * Private method to ensure climate indicators remain within defined limits.
     * This method restricts temperature, humidity, and water amount to specific ranges.
     */
    private void climateIndicatorsLimitation(){
        temperature = Math.max(-30, temperature);
        temperature = Math.min(38, temperature);
        humidity = Math.max(0, humidity);
        humidity = Math.min(100, humidity);
        waterAmount = Math.max(0, waterAmount);
        waterAmount = Math.min(1000000, waterAmount);
    }

    /**
     * Method to simulate the actions of all animals in the ecosystem.
     * This includes reproducing and acting for each animal.
     *
     * @param ecosystem The ecosystem where the animals live.
     */
    public void actAllAnimals(Ecosystem ecosystem){
        // Create a list to store animals that are reproduced during this cycle
        List<Animal> reproducedAnimals = new ArrayList<>();

        // Remove animals that have been eaten from the list of active animals
        animals.removeIf(Animal::isEaten);

        // Iterate through each animal to allow for reproduction and actions
        for (Animal animal : getAnimals()){

            // Attempt to reproduce the animal; if successful, add the new animal to the reproduced list
            Animal reproducedAnimal = animal.reproduce(ecosystem);
            if (reproducedAnimal != null){
                reproducedAnimals.add(reproducedAnimal);
            }
        }
        // Add all newly reproduced animals to the main list of animals in the ecosystem
        animals.addAll(reproducedAnimals);

        // Allow each animal to act within the ecosystem
        for (Animal animal : animals){
            animal.act(ecosystem);
        }

        // Remove any animals that have been eaten after they have acted
        animals.removeIf(Animal::isEaten);
    }

    /**
     * Simulates the growth cycle of all plants in the ecosystem.
     * This method allows each plant to reproduce and grow based on the current ecosystem conditions.
     *
     * @param ecosystem The ecosystem where the plants live.
     */
    public void growAllPlants(Ecosystem ecosystem){
        List<Plant> newPlants = new ArrayList<>();

        // Remove plants that have been eaten from the list of active plants
        plants.removeIf(Plant::isEaten);

        // Iterate through each plant to check for reproduction
        for (Plant plant: plants){

            // Attempt to reproduce the plant; if successful, add new plants to the list
            List<Plant> reproducedPlants = plant.reproduce();
            if(reproducedPlants != null){
                newPlants.addAll(reproducedPlants);
            }
        }

        // Add all newly reproduced plants to the main list of plants in the ecosystem
        plants.addAll(newPlants);

        // Allow each plant to grow according to the current ecosystem conditions
        for (Plant plant: plants){
            plant.grow(ecosystem);
        }
        plants.removeIf(Plant::isEaten);
    }
}
