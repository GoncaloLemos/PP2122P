/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package models;

import java.util.Random;
import java.util.stream.IntStream;

public class BUS extends Thread {
    ///////////ATTRIBUTES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The bus' max capacity.
     */
    private final int MAX_CAPACITY;
    /**
     * The bus' type.
     */
    private final BUS_TYPE TYPE;
    /**
     * The bus' destination.
     */
    private final CITY DESTINATION;
    /**
     * The bus' origin.
     */
    private final CITY ORIGIN;
    /**
     * The bus' travel time.
     */
    private final int TRAVEL_DELAY;
    /**
     * The bus' current stop.
     */
    private CITY CURRENT_STOP;
    /**
     * The bus' current passengers.
     */
    private int CURRENT_PASSENGERS = 0;
    /**
     * Returns 'true' if the bus is paused, do to a malfunction or by the managers request (for a driver switch, maintainance, or refeulling); otherwise, returns 'false'.
     */
    private boolean IS_PAUSED = false;
    /**
     * The duration of the bus' pause, in milliseconds.
     */
    private int PAUSE_DURATION = 0;

    ///////////CONSTRUCTOR////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for the BUS class.
     *
     * @param THREAD_NAME  Name of the thread.
     * @param TYPE         Type of the bus.
     * @param DESTINATION  Destination of the bus.
     * @param ORIGIN       Origin of the bus.
     * @param MAX_CAPACITY Maximum capacity of the bus.
     * @param TRAVEL_DELAY Travel delay of the bus.
     */
    public BUS(final String THREAD_NAME, final BUS_TYPE TYPE, final CITY DESTINATION, final CITY ORIGIN, final int MAX_CAPACITY, final int TRAVEL_DELAY) {
        this.setName(THREAD_NAME);
        this.TYPE = TYPE;
        this.DESTINATION = DESTINATION;
        this.ORIGIN = ORIGIN;
        this.MAX_CAPACITY = MAX_CAPACITY;
        CURRENT_STOP = this.ORIGIN;
        this.TRAVEL_DELAY = TRAVEL_DELAY;
    }

    ///////////OVERRIDDEN METHODS////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Overridden run method.
     * Represents the bus' behavior, until it reaches its destination.
     */
    @Override
    public void run() {
        while (this.GET_NEXT_STOP() != null) {
            CURRENT_PASSENGERS = new Random().nextInt(MAX_CAPACITY + 1);
            System.out.println("\n============================================================\n" + this.getName() + ":\n" + ORIGIN + " --> " + DESTINATION + "\nCurrent Stop: " + CURRENT_STOP + "\nCurrent Passengers: " + CURRENT_PASSENGERS + "\n============================================================");
            IntStream.range(0, TRAVEL_DELAY).forEach(i -> {
                try {
                    if (IS_PAUSED && this.getState() != State.TIMED_WAITING) {
                        sleep(PAUSE_DURATION);
                        IS_PAUSED = false;
                        System.out.println("\n===========================================================" +
                                "\n            " + getName() + " IS BACK TO WORK." +
                                "\n===========================================================");
                    }
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            CURRENT_STOP = GET_NEXT_STOP();
        }
        System.out.println("\n============================================================\n" + this.getName() + ":\n" + ORIGIN + " --> " + DESTINATION + "\nCurrent Stop: " + CURRENT_STOP + "\nCurrent Passengers: " + CURRENT_PASSENGERS + "\n============================================================");
        System.out.println("\n============================================================\n" + this.getName() + " has arrived to " + DESTINATION + "." + "\n============================================================");
        CURRENT_PASSENGERS = 0;
    }

    ///////////GETTERS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the bus' max capacity.
     *
     * @return The bus' max capacity.
     */
    public int GET_MAX_CAPACITY() {
        return MAX_CAPACITY;
    }

    /**
     * Returns the bus' type.
     *
     * @return The bus' type.
     */
    public BUS_TYPE GET_TYPE() {
        return TYPE;
    }

    /**
     * Returns the bus' destination.
     *
     * @return The bus' destination.
     */
    public CITY GET_DESTINATION() {
        return DESTINATION;
    }

    /**
     * Returns the bus' origin.
     *
     * @return The bus' origin.
     */
    public CITY GET_ORIGIN() {
        return ORIGIN;
    }

    /**
     * Returns the bus' travel time.
     *
     * @return The bus' travel time.
     */
    public int GET_TRAVEL_DELAY() {
        return TRAVEL_DELAY;
    }

    /**
     * Returns the bus' current stop.
     *
     * @return The bus' current stop.
     */
    public CITY GET_CURRENT_STOP() {
        return CURRENT_STOP;
    }

    /**
     * Returns the bus' current passengers.
     *
     * @return The bus' current passengers.
     */
    public int GET_CURRENT_PASSENGERS() {
        return CURRENT_PASSENGERS;
    }

    /**
     * Returns 'true' if the bus is paused, do to a malfunction or by the managers request (for a driver switch, maintainance, or refeulling); otherwise, returns 'false'.
     *
     * @return 'true' if the bus is paused, do to a malfunction or by the managers request (for a driver switch, maintainance, or refeulling); otherwise, returns 'false'.
     */
    public boolean IS_PAUSED() {
        return IS_PAUSED;
    }

    /**
     * Returns the duration of the bus' pause, in milliseconds.
     *
     * @return The duration of the bus' pause, in milliseconds.
     */
    public int GET_PAUSE_DURATION() {
        return PAUSE_DURATION;
    }

    ///////////METHODS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Pauses the bus for a certain duration.
     *
     * @return 'true' if the bus has been paused, 'false' otherwise.
     */
    public CITY GET_NEXT_STOP() {
        CITY[] STOPS = new CITY[]{CITY.CASCAIS, CITY.LISBOA, CITY.COIMBRA, CITY.PORTO, CITY.BRAGA};
        CITY[] STOPS_EXPRESS = new CITY[]{CITY.LISBOA, CITY.PORTO, CITY.BRAGA};
        if (CURRENT_STOP == DESTINATION) return null;
        return switch (TYPE) {
            case EXPRESS ->
                    IntStream.range(0, STOPS_EXPRESS.length).filter(i -> ORIGIN == STOPS_EXPRESS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS_EXPRESS.length).filter(i -> DESTINATION == STOPS_EXPRESS[i]).findFirst().getAsInt() ? STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() + 1] : STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() - 1];
            default ->
                    IntStream.range(0, STOPS.length).filter(i -> ORIGIN == STOPS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS.length).filter(i -> DESTINATION == STOPS[i]).findFirst().getAsInt() ? STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() + 1] : STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() - 1];
        };
    }

    /**
     * Pauses the bus for a certain duration.
     *
     * @param DURATION The duration of the pause, in milliseconds.
     */
    public void PAUSE(final int DURATION) {
        IS_PAUSED = true;
        PAUSE_DURATION = DURATION;
    }
}