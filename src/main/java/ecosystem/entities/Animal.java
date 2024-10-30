package ecosystem.entities;

import ecosystem.Ecosystem;

public abstract class Animal {
    private final String name;
    protected int energy;
    protected int foodChainLevel;
    protected int lifeTime;
    protected int currentLifeTime;
    protected boolean isEaten;

    public Animal(String name, int energy, int foodChainLevel, int lifeTime, int currentLifeTime) {
        this.name = name;
        this.energy = energy;
        this.foodChainLevel = foodChainLevel;
        this.lifeTime = lifeTime;
        this.currentLifeTime = currentLifeTime;
        isEaten = false;
    }

    public String getName() {
        return name;
    }

    public int getEnergy() {
        return energy;
    }

    public int getFoodChainLevel() {
        return foodChainLevel;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public int getCurrentLifeTime() {
        return currentLifeTime;
    }

    public void setCurrentLifeTime(int currentLifeTime) {
        this.currentLifeTime = currentLifeTime;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    /**
     * Determines the logic of animal behavior
     *
     * @param ecosystem The ecosystem in which the animal operates and interacts with other entities.
     */
    public abstract void act(Ecosystem ecosystem);

    /**
     * Initiates the reproduction process for the animal within the ecosystem.
     *
     * @param ecosystem The ecosystem in which the animal resides and where conditions for reproduction are evaluated.
     * @return A new instance of the animal if reproduction is successful; `null` if reproduction does not occur.
     */
    public abstract Animal reproduce(Ecosystem ecosystem);

}