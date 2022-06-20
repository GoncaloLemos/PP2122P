/*
 * Copyright © 2022, Pedro Simões & Gonçalo Lemos. All rights reserved.
 */

package models;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * The BUS class represents a bus. It is essentially a thread that runs in the background, designed to simulate the bus movement through a route.
 */
public class BUS extends Thread {
    ///////////ATTRIBUTES///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Maximum capacity of the bus.
     */
    private final int MAX_CAPACITY;
    /**
     * Type of the bus.
     */
    private final BUS_TYPE TYPE;
    /**
     * Destination of the bus.
     */
    private final BUS_STOP DESTINATION; // Destination of the bus.
    /**
     * Origin of the bus.
     */
    private final BUS_STOP ORIGIN; // Origin of the bus.
    /**
     * Delay between two stops on the bus' route.
     */
    private final int TRAVEL_DELAY;
    /**
     *
     */
    private BUS_STOP CURRENT_STOP;
    /**
     * Current amount of passengers on the bus.
     */
    private int CURRENT_PASSENGERS = 0;
    /**
     * Indicates whether the bus has stopped do to a malfunction.
     */
    private boolean IS_PAUSED = false;
    /**
     * Delay until the malfunction is resolved.
     */
    private int PAUSE_DURATION = 0;

    ///////////CONSTRUCTOR////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor of the class BUS.
     *
     * @param THREAD_NAME  Name of the thread.
     * @param TYPE         Type of the bus.
     * @param DESTINATION  Destination of the bus.
     * @param ORIGIN       Origin of the bus.
     * @param MAX_CAPACITY Maximum capacity of the bus.
     * @param TRAVEL_DELAY Delay between two stops on the bus' route.
     */
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
        while (this.GET_NEXT_STOP() != null) { // Runs while the bus is not at the destination.
            CURRENT_PASSENGERS = new Random().nextInt(MAX_CAPACITY + 1); // Generates a random amount of passengers. Simulating the entry and exit of passengers.
            System.out.println("\n============================================================\n" + this.getName() + ":\n" + ORIGIN + " --> " + DESTINATION + "\nCurrent Stop: " + CURRENT_STOP + "\nCurrent Passengers: " + CURRENT_PASSENGERS + "\n============================================================");
            IntStream.range(0, TRAVEL_DELAY).forEach(i -> { // Simulates the delay between two stops. The delay is defined by the TRAVEL_DELAY variable. The delay is simulated by sleeping for a certain amount of time. That amount is 1 millisecond at a time until the delay is over, so that the malfunction feature can be activated during the bus' travel.
                try {
                    if (IS_PAUSED && this.getState() != State.TIMED_WAITING) { // If a malfunction has occurred and the bus is not waiting for the malfunction to be resolved, the bus will wait for the malfunction to be resolved.
                        sleep(PAUSE_DURATION); // The bus will wait for the malfunction to be resolved, for the duration of the maintainance dela, previously set in the PAUSE method.
                        IS_PAUSED = false; // The bus will be unpaused, after the maintainance delay.
                        System.out.println("\n===========================================================" +
                                "\n            " + getName() + " IS BACK TO WORK." +
                                "\n===========================================================");
                    }
                    sleep(1); // Simulates the bus' travel between two stops. The delay is 1 millisecond at a time.
                } catch (
                        InterruptedException e) { // Catches the exception thrown by the sleep method, if such occurs. The exception will not happen do to the sleep method being called in a IntStream.range loop, one millisecond at a time. And, also, do to the this.getState() != State.TIMED_WAITING condition check in the malfunction activation condition.
                    e.printStackTrace();
                }
            });
            CURRENT_STOP = GET_NEXT_STOP(); // Sets the current stop to the next stop in the bus' route, after the bus has finished its travel between the two stops.
        }
        // If the bus has reached its destination, it will stop.
        System.out.println("\n============================================================\n" + this.getName() + ":\n" + ORIGIN + " --> " + DESTINATION + "\nCurrent Stop: " + CURRENT_STOP + "\nCurrent Passengers: " + CURRENT_PASSENGERS + "\n============================================================");
        System.out.println("\n============================================================\n" + this.getName() + " has arrived to " + DESTINATION + "." + "\n============================================================");
        CURRENT_PASSENGERS = 0; // After the bus has stopped, it will reset the current amount of passengers, because all passengers must leave the bus at the final stop.
    }

    ///////////GETTERS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the bus' type.
     *
     * @return The bus' type.
     */
    public BUS_TYPE GET_TYPE() {
        return TYPE;
    }

    /**
     * Returns true if the bus has stopped do to a malfunction, false otherwise.
     *
     * @return True if the bus has stopped do to a malfunction, false otherwise.
     */
    public boolean IS_PAUSED() {
        return IS_PAUSED;
    }

    ///////////METHODS///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the next stop of the bus' route.
     *
     * @return The next stop of the bus' route.
     */
    public BUS_STOP GET_NEXT_STOP() {
        BUS_STOP[] STOPS = BUS_STOP.values(); // Gets all the stops of the bus' route, in an array, from south to north.
        BUS_STOP[] STOPS_EXPRESS = Arrays.stream(BUS_STOP.values()).filter(j -> j != BUS_STOP.CASCAIS && j != BUS_STOP.COIMBRA).toArray(BUS_STOP[]::new); // Gets all the stops of the bus' route, in an array, from south to north, except for the two stops of the bus' route that are not in the express bus' route.
        return CURRENT_STOP == DESTINATION ? null : TYPE == BUS_TYPE.EXPRESS ? IntStream.range(0, STOPS_EXPRESS.length).filter(i -> ORIGIN == STOPS_EXPRESS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS_EXPRESS.length).filter(i -> DESTINATION == STOPS_EXPRESS[i]).findFirst().getAsInt() ? STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() + 1] : STOPS_EXPRESS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() - 1] : IntStream.range(0, STOPS.length).filter(i -> ORIGIN == STOPS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS.length).filter(i -> DESTINATION == STOPS[i]).findFirst().getAsInt() ? STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() + 1] : STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() - 1];
        // If the current stop is the destination, the bus has arrived to the destination.
        // If not it uses the current stop to find the next stop of the bus' route, deppending whether the bus is an express bus or not. If the bus is an express bus, it uses the stops of the express bus' route, otherwise it uses the stops of the regular bus' route.
        // After determining the if the bus is an express bus or not, it calculates if the bus is going from south to north or from north to south.
        // If the bus is going from south to north, it moves through the array from its front to its back, and if the bus is going from north to south, it moves through the array from its back to its front.
        // After determining the next stop of the bus' route, it returns it.
    }

    /**
     * Pauses the bus for a certain amount of time.
     *
     * @param DURATION Duration of the pause.
     */
    public void PAUSE(final int DURATION) {
        IS_PAUSED = true; // Indicates that the bus has stopped do to a malfunction.
        PAUSE_DURATION = DURATION; // Sets the delay until the malfunction is resolved.
    }
}