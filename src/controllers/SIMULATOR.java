/*
 * Copyright © 2022, Pedro S.. All rights reserved.
 */

package controllers;

import models.BUS;
import models.BUS_TYPE;
import models.CITY;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class SIMULATOR {
    public SIMULATOR() {
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
        int NUMBER_OF_MINI_BUS = JSON.get("NUMBER_OF_MINI_BUS") instanceof Long ? ((Long) JSON.get("NUMBER_OF_MINI_BUS")).intValue() : (int) JSON.get("NUMBER_OF_MINI_BUS");
        int NUMBER_OF_CONVENCIONAL = JSON.get("NUMBER_OF_CONVENCIONAL") instanceof Long ? ((Long) JSON.get("NUMBER_OF_CONVENCIONAL")).intValue() : (int) JSON.get("NUMBER_OF_CONVENCIONAL");
        int NUMBER_OF_LONG_DRIVE = JSON.get("NUMBER_OF_LONG_DRIVE") instanceof Long ? ((Long) JSON.get("NUMBER_OF_LONG_DRIVE")).intValue() : (int) JSON.get("NUMBER_OF_LONG_DRIVE");
        int NUMBER_OF_EXPRESSO = JSON.get("NUMBER_OF_EXPRESSO") instanceof Long ? ((Long) JSON.get("NUMBER_OF_EXPRESSO")).intValue() : (int) JSON.get("NUMBER_OF_EXPRESSO");
        for (int i = 0; i < NUMBER_OF_MINI_BUS; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("Bus Mini_Bus #" + i + 1, BUS_TYPE.MINI_BUS, ORIGIN, DESTINATION);
            BUS_THREAD.start();
        }
        for (int i = 0; i < NUMBER_OF_CONVENCIONAL; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("Bus Convencional #" + i + 1, BUS_TYPE.CONVENTIONAL, ORIGIN, DESTINATION);
            BUS_THREAD.start();
        }
        for (int i = 0; i < NUMBER_OF_LONG_DRIVE; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(CITY.values().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(CITY.values().length)];
            }
            BUS BUS_THREAD = new BUS("Bus Long_Drive #" + i + 1, BUS_TYPE.LONG_DRIVE, ORIGIN, DESTINATION);
            BUS_THREAD.start();
        }
        for (int i = 0; i < NUMBER_OF_EXPRESSO; i++) {
            CITY ORIGIN = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            CITY DESTINATION = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            while (DESTINATION == ORIGIN) {
                DESTINATION = CITY.values()[new Random().nextInt(Arrays.stream(CITY.values()).filter(j -> j == CITY.CASCAIS || j == CITY.COIMBRA).toArray().length)];
            }
            BUS BUS_THREAD = new BUS("Bus Expresso #" + i + 1, BUS_TYPE.EXPRESS, ORIGIN, DESTINATION);
            BUS_THREAD.start();
        }
    }
}
