/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package models;

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
     * The bus' leftover capacity.
     */
    private int CurrentCapacity;
    /**
     * The bus' current amount passengers.
     */
    private int CurrentPassengers = 0;
    /**
     * The bus' current speed. (Delay in milliseconds it takes to travel between two stops.)
     * It is calculated based on the bus' type.
     * It is also the maximum speed of the bus.
     */
    private double Speed = 0.0;

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Initializes a new instance of the Bus class.
     *
     * @param bus_type    The new bus' type.
     * @param destination The new bus' destination.
     * @param origin      The new bus' origin city.
     */
    public Bus(final BusType bus_type, final City destination, final City origin) {
        Type = bus_type;
        Destination = destination;
        Origin = origin;
        MaxCapacity = switch (bus_type) {
            case Mini_Bus -> BusType.MAX_CAPACITY_MINI_BUS;
            case Convencional -> BusType.MAX_CAPACITY_CONVENCIONAL;
            case Long_Drive -> BusType.MAX_CAPACITY_LONG_DRIVE;
            case Expresso -> BusType.MAX_CAPACITY_EXPRESSO;
        };
        CurrentCapacity = MaxCapacity;
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
     *
     * @param currentPassengers The new bus' current amount passengers.
     */
    public void setCurrentPassengers(final int currentPassengers) throws IllegalArgumentException {
        if (currentPassengers < 0 || currentPassengers > MaxCapacity) {
            throw new IllegalArgumentException("The current amount passengers must be between 0 and " + this.getMaxCapacity() + ".");
        }
        this.CurrentPassengers = currentPassengers;
        this.CurrentCapacity = this.getMaxCapacity() - this.getCurrentPassengers();
        this.Speed = this.calculateCurrentSpeed();
    }

    /**
     * Returns the bus' type.
     *
     * @return The bus' type.
     */
    public BusType getType() {
        return this.Type;
    }

    /**
     * Returns the bus' maximum capacity.
     *
     * @return The bus' maximum capacity.
     */
    public int getMaxCapacity() {
        return this.MaxCapacity;
    }

    /**
     * Returns the bus' destination.
     *
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
    public double getSpeed() {
        return this.Speed;
    }

    /**
     * Calculates the bus' current speed.
     *
     * @return The bus' current speed.
     */
    private double calculateCurrentSpeed() {
        return switch (this.getType()) {
            case Mini_Bus ->
                    BusType.BASE_SPEED_MINI_BUS + 0.01 * (this.getCurrentPassengers() + 1) * BusType.BASE_SPEED_MINI_BUS;
            case Convencional ->
                    BusType.BASE_SPEED_CONVENCIONAL + 0.01 * (this.getCurrentPassengers() + 1) * BusType.BASE_SPEED_CONVENCIONAL;
            case Long_Drive ->
                    BusType.BASE_SPEED_LONG_DRIVE + 0.01 * (this.getCurrentPassengers() + 1) * BusType.BASE_SPEED_LONG_DRIVE;
            case Expresso ->
                    BusType.BASE_SPEED_EXPRESSO + 0.01 * (this.getCurrentPassengers() + 1) * BusType.BASE_SPEED_EXPRESSO;
        };
    }
}