package com.github.ivan100ivoop.orionsrv.utils;

import com.github.ivan100ivoop.orionsrv.Main;
import com.github.ivan100ivoop.orionsrv.utils.Task;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskManager extends BukkitRunnable {
    public static List<Task> tasks = new ArrayList<>();
    public int runSeconds = 1;
    public static boolean addTask(Task task){
        if (hasTask(task)) return false;
        tasks.add(task);
        task.onStart();
        return true;
    }

    public static boolean removeTask(Task task){
        if(!hasTask(task)) return false;
        tasks.remove(task);
        task.onEnd();
        return true;
    }

    public static boolean hasTask(Task task){
        return tasks.contains(task);
    }

    public void stopAll(){
        Iterator<Task> _tasks = tasks.iterator();
        while (_tasks.hasNext()){
            Task task = _tasks.next();
            task.onEnd();
            _tasks.remove();
        }
        if(!this.isCancelled()){
            this.cancel();
        }
    }

    public void start(){
        this.runTaskTimer(Main.instance, 0L, runSeconds * 20L);
    }

    @Override
    public void run() {
        Iterator<Task> _tasks = tasks.iterator();
        while (_tasks.hasNext()){
            Task task = _tasks.next();
            task.onUpdate();
        }
    }
}
