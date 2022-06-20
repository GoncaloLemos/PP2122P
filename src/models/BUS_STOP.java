/*
 * Copyright © 2022, Pedro Simões & Gonçalo Lemos. All rights reserved.
 */

package models;

// Enumeration used to represent the diferent stops a bus can make.
// The declaration order of each stop is important because it matches the order in which the stops are visited by the bus.
// If a new stop is declared the program will support such addition.
public enum BUS_STOP {
    CASCAIS,
    LISBOA,
    COIMBRA,
    PORTO,
    BRAGA
}