package ecosystem.entities;

import ecosystem.Ecosystem;
import ecosystem.utils.LogFormer;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Plant {
    private final String name;
    private int growthLevel; // Текущий уровень роста растения
    private final int waterNeeds;  // Необходимое количество воды для роста
    private final int optimalTemperature; // Оптимальная температура для роста
    private boolean isEaten = false;

    public Plant(String name, int growthLevel, int waterNeeds, int optimalTemperature) {
        this.name = name;
        this.growthLevel = growthLevel;
        this.waterNeeds = waterNeeds;
        this.optimalTemperature = optimalTemperature;
    }

    public String getName() {
        return name;
    }

    public int getGrowthLevel() {
        return growthLevel;
    }

    public int getWaterNeeds() {
        return waterNeeds;
    }

    public int getOptimalTemperature() {
        return optimalTemperature;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    /**
     * Simulates the growth process of the plant within the ecosystem based on environmental conditions.
     * The method checks if the plant has been eaten, assesses the available water and temperature conditions,
     * and updates the growth level accordingly. It also logs the growth attempts and outcomes.
     *
     * @param ecosystem The ecosystem in which the plant resides, providing environmental conditions such as water amount and temperature.
     */
    public void grow(Ecosystem ecosystem) {
        if(isEaten) return;

        LogFormer.writeLogFile(name + " attempts to grow...");

        // Check if there is enough water available for growth
        if (ecosystem.getWaterAmount() >= waterNeeds) {
            if (Math.abs(ecosystem.getTemperature() - optimalTemperature) <= 5) {
                // Favorable conditions - the plant grows faster
                ecosystem.setWaterAmount(ecosystem.getWaterAmount() - waterNeeds);
                growthLevel += 2;
                LogFormer.writeLogFile(name + " grows faster due to favorable conditions.");
            } else if (Math.abs(ecosystem.getTemperature() - optimalTemperature) <= 10) {
                // Conditions are not ideal, but the plant can still grow
                ecosystem.setWaterAmount(ecosystem.getWaterAmount() - waterNeeds);
                growthLevel += 1;
                LogFormer.writeLogFile(name + " grows slower due to temperature.");
            } else {
                // Temperature is too unfavorable - the plant does not grow
                ecosystem.setWaterAmount(ecosystem.getWaterAmount() - waterNeeds);
                LogFormer.writeLogFile(name + " does not grow due to unfavorable temperature.");
            }
        } else {
            growthLevel -= 1;
            LogFormer.writeLogFile(name + "wilting due to lack of water.");
        }

    }

    /**
     * Simulates the reproduction process of the plant.
     * If the plant meets the growth requirements, it will reproduce and create new plants.
     *
     * @return A list of new plants created from reproduction, or null if the reproduction conditions are not met.
     */
    public List<Plant> reproduce()  {

        // Check if the plant can reproduce (growth level requirement and not eaten)
        if(growthLevel >= 18 && !isEaten()){
            SecureRandom secureRandom = new SecureRandom();
            setEaten(true);
            List<Plant> newPlants = new ArrayList<>();
            LogFormer.writeLogFile("The plant wilts and leaves seeds.");

            // Generate a random number of new plants (1 to 3)
            for(int i = 0; i <= 1 + secureRandom.nextInt(3); i++){
                newPlants.add(new Plant(getName(), 0, getWaterNeeds(), getOptimalTemperature()));
            }
            return newPlants;
        }else return null;
    }
}
