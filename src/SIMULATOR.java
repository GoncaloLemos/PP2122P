/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

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

    ///////////ATTRIBUTES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final BUS[] BUS_COLLECTION;

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
        System.out.println("""
                ========================================================
                                   SIMULATION STARTED
                ========================================================""");
        ///////////CONFIGURATION FILE VARIABLES/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final int NUMBER_OF_MINI_BUS = Integer.parseInt(JSON.get("NUMBER_OF_MINI_BUS").toString());
        final int NUMBER_OF_CONVENCIONAL = Integer.parseInt(JSON.get("NUMBER_OF_CONVENCIONAL").toString());
        final int NUMBER_OF_LONG_DRIVE = Integer.parseInt(JSON.get("NUMBER_OF_LONG_DRIVE").toString());
        final int NUMBER_OF_EXPRESSO = Integer.parseInt(JSON.get("NUMBER_OF_EXPRESSO").toString());
        final int MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int MAX_CAPACITY_MINI_BUS = Integer.parseInt(JSON.get("MAX_CAPACITY_MINI_BUS").toString());
        final int MAX_CAPACITY_CONVENCIONAL = Integer.parseInt(JSON.get("MAX_CAPACITY_CONVENCIONAL").toString());
        final int MAX_CAPACITY_LONG_DRIVE = Integer.parseInt(JSON.get("MAX_CAPACITY_LONG_DRIVE").toString());
        final int MAX_CAPACITY_EXPRESSO = Integer.parseInt(JSON.get("MAX_CAPACITY_EXPRESSO").toString());
        final int BASE_SPEED_MINI_BUS = Integer.parseInt(JSON.get("BASE_SPEED_MINI_BUS").toString());
        final int BASE_SPEED_CONVENCIONAL = Integer.parseInt(JSON.get("BASE_SPEED_CONVENCIONAL").toString());
        final int BASE_SPEED_LONG_DRIVE = Integer.parseInt(JSON.get("BASE_SPEED_LONG_DRIVE").toString());
        final int BASE_SPEED_EXPRESSO = Integer.parseInt(JSON.get("BASE_SPEED_EXPRESSO").toString());
        final int FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        final int COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        BUS_COLLECTION = new BUS[NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL + NUMBER_OF_LONG_DRIVE + NUMBER_OF_EXPRESSO];
        for (int i = 0; i < NUMBER_OF_MINI_BUS; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("MINI_BUS #" + (i + 1), BUS_TYPE.MINI_BUS, ORIGIN, DESTINATION, MAX_CAPACITY_MINI_BUS, BASE_SPEED_MINI_BUS);
            BUS_THREAD.start();
            BUS_COLLECTION[i] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_CONVENCIONAL; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("CONVENCIONAL #" + (i + 1), BUS_TYPE.CONVENTIONAL, ORIGIN, DESTINATION, MAX_CAPACITY_CONVENCIONAL, BASE_SPEED_CONVENCIONAL);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_LONG_DRIVE; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("LONG_DRIVE #" + (i + 1), BUS_TYPE.LONG_DRIVE, ORIGIN, DESTINATION, MAX_CAPACITY_LONG_DRIVE, BASE_SPEED_LONG_DRIVE);
            BUS_THREAD.start();
            BUS_COLLECTION[i + NUMBER_OF_MINI_BUS + NUMBER_OF_CONVENCIONAL] = BUS_THREAD;
        }
        for (int i = 0; i < NUMBER_OF_EXPRESSO; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            }
            BUS BUS_THREAD = new BUS("EXPRESS #" + (i + 1), BUS_TYPE.EXPRESS, ORIGIN, DESTINATION, MAX_CAPACITY_EXPRESSO, BASE_SPEED_EXPRESSO);
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

    ///////////METHODS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void STOP_BUS_FOR(final String TYPE, final String BUS_NUMBER, final int STOP_TIME) {
        Arrays.stream(BUS_COLLECTION).filter(BUS_THREAD -> BUS_THREAD.GET_TYPE().toString().equals(TYPE) && BUS_THREAD.getName().equals(TYPE + " #" + BUS_NUMBER)).findFirst().ifPresent(BUS_THREAD -> BUS_THREAD.PAUSE(STOP_TIME));
    }
}