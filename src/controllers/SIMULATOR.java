/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package controllers;

import models.BUS;
import models.BUS_TYPE;
import models.CITY;
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
        System.out.println("""

                ========================================================
                                   SIMULATION STARTED
                ========================================================""");
        final int NUMBER_OF_MINI_BUS = JSON.get("NUMBER_OF_MINI_BUS") instanceof Long ? ((Long) JSON.get("NUMBER_OF_MINI_BUS")).intValue() : (int) JSON.get("NUMBER_OF_MINI_BUS");
        final int NUMBER_OF_CONVENCIONAL = JSON.get("NUMBER_OF_CONVENCIONAL") instanceof Long ? ((Long) JSON.get("NUMBER_OF_CONVENCIONAL")).intValue() : (int) JSON.get("NUMBER_OF_CONVENCIONAL");
        final int NUMBER_OF_LONG_DRIVE = JSON.get("NUMBER_OF_LONG_DRIVE") instanceof Long ? ((Long) JSON.get("NUMBER_OF_LONG_DRIVE")).intValue() : (int) JSON.get("NUMBER_OF_LONG_DRIVE");
        final int NUMBER_OF_EXPRESSO = JSON.get("NUMBER_OF_EXPRESSO") instanceof Long ? ((Long) JSON.get("NUMBER_OF_EXPRESSO")).intValue() : (int) JSON.get("NUMBER_OF_EXPRESSO");
        final int MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = JSON.get("MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION") instanceof Long ? ((Long) JSON.get("MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION")).intValue() : (int) JSON.get("MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION");
        final int MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = JSON.get("MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION") instanceof Long ? ((Long) JSON.get("MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION")).intValue() : (int) JSON.get("MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION");
        BUS[] BUS_COLLECTION = new BUS[NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESSO];
        for (int i = 0; i < NUMBER_OF_MINI_BUS; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("MINI_BUS #" + i + 1, BUS_TYPE.MINI_BUS, ORIGIN, DESTINATION);
            BUS_THREAD.start();
            BUS_COLLECTION[i] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_CONVENCIONAL; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("CONVENCIONAL #" + i + 1, BUS_TYPE.CONVENTIONAL, ORIGIN, DESTINATION);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_LONG_DRIVE; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("LONG_DRIVE #" + i + 1, BUS_TYPE.LONG_DRIVE, ORIGIN, DESTINATION);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_EXPRESSO; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            }
            BUS BUS_THREAD = new BUS("EXPRESS #" + i + 1, BUS_TYPE.EXPRESS, ORIGIN, DESTINATION);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE] = BUS_THREAD;
        }
        Timer MALFUNCTION_TIMER = new Timer("MALFUNCTION_TIMER");
        MALFUNCTION_TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // TODO: Implement malfunction execution.
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
        System.exit(0);
    }
}
