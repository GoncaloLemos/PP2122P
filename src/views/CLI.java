/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package views;

import controllers.Simulator;

import java.util.Scanner;

public class CLI {
    public CLI() {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the bus simulator!");
        final Simulator simulator = new Simulator();
        // TODO: Implement the CLI.
        do {
//            final var line = scanner.nextLine();
//            final String[] commands = line.split(" ");
//            switch (commands[0]) {
//                default -> System.out.println("Instrução inválida.");
//            }
        } while (scanner.hasNextLine());
    }
}