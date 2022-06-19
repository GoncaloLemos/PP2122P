/*
 * Copyright © 2022, Pedro S.. All rights reserved.
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
    ///////////CONSTRUCTOR////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public SIMULATOR() {
        JSONObject JSON = null;
        try {
            JSON = (JSONObject) new JSONParser().parse(new FileReader("./src/config.json"));
        } catch (IOException e) {
            System.err.println("""
                    ERROR: Could not read config.json file.
                    Please make sure it is in the same directory as the program.""");
            System.exit(1);
        } catch (ParseException e) {
            System.err.println("""
                    ERROR: Could not parse config.json file.
                    Please make sure it is in the same directory as the program.""");
            System.exit(1);
        }
        final int MINIMUM_FLEET_SIZE = Integer.parseInt(JSON.get("MINIMUM_FLEET_SIZE").toString());
        final int MAXIMUM_FLEET_SIZE = Integer.parseInt(JSON.get("MAXIMUM_FLEET_SIZE").toString());
        final int NUMBER_OF_MINI_BUS = Integer.parseInt(JSON.get("NUMBER_OF_MINI_BUS").toString());
        final int NUMBER_OF_CONVENCIONAL = Integer.parseInt(JSON.get("NUMBER_OF_CONVENCIONAL").toString());
        final int NUMBER_OF_LONG_DRIVE = Integer.parseInt(JSON.get("NUMBER_OF_LONG_DRIVE").toString());
        final int NUMBER_OF_EXPRESS = Integer.parseInt(JSON.get("NUMBER_OF_EXPRESS").toString());
        if (MINIMUM_FLEET_SIZE > MAXIMUM_FLEET_SIZE) {
            System.err.println("""
                    ERROR: MINIMUM_FLEET_SIZE must be less than or equal to MAXIMUM_FLEET_SIZE.""");
            System.exit(1);
        }
        if (NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESS > MAXIMUM_FLEET_SIZE) {
            System.err.println("""
                    ERROR: The sum of the number of buses of each type must be less than or equal to MAXIMUM_FLEET_SIZE.""");
            System.exit(1);
        }
        if (NUMBER_OF_MINI_BUS < 1 || NUMBER_OF_CONVENCIONAL < 1 || NUMBER_OF_LONG_DRIVE < 1 || NUMBER_OF_EXPRESS < 1) {
            System.err.println("""
                    ERROR: The number of buses of each type must be greater than or equal to 1.""");
            System.exit(1);
        }
        if (NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESS < MINIMUM_FLEET_SIZE) {
            System.err.println("""
                    ERROR: The sum of the number of buses of each type must be greater than or equal to MINIMUM_FLEET_SIZE.""");
            System.exit(1);
        }
        final int MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int MAX_CAPACITY_MINI_BUS = Integer.parseInt(JSON.get("MAX_CAPACITY_MINI_BUS").toString());
        final int MAX_CAPACITY_CONVENCIONAL = Integer.parseInt(JSON.get("MAX_CAPACITY_CONVENCIONAL").toString());
        final int MAX_CAPACITY_LONG_DRIVE = Integer.parseInt(JSON.get("MAX_CAPACITY_LONG_DRIVE").toString());
        final int MAX_CAPACITY_EXPRESS = Integer.parseInt(JSON.get("MAX_CAPACITY_EXPRESS").toString());
        final int BASE_SPEED_MINI_BUS = Integer.parseInt(JSON.get("BASE_SPEED_MINI_BUS").toString());
        final int BASE_SPEED_CONVENCIONAL = Integer.parseInt(JSON.get("BASE_SPEED_CONVENCIONAL").toString());
        final int BASE_SPEED_LONG_DRIVE = Integer.parseInt(JSON.get("BASE_SPEED_LONG_DRIVE").toString());
        final int BASE_SPEED_EXPRESS = Integer.parseInt(JSON.get("BASE_SPEED_EXPRESS").toString());
        final int FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        System.out.println("""
                ========================================================
                                   SIMULATION STARTED
                ========================================================""");
        final BUS[] BUS_COLLECTION = new BUS[NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESS];
        for (int i = 0; i < NUMBER_OF_MINI_BUS; i++) {
            BUS_STOP ORIGIN = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            BUS_STOP DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            }
            BUS BUS_THREAD = new BUS("MINI_BUS #" + (i + 1), BUS_TYPE.MINI_BUS, ORIGIN, DESTINATION, MAX_CAPACITY_MINI_BUS, BASE_SPEED_MINI_BUS);
            BUS_THREAD.start();
            BUS_COLLECTION[i] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_CONVENCIONAL; i++) {
            BUS_STOP ORIGIN = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            BUS_STOP DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            }
            BUS BUS_THREAD = new BUS("CONVENCIONAL #" + (i + 1), BUS_TYPE.CONVENTIONAL, ORIGIN, DESTINATION, MAX_CAPACITY_CONVENCIONAL, BASE_SPEED_CONVENCIONAL);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_LONG_DRIVE; i++) {
            BUS_STOP ORIGIN = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            BUS_STOP DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = BUS_STOP.values()[new Random().nextInt(BUS_STOP.values().length)];
            }
            BUS BUS_THREAD = new BUS("LONG_DRIVE #" + (i + 1), BUS_TYPE.LONG_DRIVE, ORIGIN, DESTINATION, MAX_CAPACITY_LONG_DRIVE, BASE_SPEED_LONG_DRIVE);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_EXPRESS; i++) {
            BUS_STOP[] STOPS_EXPRESS = Arrays.stream(BUS_STOP.values()).filter(j -> j != BUS_STOP.CASCAIS && j != BUS_STOP.COIMBRA).toArray(BUS_STOP[]::new);
            BUS_STOP ORIGIN = STOPS_EXPRESS[new Random().nextInt(STOPS_EXPRESS.length)];
            BUS_STOP DESTINATION = STOPS_EXPRESS[new Random().nextInt(STOPS_EXPRESS.length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = STOPS_EXPRESS[new Random().nextInt(STOPS_EXPRESS.length)];
            }
            BUS BUS_THREAD = new BUS("EXPRESS #" + (i + 1), BUS_TYPE.EXPRESS, ORIGIN, DESTINATION, MAX_CAPACITY_EXPRESS, BASE_SPEED_EXPRESS);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE] = BUS_THREAD;
        }
        Timer MALFUNCTION_TIMER = new Timer("MALFUNCTION_TIMER");
        MALFUNCTION_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                BUS BUS_THREAD = BUS_COLLECTION[new Random().nextInt(BUS_COLLECTION.length)];
                if (!BUS_THREAD.IS_PAUSED() && BUS_THREAD.isAlive()) {
                    MALFUNCTION_TYPE MAL_FUNCTION_TYPE = MALFUNCTION_TYPE.values()[new Random().nextInt(MALFUNCTION_TYPE.values().length)];
                    BUS_THREAD.PAUSE(switch (MAL_FUNCTION_TYPE) {
                        case FLAT_TIRE -> FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION;
                        case COOLING_SYSTEM -> COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION;
                        case COLLISION -> COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION;
                    });
                    System.out.println("\n===========================================================" +
                            "\n [MALFUNCTION] " + MAL_FUNCTION_TYPE + ": " + BUS_THREAD.getName() +
                            "\n===========================================================");
                }
            }
        }, 0, new Random().nextInt(MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION, MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION));
        for (BUS BUS_THREAD : BUS_COLLECTION) {
            try {
                BUS_THREAD.join();
            } catch (InterruptedException e) {
                System.err.println("""
                        An error occurred while waiting for the bus to finish its journey.
                        The program will now exit.
                        """);
                System.exit(1);
            }
        }
        System.out.println("""
                ========================================================
                                    SIMULATION ENDED
                ========================================================""");
        MALFUNCTION_TIMER.cancel();
        System.exit(0);
    }
}