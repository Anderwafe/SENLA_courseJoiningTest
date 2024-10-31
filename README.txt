# Creator: Smolenskiy Evgeniy
# Ecosystem Simulator


## Description
Ecosystem Simulator is a console-based application that allows users to create and manage an ecosystem.
Users can add animals and plants, adjust environmental parameters, and simulate the interactions within the ecosystem.


## Features
- Create and Load Ecosystems (saved ecosystems located in project_folder\savedEcosystems): Easily create a new ecosystem from scratch or load an existing one from a file.

- Dynamic Ecosystem Management: Interact with various aspects of the ecosystem, including:
    -Adding animals and plants
    -Adjusting environmental conditions such as temperature, humidity, and water availability

- Simulation Capabilities: Run simulations that model the behavior of animals, plant growth, and environmental changes over time.

- Randomized Events: Experience diverse outcomes each time you run a simulation due to the incorporation of random factors that affect:
    -Animal behavior and actions
    -Plant growth rates
    -Weather conditions

- Population Predictions: Use the prediction feature to estimate changes in population dynamics within the ecosystem.

- Log Management (log file located in project_folder\log.txt): Clear and maintain log files for tracking simulation events and outcomes.

- User-Friendly Interface: Navigate through menus with clear options for managing your ecosystem and running simulations.

- Error Handling: Receive informative messages in case of input errors or issues during loading/saving operations.


## Usage
Running the Program
To run the Ecosystem Simulator, you can use one of the provided script files depending on your operating system:
- EcosystemSimulator_Run.bat for Windows
- EcosystemSimulator_Run.sh for macOS or Linux

Once the program is running, you will be presented with a menu where you can choose to:
- Create ecosystem
- Load ecosystem
After loading or creating an ecosystem, you will be presented with a menu that allows you to interact with and manage the ecosystem.

If you want to try simulating your ecosystem, please note that the simulation incorporates random factors that affect animal behavior, plant growth, and weather changes. This means that simulations conducted under the same conditions may yield different results. As a result, animals or plants may die quickly, or conversely, reproduce excessively.

Follow the on-screen prompts to interact with the application, and be prepared for unexpected outcomes!

Recommendation: When running the simulation, I advise against setting a simulation time that is too long, as this may lead to extended waiting periods. Adjust the simulation duration according to your preferences to ensure a smoother experience.


## Additional Information
Example Ecosystem Format: In the project folder, there is an example file(Primer.txt) that demonstrates the format for loading ecosystems. This allows users to create a simulation file independently. So users can write their own ecosystem configuration files following the provided format to quickly create new simulations without going through the console interface.
