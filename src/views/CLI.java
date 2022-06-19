/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package views;

import controllers.SIMULATOR;

import java.util.Scanner;

public class CLI {
    public CLI() {
        final Scanner scanner = new Scanner(System.in);
        final SIMULATOR simulator = new SIMULATOR();
        while (scanner.hasNextLine()) {
            final var line = scanner.nextLine();
            final String[] commands = line.split(" ");
            switch (commands[0]) {
                case "show" -> simulator.show();
                case "stop_bus" -> simulator.stopBus(commands[1], commands[2], Integer.parseInt(commands[3]));
                case "refuel" -> simulator.refuel(commands[1], commands[2]);
                case "switch_driver" -> simulator.switchDriver(commands[1], commands[2]);
                case "stop_for_maintenance" -> simulator.stopForMaintenance(commands[1], commands[2]);
                case "exit" -> System.exit(0);
                default -> System.out.println("Invalid command.");
            }
        }
    }
}