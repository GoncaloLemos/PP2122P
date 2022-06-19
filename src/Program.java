/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final SIMULATOR simulator = new SIMULATOR();
        while (scanner.hasNextLine()) {
            final var line = scanner.nextLine();
            final String[] commands = line.split(" ");
            switch (commands[0]) {
                case "stop_bus":
                    simulator.STOP_BUS_FOR(commands[1], commands[2], Integer.parseInt(commands[3]));
                    break;
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }
}