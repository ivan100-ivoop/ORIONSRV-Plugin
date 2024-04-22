package com.github.ivan100ivoop.orionsrv;

import com.github.ivan100ivoop.orionsrv.Tasks.Porches;
import com.github.ivan100ivoop.orionsrv.utils.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public static Logger logger = Logger.getLogger("ORIONSRV");

    public static TaskManager taskManager;
    public static Main instance;
    @Override
    public void onEnable() {
        instance = this;
        taskManager = new TaskManager();
        taskManager.runSeconds = 5;
        taskManager.start();
        logger.info("Enabled!");
        registerTasks();
    }

    private void registerTasks() {
        TaskManager.addTask(new Porches());
    }

    @Override
    public void onDisable() {
        taskManager.stopAll();
        logger.info("Disabled!");
    }
}
