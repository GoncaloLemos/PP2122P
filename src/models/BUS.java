/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package models;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
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

    public BUS(final String THREAD_NAME, final BUS_TYPE BUS_TYPE, final CITY DESTINATION, final CITY ORIGIN) {
        JSONObject JSON = null;
        try {
            JSON = (JSONObject) new JSONParser().parse(new FileReader("./src/config.json"));
        } catch (IOException e) {
            System.out.println("Error reading config.json.");
            System.exit(1);
        } catch (ParseException e) {
            System.out.println("Error parsing config.json.");
            System.exit(1);
        }
        TYPE = BUS_TYPE;
        this.DESTINATION = DESTINATION;
        this.ORIGIN = ORIGIN;
        MAX_CAPACITY = switch (BUS_TYPE) {
            case MINI_BUS ->
                    JSON.get("MAX_CAPACITY_MINI_BUS") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_MINI_BUS")).intValue() : (int) JSON.get("MAX_CAPACITY_MINI_BUS");
            case CONVENTIONAL ->
                    JSON.get("MAX_CAPACITY_CONVENCIONAL") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_CONVENCIONAL")).intValue() : (int) JSON.get("MAX_CAPACITY_CONVENCIONAL");
            case LONG_DRIVE ->
                    JSON.get("MAX_CAPACITY_LONG_DRIVE") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_LONG_DRIVE")).intValue() : (int) JSON.get("MAX_CAPACITY_LONG_DRIVE");
            case EXPRESS ->
                    JSON.get("MAX_CAPACITY_EXPRESSO") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_EXPRESSO")).intValue() : (int) JSON.get("MAX_CAPACITY_EXPRESSO");
        };
        CURRENT_STOP = this.ORIGIN;
        SPEED = switch (BUS_TYPE) {
            case MINI_BUS ->
                    JSON.get("BASE_SPEED_MINI_BUS") instanceof Long ? ((Long) JSON.get("BASE_SPEED_MINI_BUS")).intValue() : (int) JSON.get("BASE_SPEED_MINI_BUS");
            case CONVENTIONAL ->
                    JSON.get("BASE_SPEED_CONVENCIONAL") instanceof Long ? ((Long) JSON.get("BASE_SPEED_CONVENCIONAL")).intValue() : (int) JSON.get("BASE_SPEED_CONVENCIONAL");
            case LONG_DRIVE ->
                    JSON.get("BASE_SPEED_LONG_DRIVE") instanceof Long ? ((Long) JSON.get("BASE_SPEED_LONG_DRIVE")).intValue() : (int) JSON.get("BASE_SPEED_LONG_DRIVE");
            case EXPRESS ->
                    JSON.get("BASE_SPEED_EXPRESSO") instanceof Long ? ((Long) JSON.get("BASE_SPEED_EXPRESSO")).intValue() : (int) JSON.get("BASE_SPEED_EXPRESSO");
        };
        this.setPriority(Thread.NORM_PRIORITY);
        this.setName(THREAD_NAME);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void run() {
        super.run();
        try {
            while (this.GET_NEXT_STOP() != null) {
                System.out.println("\n============================================================\n" +
                        this.getName() + ":" +
                        "\nOrigin: " + ORIGIN +
                        "\nDestination: " + DESTINATION +
                        "\nCurrent Stop: " + this.GET_NEXT_STOP().name() +
                        "\nCurrent Passengers: " + CURRENT_PASSENGERS +
                        "\nLeftover Seats: " + (MAX_CAPACITY - CURRENT_PASSENGERS) +
                        "\n============================================================");
                CURRENT_PASSENGERS = new Random().nextInt(MAX_CAPACITY + 1);
                Thread.sleep(SPEED);
                CURRENT_STOP = GET_NEXT_STOP();
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("\n============================================================\n" +
                    this.getName() + " has arrived to " + DESTINATION + "." +
                    "\n============================================================");
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
                    IntStream.range(0, STOPS_EXPRESS.length).filter(i -> ORIGIN == STOPS_EXPRESS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS_EXPRESS.length).filter(i -> DESTINATION == STOPS_EXPRESS[i]).findFirst().getAsInt() ? STOPS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() + 1] : STOPS[IntStream.range(0, STOPS_EXPRESS.length).filter(i -> CURRENT_STOP == STOPS_EXPRESS[i]).findFirst().getAsInt() - 1];
            default ->
                    IntStream.range(0, STOPS.length).filter(i -> ORIGIN == STOPS[i]).findFirst().getAsInt() < IntStream.range(0, STOPS.length).filter(i -> DESTINATION == STOPS[i]).findFirst().getAsInt() ? STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() + 1] : STOPS[IntStream.range(0, STOPS.length).filter(i -> CURRENT_STOP == STOPS[i]).findFirst().getAsInt() - 1];
        };
    }
}