/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package views;

import controllers.SIMULATOR;

import java.util.Scanner;

public class CLI {
    public CLI() {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the bus simulator!");
        final SIMULATOR simulator = new SIMULATOR();
        // TODO: Implement the CLI.
        do {
            final var line = scanner.nextLine();
            final String[] commands = line.split(" ");
            System.out.println("Instrução inválida.");
        } while (scanner.hasNextLine());
    }
}