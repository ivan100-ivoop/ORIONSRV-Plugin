package com.github.ivan100ivoop.orionsrv.Tasks;

import com.github.ivan100ivoop.orionsrv.Main;
import com.github.ivan100ivoop.orionsrv.utils.Requests;
import com.github.ivan100ivoop.orionsrv.utils.Task;

public class Porches extends Task {

    @Override
    public void onStart() {
        Main.logger.info("Task Registered!");
    }

    @Override
    public void onUpdate() {
        String content = Requests.get("https://www.google.com", null);
        Main.logger.info(content);
    }

    @Override
    public void onEnd() {
        Main.logger.info("Task Unregistered!");
    }
}
