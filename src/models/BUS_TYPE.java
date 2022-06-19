/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package models;

// Enumeration used to represent the diferent types of buses.
public enum BUS_TYPE {
    //The same as the conventional one, allowing the transport of only 24 passengers.
    MINI_BUS,
    //It can carry up to a maximum of 51 passengers.
    CONVENTIONAL,
    //It can carry up to a maximum of 59 passengers.
    // It travels slower when compared to a conventional bus.
    LONG_DRIVE,
    //Same as conventional, but can carry up to a maximum of 69 passengers.
    // It only stops in the cities of Lisbon, Porto and Braga.
    EXPRESS
}