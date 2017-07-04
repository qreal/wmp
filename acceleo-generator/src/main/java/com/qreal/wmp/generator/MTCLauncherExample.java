package com.qreal.wmp.generator;

import cs.ualberta.launcher.Launcher;
import edu.ca.ualberta.ssrg.chaintracker.acceleo.main.AcceleoLauncherException;

public class MTCLauncherExample {
    public static void main(String... args) throws AcceleoLauncherException {

        Launcher launcher = new Launcher();

        launcher.runAcceleo("metamodels/qwe1.ecore", "qwe1", "models/dsa.xmi",
                "transformations/M2T/generateRobots.mtl", "gen/");
    }
}
