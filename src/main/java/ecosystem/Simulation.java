package ecosystem;

import java.security.SecureRandom;
import java.util.List;
import ecosystem.entities.*;

public class Simulation {

    /**
     * Method to simulate the ecosystem over a specified period.
     * This method handles the growth of plants, the actions of animals,
     * and the effects of weather on the ecosystem.
     *
     * @param ecosystem The ecosystem to be simulated.
     * @param simulationTime The duration of the simulation, in hours.
     */
    public void simulate(Ecosystem ecosystem, int simulationTime) {
        SecureRandom secureRandom = new SecureRandom();
        System.out.println("Starting simulation...");
        System.out.println("Current temperature: " + ecosystem.getTemperature());
        System.out.println("Current humidity: " + ecosystem.getHumidity());
        System.out.println("Available water: " + ecosystem.getWaterAmount());
        System.out.println("Please wait for the simulation to complete.");

        // Loop through the specified simulation time
        for(int i = 0; i <= simulationTime; i++){

            // Every 24 hours, simulate actions and growth
            if(i % 24 == 0){

                ecosystem.growAllPlants(ecosystem);
                removeMinusGrowthPlants(ecosystem);

                ecosystem.actAllAnimals(ecosystem);
                removeDeadAnimals(ecosystem);
                removeOldAnimals(ecosystem);

                // Randomly determine weather effects (rain, sunny day, or cloudy day)
                switch(secureRandom.nextInt(3)){
                    case 0:
                        ecosystem.callRain();
                        break;
                    case 1:
                        ecosystem.callSunnyDay();
                        break;
                    case 2:
                        ecosystem.callCloudyDay();
                        break;
                }
            }
        }
        System.out.println("Simulation completed.");
    }

    /**
     * Removes animals that have died from starvation from the ecosystem.
     *
     * @param ecosystem The ecosystem from which to remove dead animals.
     */
    public void removeDeadAnimals(Ecosystem ecosystem){
        List<Animal> animals = ecosystem.getAnimals();
        animals.removeIf(animal -> animal.getEnergy() < 0);
        ecosystem.setAnimals(animals);
    }

    /**
     * Removes aging animals that have exceeded their lifespan from the ecosystem.
     *
     * @param ecosystem The ecosystem from which to remove old animals.
     */
    public void removeOldAnimals(Ecosystem ecosystem){
        List<Animal> animals = ecosystem.getAnimals();
        animals.removeIf(animal -> animal.getLifeTime() < animal.getCurrentLifeTime());
        ecosystem.setAnimals(animals);
    }

    /**
     * Removes plants that have negative growth levels from the ecosystem.
     *
     * @param ecosystem The ecosystem from which to remove plants with negative growth.
     */
    public void removeMinusGrowthPlants(Ecosystem ecosystem){
        List<Plant> plants = ecosystem.getPlants();
        plants.removeIf(plant -> plant.getGrowthLevel() < 0);
        ecosystem.setPlants(plants);
    }

}
