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

public class Bus extends Thread {
    /**
     * The bus’ maximum capacity.
     */
    private final int MaxCapacity;
    /**
     * The bus' type.
     */
    private final BusType Type;
    /**
     * The bus' destination.
     */
    private final City Destination;
    /**
     * The bus' origin city.
     */
    private final City Origin;
    /**
     * The bus' current speed. (Delay in milliseconds it takes to travel between two stops.)
     * It is calculated based on the bus' type.
     * It is also the maximum speed of the bus.
     */
    private final int Speed;
    /**
     * The bus' current stop.
     */
    private City CurrentStop;
    /**
     * The bus' current amount passengers.
     */
    private int CurrentPassengers = 0;
    /**
     * The bus' leftover capacity.
     */
    private int CurrentCapacity;

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initializes a new instance of the Bus class.
     *
     * @param name        The bus' name.
     * @param bus_type    The new bus' type.
     * @param destination The new bus' destination.
     * @param origin      The new bus' origin city.
     */
    public Bus(final String name, final BusType bus_type, final City destination, final City origin) {
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
        Type = bus_type;
        Destination = destination;
        Origin = origin;
        MaxCapacity = switch (bus_type) {
            case Mini_Bus ->
                    JSON.get("MAX_CAPACITY_MINI_BUS") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_MINI_BUS")).intValue() : (int) JSON.get("MAX_CAPACITY_MINI_BUS");
            case Convencional ->
                    JSON.get("MAX_CAPACITY_CONVENCIONAL") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_CONVENCIONAL")).intValue() : (int) JSON.get("MAX_CAPACITY_CONVENCIONAL");
            case Long_Drive ->
                    JSON.get("MAX_CAPACITY_LONG_DRIVE") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_LONG_DRIVE")).intValue() : (int) JSON.get("MAX_CAPACITY_LONG_DRIVE");
            case Expresso ->
                    JSON.get("MAX_CAPACITY_EXPRESSO") instanceof Long ? ((Long) JSON.get("MAX_CAPACITY_EXPRESSO")).intValue() : (int) JSON.get("MAX_CAPACITY_EXPRESSO");
        };
        CurrentCapacity = MaxCapacity;
        CurrentStop = Origin;
        Speed = switch (bus_type) {
            case Mini_Bus ->
                    JSON.get("BASE_SPEED_MINI_BUS") instanceof Long ? ((Long) JSON.get("BASE_SPEED_MINI_BUS")).intValue() : (int) JSON.get("BASE_SPEED_MINI_BUS");
            case Convencional ->
                    JSON.get("BASE_SPEED_CONVENCIONAL") instanceof Long ? ((Long) JSON.get("BASE_SPEED_CONVENCIONAL")).intValue() : (int) JSON.get("BASE_SPEED_CONVENCIONAL");
            case Long_Drive ->
                    JSON.get("BASE_SPEED_LONG_DRIVE") instanceof Long ? ((Long) JSON.get("BASE_SPEED_LONG_DRIVE")).intValue() : (int) JSON.get("BASE_SPEED_LONG_DRIVE");
            case Expresso ->
                    JSON.get("BASE_SPEED_EXPRESSO") instanceof Long ? ((Long) JSON.get("BASE_SPEED_EXPRESSO")).intValue() : (int) JSON.get("BASE_SPEED_EXPRESSO");
        };
        this.setPriority(Thread.NORM_PRIORITY);
        this.setName(name);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void run() {
        super.run();
        try {
            while (this.has_next_stop()) {
                System.out.println("\n============================================================\n" +
                        this.getName() + ", that started in " + this.getOrigin() + ", is going to " + this.get_next_stop().name() + "." +
                        "\nIt has " + this.getCurrentCapacity() + " seats left." +
                        "\nAnd it's carrying " + this.getCurrentPassengers() + " passengers." +
                        "\nIt's current delay between two stops is " + this.getSpeed() + "ms." +
                        "\n============================================================");
                this.setCurrentPassengers(new Random().nextInt(this.getMaxCapacity() + 1));
                Thread.sleep(this.getSpeed());
                this.setCurrentStop(this.get_next_stop());
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            this.setCurrentPassengers(0);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the bus' current amount passengers.
     *
     * @return The bus' current amount passengers.
     */
    public int getCurrentPassengers() {
        return this.CurrentPassengers;
    }
    /**
     * Sets the bus' current amount passengers.
     * @param currentPassengers The new bus' current amount passengers.
     */
    public void setCurrentPassengers(final int currentPassengers) throws IllegalArgumentException {
        if (currentPassengers < 0 || currentPassengers > MaxCapacity) {
            throw new IllegalArgumentException("The current amount passengers must be between 0 and " + this.getMaxCapacity() + ".");
        }
        this.CurrentPassengers = currentPassengers;
        this.CurrentCapacity = this.getMaxCapacity() - this.getCurrentPassengers();
    }
    /**
     * Returns the bus' type.
     * @return The bus' type.
     */
    public BusType getType() {
        return this.Type;
    }
    /**
     * Returns the bus' maximum capacity.
     * @return The bus' maximum capacity.
     */
    public int getMaxCapacity() {
        return this.MaxCapacity;
    }
    /**
     * Returns the bus' destination.
     * @return The bus' destination.
     */
    public City getDestination() {
        return this.Destination;
    }

    /**
     * Returns the bus' origin city.
     *
     * @return The bus' origin city.
     */
    public City getOrigin() {
        return this.Origin;
    }

    /**
     * Returns the bus' maximum speed.
     *
     * @return The bus' maximum speed.
     */
    public int getSpeed() {
        return this.Speed;
    }

    /**
     * Returns the bus' current stop.
     *
     * @return The bus' current stop.
     */
    public City getCurrentStop() {
        return CurrentStop;
    }

    /**
     * Sets the bus' current stop.
     *
     * @param currentStop The new bus' current stop.
     */
    public void setCurrentStop(final City currentStop) {
        CurrentStop = currentStop;
    }

    /**
     * Returns the bus' next stop.
     *
     * @return The bus' next stop.
     * @throws Exception If the bus' current stop is the last stop.
     */
    public City get_next_stop() throws Exception {
        if (CurrentStop == City.Cascais && Origin == City.Cascais && Type != BusType.Expresso) return City.Lisboa;
        else if (CurrentStop == City.Lisboa && Origin == City.Cascais && Type != BusType.Expresso) return City.Coimbra;
        else if (CurrentStop == City.Coimbra && Origin == City.Cascais && Type != BusType.Expresso) return City.Porto;
        else if (CurrentStop == City.Porto && Origin == City.Cascais && Type != BusType.Expresso) return City.Braga;
        else if (CurrentStop == City.Braga && Origin == City.Braga && Type != BusType.Expresso) return City.Porto;
        else if (CurrentStop == City.Porto && Origin == City.Braga && Type != BusType.Expresso) return City.Coimbra;
        else if (CurrentStop == City.Coimbra && Origin == City.Braga && Type != BusType.Expresso) return City.Lisboa;
        else if (CurrentStop == City.Lisboa && Origin == City.Braga && Type != BusType.Expresso) return City.Cascais;
        else if (CurrentStop == City.Lisboa && Origin == City.Lisboa && Type == BusType.Expresso) return City.Porto;
        else if (CurrentStop == City.Porto && Origin == City.Lisboa && Type == BusType.Expresso) return City.Braga;
        else if (CurrentStop == City.Braga && Origin == City.Braga && Type == BusType.Expresso) return City.Porto;
        else if (CurrentStop == City.Porto && Origin == City.Braga && Type == BusType.Expresso) return City.Lisboa;
        else throw new Exception("You have reached your destination. There are no more stops.");
    }

    /**
     * Returns true if it has a next stop, false otherwise.
     *
     * @return True if it has a next stop, false otherwise.
     */
    public boolean has_next_stop() {
        return ((((((((((CurrentStop == City.Cascais && Origin == City.Cascais && Type != BusType.Expresso || CurrentStop == City.Lisboa && Origin == City.Cascais && Type != BusType.Expresso) || CurrentStop == City.Coimbra && Origin == City.Cascais && Type != BusType.Expresso) || CurrentStop == City.Porto && Origin == City.Cascais && Type != BusType.Expresso) || CurrentStop == City.Braga && Origin == City.Braga && Type != BusType.Expresso) || CurrentStop == City.Porto && Origin == City.Braga && Type != BusType.Expresso) || CurrentStop == City.Coimbra && Origin == City.Braga && Type != BusType.Expresso) || CurrentStop == City.Lisboa && Origin == City.Braga && Type != BusType.Expresso) || CurrentStop == City.Lisboa && Origin == City.Lisboa && Type == BusType.Expresso) || CurrentStop == City.Porto && Origin == City.Lisboa && Type == BusType.Expresso) || CurrentStop == City.Braga && Origin == City.Braga && Type == BusType.Expresso) || CurrentStop == City.Porto && Origin == City.Braga && Type == BusType.Expresso;
    }

    /**
     * Returns the bus' leaftover capacity.
     *
     * @return The bus' leafover capacity.
     */
    public int getCurrentCapacity() {
        return CurrentCapacity;
    }

    /**
     * Returns true if the bus has reached its destination, false otherwise.
     *
     * @return True if the bus has reached its destination, false otherwise.
     */
    public boolean has_reached_destination() {
        return CurrentStop == Destination;
    }
}