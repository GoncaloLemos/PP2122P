/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package models;

public enum BusType {
    /**
     * The same as the conventional one, allowing the transport of only 24 passengers.
     */
    Mini_Bus,
    /**
     * It can carry up to a maximum of 51 passengers.
     */
    Convencional,
    /**
     * It can carry up to a maximum of 59 passengers.
     * It travels slower when compared to a conventional bus.
     */
    Long_Drive,
    /**
     * Same as conventional, but can carry up to a maximum of 69 passengers.
     * It only stops in the cities of Lisbon, Porto and Braga.
     */
    Expresso;

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int MAX_CAPACITY_MINI_BUS = 24;
    public static final int MAX_CAPACITY_CONVENCIONAL = 51;
    public static final int MAX_CAPACITY_LONG_DRIVE = 59;
    public static final int MAX_CAPACITY_EXPRESSO = 69;

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int BASE_SPEED_MINI_BUS = 50000;
    public static final int BASE_SPEED_CONVENCIONAL = 100000;
    public static final int BASE_SPEED_LONG_DRIVE = 1200000;
    public static final int BASE_SPEED_EXPRESSO = 150000;
}