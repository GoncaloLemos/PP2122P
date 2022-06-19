/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package models;

import java.util.Random;
import java.util.stream.IntStream;

public class BUS extends Thread {
    /**
     * The bus’ maximum capacity.
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
     * The bus' origin city.
     */
    private final CITY ORIGIN;
    /**
     * The bus' current speed. (Delay in milliseconds it takes to travel between two stops.)
     * It is calculated based on the bus' type.
     * It is also the maximum speed of the bus.
     */
    private final int SPEED;
    /**
     * The bus' current stop.
     */
    private CITY CURRENT_STOP;
    /**
     * The bus' current amount passengers.
     */
    private int CURRENT_PASSENGERS = 0;

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public BUS(final String THREAD_NAME, final BUS_TYPE TYPE, final CITY DESTINATION, final CITY ORIGIN, final int MAX_CAPACITY, final int SPEED) {
        this.setName(THREAD_NAME);
        this.TYPE = TYPE;
        this.DESTINATION = DESTINATION;
        this.ORIGIN = ORIGIN;
        this.MAX_CAPACITY = MAX_CAPACITY;
        CURRENT_STOP = this.ORIGIN;
        this.SPEED = SPEED;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void run() {
        try {
            while (this.GET_NEXT_STOP() != null) {
                CURRENT_PASSENGERS = new Random().nextInt(MAX_CAPACITY + 1);
                System.out.println("\n============================================================\n" + this.getName() + ":\n" + ORIGIN + " --> " + DESTINATION + "\nCurrent Stop: " + CURRENT_STOP + "\nCurrent Passengers: " + CURRENT_PASSENGERS + "\n============================================================");
                Thread.sleep(SPEED);
                CURRENT_STOP = GET_NEXT_STOP();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("\n============================================================\n" + this.getName() + ":\n" + ORIGIN + " --> " + DESTINATION + "\nCurrent Stop: " + CURRENT_STOP + "\nCurrent Passengers: " + CURRENT_PASSENGERS + "\n============================================================");
            System.out.println("\n============================================================\n" + this.getName() + " has arrived to " + DESTINATION + "." + "\n============================================================");
            CURRENT_PASSENGERS = 0;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

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
}