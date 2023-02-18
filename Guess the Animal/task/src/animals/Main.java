package animals;

import animals.controller.AppLogic;

public class Main {
    public static void main(String[] args) {
        AppLogic appLogic = new AppLogic(args);
        appLogic.mainWorkflow();
    }
}
