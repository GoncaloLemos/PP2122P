/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

import java.util.Random;
import java.util.stream.IntStream;

public class BUS extends Thread {
    ///////////ATTRIBUTES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final int MAX_CAPACITY;
    private final BUS_TYPE TYPE;
    private final CITY DESTINATION;
    private final CITY ORIGIN;
    private final int TRAVEL_DELAY;
    private CITY CURRENT_STOP;
    private int CURRENT_PASSENGERS = 0;
    private boolean IS_PAUSED = false;
    private int PAUSE_DURATION = 0;

    ///////////CONSTRUCTOR////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    public CITY GET_DESTINATION() {
        return DESTINATION;
    }

    public CITY GET_ORIGIN() {
        return ORIGIN;
    }

    public int GET_TRAVEL_DELAY() {
        return TRAVEL_DELAY;
    }

    public CITY GET_CURRENT_STOP() {
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
    public CITY GET_NEXT_STOP() {
        CITY[] STOPS = new CITY[]{CITY.CASCAIS, CITY.LISBOA, CITY.COIMBRA, CITY.PORTO, CITY.BRAGA};
        CITY[] STOPS_EXPRESS = new CITY[]{CITY.LISBOA, CITY.PORTO, CITY.BRAGA};
        return CURRENT_STOP == DESTINATION ? null : TYPE == BUS_TYPE.EXPRESS ? IntStream.range(0, STOPS_EXPRESS.length).filter(i -> ORIGIN == STOPS_EXPRESS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS_EXPRESS.length).filter(i -> DESTINATION == STOPS_EXPRESS[i]).findFirst().getAsInt() ? STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() + 1] : STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() - 1] : IntStream.range(0, STOPS.length).filter(i -> ORIGIN == STOPS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS.length).filter(i -> DESTINATION == STOPS[i]).findFirst().getAsInt() ? STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() + 1] : STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() - 1];
    }

    public void PAUSE(final int DURATION) {
        IS_PAUSED = true;
        PAUSE_DURATION = DURATION;
        System.out.println("\n===========================================================" +
                "\n [STOPPED] " + getName() + " FOR " + DURATION + " MILLISECONDS." +
                "\n===========================================================");
    }
}