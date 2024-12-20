package ecosystem.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LogFormer {

    private static final Logger logger = LogManager.getLogger(LogFormer.class);

    private static final Path LOG_FILE_PATH = Paths.get(System.getProperty("user.dir"), "log.txt");

    /**
     * Writes a log entry to the log file.
     * This method appends the provided message to the log file.
     *
     * @param line The message to be logged.
     */
    public static void writeLogFile(String line) {
        logger.info(line);
    }

    /**
     * Clears the contents of the log file.
     * This method overwrites the log file with an empty file.
     */
    public static void cleanLogFile() {
        try {
            Files.write(LOG_FILE_PATH, new byte[0]);
            logger.info("Log file cleared.");
        } catch (IOException e) {
            logger.error("Error clearing log file: {}", e.getMessage(), e);
        }
    }
}
