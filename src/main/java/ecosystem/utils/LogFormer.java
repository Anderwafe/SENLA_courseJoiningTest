package ecosystem.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class LogFormer {

    /**
     * Writes a log entry to the log file.
     * This method appends the provided message to the log file.
     *
     * @param line The message to be logged.
     */
    public static void writeLogFile(String line){

        // Attempt to open the log file in append mode
        try(FileWriter writer = new FileWriter(Paths.get(System.getProperty("user.dir"), "log.txt")
                .toString(), true)){
            writer.write(line +"\n");
        }catch (IOException e){
            System.out.println("Error writing to log: " + e.getMessage());
        }
    }

    /**
     * Clears the contents of the log file.
     * This method overwrites the log file with an empty file.
     */
    public static void cleanLogFile(){

        // Attempt to open the log file in overwrite mode
        try(FileWriter writer = new FileWriter(Paths.get(System.getProperty("user.dir"), "log.txt")
                .toString(), false)){
            writer.write("");
        }catch (IOException e){
            System.out.println("Error clearing log: " + e.getMessage());
        }
    }
}
