/*
 * Copyright © 2022, Pedro Simões & Gonçalo Lemos. All rights reserved.
 */

package controllers;

import models.BUS;
import models.BUS_STOP;
import models.BUS_TYPE;
import models.MALFUNCTION_TYPE;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SIMULATOR {
    public SIMULATOR() {
        JSONObject JSON = null; // JSON object that holds the parsed JSON file.
        try {
            JSON = (JSONObject) new JSONParser().parse(new FileReader("./src/config.json")); // Parses the JSON file.
        } catch (IOException e) { // Catches IOException. If the file is not found, it will throw an error.
            System.err.println("""
                    ERROR: Could not read config.json file.
                    Please make sure it is in the same directory as the program."""); // Prints error message.
            System.exit(1); // Exits the program, if the file is not found.
        } catch (
                ParseException e) { // Catches ParseException. If the file is not valid JSON, it will throw an error, or if the JSON file is not well-formed.
            System.err.println("""
                    ERROR: Could not parse config.json file.
                    Please make sure it is in the same directory as the program."""); // Prints the error message.
            System.exit(1); // Exits the program, if the file is not valid JSON.
        }
        final int MINIMUM_FLEET_SIZE = Integer.parseInt(JSON.get("MINIMUM_FLEET_SIZE").toString()); // Gets the minimum fleet size from the JSON file.
        final int MAXIMUM_FLEET_SIZE = Integer.parseInt(JSON.get("MAXIMUM_FLEET_SIZE").toString()); // Gets the maximum fleet size from the JSON file.
        final int NUMBER_OF_MINI_BUS = Integer.parseInt(JSON.get("NUMBER_OF_MINI_BUS").toString()); // Gets the number of mini buses from the JSON file.
        final int NUMBER_OF_CONVENCIONAL = Integer.parseInt(JSON.get("NUMBER_OF_CONVENCIONAL").toString()); // Gets the number of conventional buses from the JSON file.
        final int NUMBER_OF_LONG_DRIVE = Integer.parseInt(JSON.get("NUMBER_OF_LONG_DRIVE").toString()); // Gets the number of long-drive buses from the JSON file.
        final int NUMBER_OF_EXPRESS = Integer.parseInt(JSON.get("NUMBER_OF_EXPRESS").toString()); // Gets the number of express buses from the JSON file.
        if (MINIMUM_FLEET_SIZE > MAXIMUM_FLEET_SIZE) { // Checks if the minimum fleet size is greater than the maximum fleet size.
            System.err.println("""
                    ERROR: MINIMUM_FLEET_SIZE must be less than or equal to MAXIMUM_FLEET_SIZE."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the minimum fleet size is greater than the maximum fleet size.
        }
        if (NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESS > MAXIMUM_FLEET_SIZE) { // Checks if the total number of buses is greater than the maximum fleet size.
            System.err.println("""
                    ERROR: The sum of the number of buses of each type must be less than or equal to MAXIMUM_FLEET_SIZE."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the total number of buses is greater than the maximum fleet size.
        }
        if (NUMBER_OF_MINI_BUS < 1 || NUMBER_OF_CONVENCIONAL < 1 || NUMBER_OF_LONG_DRIVE < 1 || NUMBER_OF_EXPRESS < 1) { // Checks if the number of buses of each type is less than 1.
            System.err.println("""
                    ERROR: The number of buses of each type must be greater than or equal to 1."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the number of buses of each type is less than 1.
        }
        if (NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESS < MINIMUM_FLEET_SIZE) { // Checks if the total number of buses is less than the minimum fleet size.
            System.err.println("""
                    ERROR: The sum of the number of buses of each type must be greater than or equal to MINIMUM_FLEET_SIZE."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the total number of buses is less than the minimum fleet size.
        }
        final int MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString()); // Gets the minimum delay for a malfunction to execute from the JSON file.
        final int MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString()); // Gets the maximum delay for a malfunction to execute from the JSON file.
        if (MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION < 0) { // Checks if the minimum delay for a malfunction to execute is less than 0.
            System.err.println("""
                    ERROR: MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION must be greater than or equal to 0."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the minimum delay for a malfunction to execute is less than 0.
        }
        if (MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION < 0) { // Checks if the maximum delay for a malfunction to execute is less than 0.
            System.err.println("""
                    ERROR: MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION must be greater than or equal to 0."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the maximum delay for a malfunction to execute is less than 0.
        }
        if (MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION < MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION) { // Checks if the maximum delay for a malfunction to execute is less than the minimum delay for a malfunction to execute.
            System.err.println("""
                    ERROR: MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION must be greater than or equal to MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the maximum delay for a malfunction to execute is less than the minimum delay for a malfunction to execute.
        }
        final int MAX_CAPACITY_MINI_BUS = Integer.parseInt(JSON.get("MAX_CAPACITY_MINI_BUS").toString()); // Gets the maximum capacity of a mini bus from the JSON file.
        final int MAX_CAPACITY_CONVENCIONAL = Integer.parseInt(JSON.get("MAX_CAPACITY_CONVENCIONAL").toString()); // Gets the maximum capacity of a conventional bus from the JSON file.
        final int MAX_CAPACITY_LONG_DRIVE = Integer.parseInt(JSON.get("MAX_CAPACITY_LONG_DRIVE").toString()); // Gets the maximum capacity of a long-drive bus from the JSON file.
        final int MAX_CAPACITY_EXPRESS = Integer.parseInt(JSON.get("MAX_CAPACITY_EXPRESS").toString()); // Gets the maximum capacity of an express bus from the JSON file.
        if (MAX_CAPACITY_MINI_BUS < 1 || MAX_CAPACITY_CONVENCIONAL < 1 || MAX_CAPACITY_LONG_DRIVE < 1 || MAX_CAPACITY_EXPRESS < 1) { // Checks if the maximum capacity of a bus is less than 1.
            System.err.println("""
                    ERROR: The maximum capacity of a bus must be greater than or equal to 1."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the maximum capacity of a bus is less than 1.
        }
        final int BASE_SPEED_MINI_BUS = Integer.parseInt(JSON.get("BASE_SPEED_MINI_BUS").toString()); // Gets the base speed of a mini bus from the JSON file.
        final int BASE_SPEED_CONVENCIONAL = Integer.parseInt(JSON.get("BASE_SPEED_CONVENCIONAL").toString()); // Gets the base speed of a conventional bus from the JSON file.
        final int BASE_SPEED_LONG_DRIVE = Integer.parseInt(JSON.get("BASE_SPEED_LONG_DRIVE").toString()); // Gets the base speed of a long-drive bus from the JSON file.
        final int BASE_SPEED_EXPRESS = Integer.parseInt(JSON.get("BASE_SPEED_EXPRESS").toString()); // Gets the base speed of an express bus from the JSON file.
        if (BASE_SPEED_MINI_BUS < 0 || BASE_SPEED_CONVENCIONAL < 0 || BASE_SPEED_LONG_DRIVE < 0 || BASE_SPEED_EXPRESS < 0) { // Checks if the base speed of a bus is less than 0.
            System.err.println("""
                    ERROR: The base speed of a bus must be greater than or equal to 0."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the base speed of a bus is less than 0.
        }
        final int FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION").toString()); // Gets the flat tire delay for a malfunction to be repaired from the JSON file.
        final int COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION").toString()); // Gets the cooling system delay for a malfunction to be repaired from the JSON file.
        final int COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION").toString()); // Gets the collision delay for a malfunction to be repaired from the JSON file.
        if (FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION < 0 || COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION < 0 || COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION < 0) { // Checks if the delay for a malfunction to be repaired is less than 0.
            System.err.println("""
                    ERROR: The delay for a malfunction to be repaired must be greater than or equal to 0."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the delay for a malfunction to be repaired is less than 0.
        }
        final double MALFUNCTION_PROBABILITY = Double.parseDouble(JSON.get("MALFUNCTION_PROBABILITY").toString()); // Gets the probability of a malfunction occurring from the JSON file.
        if (MALFUNCTION_PROBABILITY < 0 || MALFUNCTION_PROBABILITY > 1) { // Checks if the probability of a malfunction occurring is less than 0 or greater than 1.
            System.err.println("""
                    ERROR: The probability of a malfunction occurring must be between 0 and 1."""); // If it is, it will print an error.
            System.exit(1); // Exits the program, if the probability of a malfunction occurring is less than 0 or greater than 1.
        }
        System.out.println("""
                ========================================================
                                   SIMULATION STARTED
                ========================================================"""); // Prints a message to the console indicating the start of the simulation.
        final BUS[] BUS_COLLECTION = new BUS[NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESS]; // Creates an array of buses, using the total number of buses.
        for (int i = 0; i < NUMBER_OF_MINI_BUS; i++) { // For each mini bus, create a new mini bus. This loop will create the number of mini buses specified in the JSON file.
            BUS_STOP ORIGIN = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)]; // Gets a random bus stop from the list of bus stops, to be the origin of the bus.
            BUS_STOP DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)]; // Gets a random bus stop from the list of bus stops, to be the destination of the bus.
            while (DESTINATION == ORIGIN) { // If the destination is the same as the origin, get a new destination.
                DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            }
            BUS BUS_THREAD = new BUS("MINI_BUS #" + (i + 1), BUS_TYPE.MINI_BUS, ORIGIN, DESTINATION, MAX_CAPACITY_MINI_BUS, BASE_SPEED_MINI_BUS); // Creates a new mini bus. The bus will be named "MINI_BUS #" + (i + 1), and will have the maximum capacity of the mini bus, the base speed of the mini bus, and the origin and destination of the bus. The bus will be added to the array of buses.
            BUS_THREAD.start(); // Starts the bus.
            BUS_COLLECTION[i] = BUS_THREAD; // Adds the bus to the array of buses.
        }
        for (int i = 0; i < NUMBER_OF_CONVENCIONAL; i++) { // For each conventional bus, create a new conventional bus. This loop will create the number of conventional buses specified in the JSON file.
            BUS_STOP ORIGIN = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)]; // Gets a random bus stop from the list of bus stops, to be the origin of the bus.
            BUS_STOP DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)]; // Gets a random bus stop from the list of bus stops, to be the destination of the bus.
            while (DESTINATION == ORIGIN) { // If the destination is the same as the origin, get a new destination.
                DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            }
            BUS BUS_THREAD = new BUS("CONVENCIONAL #" + (i + 1), BUS_TYPE.CONVENTIONAL, ORIGIN, DESTINATION, MAX_CAPACITY_CONVENCIONAL, BASE_SPEED_CONVENCIONAL); // Creates a new conventional bus. The bus will be named "CONVENCIONAL #" + (i + 1), and will have the maximum capacity of the conventional bus, the base speed of the conventional bus, and the origin and destination of the bus. The bus will be added to the array of buses.
            BUS_THREAD.start(); // Starts the bus.
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS] = BUS_THREAD; // Adds the bus to the array of buses.
        }
        for (int i = 0; i < NUMBER_OF_LONG_DRIVE; i++) { // For each long-drive bus, create a new long-drive bus. This loop will create the number of long-drive buses specified in the JSON file.
            BUS_STOP ORIGIN = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)]; // Gets a random bus stop from the list of bus stops, to be the origin of the bus.
            BUS_STOP DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)]; // Gets a random bus stop from the list of bus stops, to be the destination of the bus.
            while (DESTINATION == ORIGIN) { // If the destination is the same as the origin, get a new destination.
                DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            }
            BUS BUS_THREAD = new BUS("LONG_DRIVE #" + (i + 1), BUS_TYPE.LONG_DRIVE, ORIGIN, DESTINATION, MAX_CAPACITY_LONG_DRIVE, BASE_SPEED_LONG_DRIVE); // Creates a new long-drive bus. The bus will be named "LONG_DRIVE #" + (i + 1), and will have the maximum capacity of the long-drive bus, the base speed of the long-drive bus, and the origin and destination of the bus. The bus will be added to the array of buses.
            BUS_THREAD.start(); // Starts the bus.
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL] = BUS_THREAD; // Adds the bus to the array of buses.
        }
        for (int i = 0; i < NUMBER_OF_EXPRESS; i++) { // For each express bus, create a new express bus. This loop will create the number of express buses specified in the JSON file.
            BUS_STOP[] STOPS_EXPRESS = Arrays.stream(BUS_STOP.values()).filter(j -> j != BUS_STOP.CASCAIS && j != BUS_STOP.COIMBRA).toArray(BUS_STOP[]::new); // Gets a list of all bus stops except for the two bus stops that are not used for the express bus, and puts them into an array.
            BUS_STOP ORIGIN = STOPS_EXPRESS[new Random().nextInt(STOPS_EXPRESS.length)]; // Gets a random bus stop from the list of bus stops, to be the origin of the bus.
            BUS_STOP DESTINATION = STOPS_EXPRESS[new Random().nextInt(STOPS_EXPRESS.length)]; // Gets a random bus stop from the list of bus stops, to be the destination of the bus.
            while (DESTINATION == ORIGIN) { // If the destination is the same as the origin, get a new destination.
                DESTINATION = STOPS_EXPRESS[new Random().nextInt(STOPS_EXPRESS.length)];
            }
            BUS BUS_THREAD = new BUS("EXPRESS #" + (i + 1), BUS_TYPE.EXPRESS, ORIGIN, DESTINATION, MAX_CAPACITY_EXPRESS, BASE_SPEED_EXPRESS); // Creates a new express bus. The bus will be named "EXPRESS #" + (i + 1), and will have the maximum capacity of the express bus, the base speed of the express bus, and the origin and destination of the bus. The bus will be added to the array of buses.
            BUS_THREAD.start(); // Starts the bus.
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE] = BUS_THREAD; // Adds the bus to the array of buses.
        }
        Timer MALFUNCTION_TIMER = new Timer("MALFUNCTION_TIMER"); // Creates a new timer that will be used to schedule the malfunction of the buses.
        MALFUNCTION_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { // This method will be called every time the timer is called.
                BUS BUS_THREAD = BUS_COLLECTION[new Random().nextInt(BUS_COLLECTION.length)]; // Gets a random bus from the array of buses.
                if (!BUS_THREAD.IS_PAUSED() && BUS_THREAD.isAlive() && new Random().nextDouble() < MALFUNCTION_PROBABILITY) { // If the bus is not paused, is alive, and the random number is less than the probability of a malfunction, then the bus will malfunction.
                    MALFUNCTION_TYPE MAL_FUNCTION_TYPE = MALFUNCTION_TYPE.values()[new Random().nextInt(MALFUNCTION_TYPE.values().length)]; // Gets a random malfunction type from the list of malfunction types.
                    BUS_THREAD.PAUSE(switch (MAL_FUNCTION_TYPE) { // Pauses the bus, depending on the malfunction type.
                        case FLAT_TIRE ->
                                FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION; // If the malfunction type is flat tire, then the bus will be paused for the flat tire delay for malfunction execution.
                        case COOLING_SYSTEM ->
                                COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION; // If the malfunction type is cooling system, then the bus will be paused for the cooling system delay for malfunction execution.
                        case COLLISION ->
                                COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION; // If the malfunction type is collision, then the bus will be paused for the collision delay for malfunction execution.
                    });
                    System.out.println("\n===========================================================" +
                            "\n [MALFUNCTION] " + MAL_FUNCTION_TYPE + ": " + BUS_THREAD.getName() +
                            "\n==========================================================="); // Prints out a message indicating that the bus has malfunctioned.
                }
            }
        }, 0, new Random().nextInt(MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION, MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION)); // The dealy for the malfunction execution will be a random number between the minimum delay for malfunction execution and the maximum delay for malfunction execution, collected from the JSON file.
        for (BUS BUS_THREAD : BUS_COLLECTION) { // For each bus in the array of buses, wait for the bus to finish.
            try {
                BUS_THREAD.join();
            } catch (
                    InterruptedException e) { // If the bus is interrupted, then print out a message indicating that the bus has been interrupted.
                System.err.println("""
                        An error occurred while waiting for the bus to finish its journey.
                        The program will now exit.
                        """); // Prints out a message indicating that the bus has been interrupted.
                System.exit(1); // Exits the program.
            }
        }
        System.out.println("""
                ========================================================
                                    SIMULATION ENDED
                ========================================================"""); // Prints the message indicating that the simulation has ended.
        MALFUNCTION_TIMER.cancel(); // Cancels the malfunction timer.
        System.exit(0); // Exits the program.
    }
}