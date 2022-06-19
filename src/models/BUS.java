/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package models;

import java.util.Random;
import java.util.stream.IntStream;

// BUS is the class that represents a bus. It is essentially a thread that runs as long as the bus hasn't reached its destination.
public class BUS extends Thread {
    ///////////ATTRIBUTES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final int MAX_CAPACITY;
    private final BUS_TYPE TYPE; // Atribute that represents the type of the bus.
    private final BUS_STOP DESTINATION; // Atribute that represents the destination of the bus.
    private final BUS_STOP ORIGIN; // Atribute that represents the origin of the bus.
    private final int TRAVEL_DELAY; // Atribute that represents the time (in milliseconds) the bus takes between two stops.
    private BUS_STOP CURRENT_STOP; // Atribute that represents the current stop of the bus.
    private int CURRENT_PASSENGERS = 0; // Atribute that represents the current amount passengers in the bus.
    private boolean IS_PAUSED = false; // Atribute that represents if the bus is paused. The bus only pauses when it malfunctions or if the manager requests it.
    private int PAUSE_DURATION = 0; // Atribute that represents the duration of the pause.

    ///////////CONSTRUCTOR////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BUS(final String THREAD_NAME, final BUS_TYPE TYPE, final BUS_STOP DESTINATION, final BUS_STOP ORIGIN, final int MAX_CAPACITY, final int TRAVEL_DELAY) {
        this.setName(THREAD_NAME);
        this.TYPE = TYPE;
        this.DESTINATION = DESTINATION;
        this.ORIGIN = ORIGIN;
        this.MAX_CAPACITY = MAX_CAPACITY;
        CURRENT_STOP = this.ORIGIN;
        this.TRAVEL_DELAY = TRAVEL_DELAY;
    }

    ///////////OVERRIDDEN METHODS////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    public int GET_MAX_CAPACITY() {
        return MAX_CAPACITY;
    }

    public BUS_TYPE GET_TYPE() {
        return TYPE;
    }

    public BUS_STOP GET_DESTINATION() {
        return DESTINATION;
    }

    public BUS_STOP GET_ORIGIN() {
        return ORIGIN;
    }

    public int GET_TRAVEL_DELAY() {
        return TRAVEL_DELAY;
    }

    public BUS_STOP GET_CURRENT_STOP() {
        return CURRENT_STOP;
    }

    public int GET_CURRENT_PASSENGERS() {
        return CURRENT_PASSENGERS;
    }

    public boolean IS_PAUSED() {
        return IS_PAUSED;
    }

    public int GET_PAUSE_DURATION() {
        return PAUSE_DURATION;
    }

    ///////////METHODS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public BUS_STOP GET_NEXT_STOP() {
        BUS_STOP[] STOPS = new BUS_STOP[]{BUS_STOP.CASCAIS, BUS_STOP.LISBOA, BUS_STOP.COIMBRA, BUS_STOP.PORTO, BUS_STOP.BRAGA};
        BUS_STOP[] STOPS_EXPRESS = new BUS_STOP[]{BUS_STOP.LISBOA, BUS_STOP.PORTO, BUS_STOP.BRAGA};
        return CURRENT_STOP == DESTINATION ? null : TYPE == BUS_TYPE.EXPRESS ? IntStream.range(0, STOPS_EXPRESS.length).filter(i -> ORIGIN == STOPS_EXPRESS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS_EXPRESS.length).filter(i -> DESTINATION == STOPS_EXPRESS[i]).findFirst().getAsInt() ? STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() + 1] : STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() - 1] : IntStream.range(0, STOPS.length).filter(i -> ORIGIN == STOPS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS.length).filter(i -> DESTINATION == STOPS[i]).findFirst().getAsInt() ? STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() + 1] : STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() - 1];
    }

    public void PAUSE(final int DURATION) {
        IS_PAUSED = true;
        PAUSE_DURATION = DURATION;
    }
}