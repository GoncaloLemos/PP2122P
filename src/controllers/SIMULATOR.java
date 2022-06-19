/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package controllers;

import models.BUS;
import models.BUS_TYPE;
import models.CITY;
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

    ///////////ATTRIBUTES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * The amount mini buses.
     */
    final int NUMBER_OF_MINI_BUS;
    /**
     * The amount conventional buses.
     */
    final int NUMBER_OF_CONVENCIONAL;
    /**
     * The amount long drive buses.
     */
    final int NUMBER_OF_LONG_DRIVE;
    /**
     * The
     */
    final int NUMBER_OF_EXPRESSO;
    final int MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION;
    final int MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION;
    final int MAX_CAPACITY_MINI_BUS;
    final int MAX_CAPACITY_CONVENCIONAL;
    final int MAX_CAPACITY_LONG_DRIVE;
    final int MAX_CAPACITY_EXPRESSO;
    final int BASE_SPEED_MINI_BUS;
    final int BASE_SPEED_CONVENCIONAL;
    final int BASE_SPEED_LONG_DRIVE;
    final int BASE_SPEED_EXPRESSO;
    final int FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION;
    final int COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION;
    final int COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION;
    final int REFUELING_TIME;
    final int SWITCH_DRIVER_TIME;
    final int STOP_FOR_MAINTENANCE_TIME;
    final int GENERAL_MAINTAINANCE_TIME;
    private final BUS[] BUS_COLLECTION;

    ///////////CONSTRUCTOR////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for the SIMULATOR class.
     */
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
        NUMBER_OF_MINI_BUS = Integer.parseInt(JSON.get("NUMBER_OF_MINI_BUS").toString());
        NUMBER_OF_CONVENCIONAL = Integer.parseInt(JSON.get("NUMBER_OF_CONVENCIONAL").toString());
        NUMBER_OF_LONG_DRIVE = Integer.parseInt(JSON.get("NUMBER_OF_LONG_DRIVE").toString());
        NUMBER_OF_EXPRESSO = Integer.parseInt(JSON.get("NUMBER_OF_EXPRESSO").toString());
        MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MINIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("MAXIMUM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        MAX_CAPACITY_MINI_BUS = Integer.parseInt(JSON.get("MAX_CAPACITY_MINI_BUS").toString());
        MAX_CAPACITY_CONVENCIONAL = Integer.parseInt(JSON.get("MAX_CAPACITY_CONVENCIONAL").toString());
        MAX_CAPACITY_LONG_DRIVE = Integer.parseInt(JSON.get("MAX_CAPACITY_LONG_DRIVE").toString());
        MAX_CAPACITY_EXPRESSO = Integer.parseInt(JSON.get("MAX_CAPACITY_EXPRESSO").toString());
        BASE_SPEED_MINI_BUS = Integer.parseInt(JSON.get("BASE_SPEED_MINI_BUS").toString());
        BASE_SPEED_CONVENCIONAL = Integer.parseInt(JSON.get("BASE_SPEED_CONVENCIONAL").toString());
        BASE_SPEED_LONG_DRIVE = Integer.parseInt(JSON.get("BASE_SPEED_LONG_DRIVE").toString());
        BASE_SPEED_EXPRESSO = Integer.parseInt(JSON.get("BASE_SPEED_EXPRESSO").toString());
        FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("FLAT_TIRE_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COOLING_SYSTEM_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION = Integer.parseInt(JSON.get("COLLISION_DELAY_FOR_MALFUNCTION_EXECUTION").toString());
        REFUELING_TIME = Integer.parseInt(JSON.get("REFUELING_TIME").toString());
        SWITCH_DRIVER_TIME = Integer.parseInt(JSON.get("SWITCH_DRIVER_TIME").toString());
        STOP_FOR_MAINTENANCE_TIME = Integer.parseInt(JSON.get("STOP_FOR_MAINTENANCE_TIME").toString());
        GENERAL_MAINTAINANCE_TIME = Integer.parseInt(JSON.get("GENERAL_MAINTAINANCE_TIME").toString());
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

    /**
     * Stops the bus, for a given delay.
     *
     * @param BUSTYPE    The type of bus to be stopped.
     * @param BUS_NUMBER The amount bus to be stopped.
     * @param STOP_TIME  The time to wait before stopping the bus.
     */
    public void stopBus(final String BUSTYPE, final String BUS_NUMBER, final int STOP_TIME) {
        BUS_TYPE BT = switch (BUSTYPE) {
            case "MINI_BUS" -> BUS_TYPE.MINI_BUS;
            case "CONVENCIONAL" -> BUS_TYPE.CONVENTIONAL;
            case "LONG_DRIVE" -> BUS_TYPE.LONG_DRIVE;
            case "EXPRESSO" -> BUS_TYPE.EXPRESS;
        };
        for (BUS BUS_THREAD : BUS_COLLECTION) {
            if (BUS_THREAD.GET_TYPE() == BT && BUS_THREAD.getName().equals(BT + " #" + BUS_NUMBER)) {
                BUS_THREAD.PAUSE(STOP_TIME);
                System.out.println("\n===========================================================" +
                        "\n [STOPPED] " + BUS_THREAD.getName() + " FOR " + STOP_TIME + " MILLISECONDS." +
                        "\n===========================================================");
                break;
            }
        }
    }

    /**
     * Refeuls the bus.
     *
     * @param BUSTYPE    The type of bus to be resumed.
     * @param BUS_NUMBER The amount bus to be resumed.
     */
    public void refuel(final String BUSTYPE, final String BUS_NUMBER) {
        BUS_TYPE BT = switch (BUSTYPE) {
            case "MINI_BUS" -> BUS_TYPE.MINI_BUS;
            case "CONVENCIONAL" -> BUS_TYPE.CONVENTIONAL;
            case "LONG_DRIVE" -> BUS_TYPE.LONG_DRIVE;
            case "EXPRESSO" -> BUS_TYPE.EXPRESS;
        };
        for (BUS BUS_THREAD : BUS_COLLECTION) {
            if (BUS_THREAD.GET_TYPE() == BT && BUS_THREAD.getName().equals(BT + " #" + BUS_NUMBER)) {
                BUS_THREAD.PAUSE(REFUELING_TIME);
                System.out.println("\n===========================================================" +
                        "\n [REFUELLING] " + BUS_THREAD.getName() +
                        "\n===========================================================");
                break;
            }
        }
    }

    /**
     * Switches the driver of the bus.
     *
     * @param BUSTYPE    The type of bus to be paused.
     * @param BUS_NUMBER The amount bus to be paused.
     */
    public void switchDriver(final String BUSTYPE, final String BUS_NUMBER) {
        BUS_TYPE BT = switch (BUSTYPE) {
            case "MINI_BUS" -> BUS_TYPE.MINI_BUS;
            case "CONVENCIONAL" -> BUS_TYPE.CONVENTIONAL;
            case "LONG_DRIVE" -> BUS_TYPE.LONG_DRIVE;
            case "EXPRESSO" -> BUS_TYPE.EXPRESS;
        };
        for (BUS BUS_THREAD : BUS_COLLECTION) {
            if (BUS_THREAD.GET_TYPE() == BT && BUS_THREAD.getName().equals(BT + " #" + BUS_NUMBER)) {
                BUS_THREAD.PAUSE(SWITCH_DRIVER_TIME);
                System.out.println("\n===========================================================" +
                        "\n [SWITCHING DRIVER] " + BUS_THREAD.getName() +
                        "\n===========================================================");
                break;
            }
        }
    }

    /**
     * Stops the bus for maintenance.
     *
     * @param BUSTYPE    The type of bus to be stopped.
     * @param BUS_NUMBER The amount bus to be stopped.
     */
    public void stopForMaintenance(final String BUSTYPE, final String BUS_NUMBER) {
        BUS_TYPE BT = switch (BUSTYPE) {
            case "MINI_BUS" -> BUS_TYPE.MINI_BUS;
            case "CONVENCIONAL" -> BUS_TYPE.CONVENTIONAL;
            case "LONG_DRIVE" -> BUS_TYPE.LONG_DRIVE;
            case "EXPRESSO" -> BUS_TYPE.EXPRESS;
        };
        for (BUS BUS_THREAD : BUS_COLLECTION) {
            if (BUS_THREAD.GET_TYPE() == BT && BUS_THREAD.getName().equals(BT + " #" + BUS_NUMBER)) {
                BUS_THREAD.PAUSE(GENERAL_MAINTAINANCE_TIME);
                System.out.println("\n===========================================================" +
                        "\n [STOPPED FOR MAINTENANCE] " + BUS_THREAD.getName() +
                        "\n===========================================================");
                break;
            }
        }
    }
}