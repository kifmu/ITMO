package util;

import java.io.IOException;
import java.util.logging.*;

public class ServerLogger {
    private static ServerLogger instance;
    private final Logger logger;
    private ConsoleHandler consoleHandler;
    private FileHandler fileHandler;
    private boolean closed = false;

    private ServerLogger() {
        logger = Logger.getLogger("Lab6Server");
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        try {
            consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);

            fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            System.err.println("Ошибка создания логгера: " + e.getMessage());
        }
    }

    public static ServerLogger getInstance() {
        if (instance == null) {
            instance = new ServerLogger();
        }
        return instance;
    }

    public void info(String msg) {
        if (!closed) {
            logger.info(msg);
        }
    }

    public void warning(String msg) {
        if (!closed) {
            logger.warning(msg);
        }
    }

    public void severe(String msg) {
        if (!closed) {
            logger.severe(msg);
        }
    }

    /**
     * Закрывает все хендлеры логгера.
     */
    public void close() {
        closed = true;
        if (fileHandler != null) {
            fileHandler.flush();
            fileHandler.close();
        }
        if (consoleHandler != null) {
            consoleHandler.close();
        }
        for (Handler loggerHandler : logger.getHandlers()) {
            try {
                loggerHandler.close();
            } catch (Exception e) {
            }
        }
    }
}