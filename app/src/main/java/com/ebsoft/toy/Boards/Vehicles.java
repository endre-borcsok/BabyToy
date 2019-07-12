package com.ebsoft.toy.Boards;

import com.ebsoft.toy.R;

import java.util.ArrayList;

/**
 * Created by Endre on 15/04/2017.
 */

public class Vehicles extends Board {
    public static final String BOARD_VEHICLES = "Vehicles";

    public Vehicles() {
        mBoardName = BOARD_VEHICLES;

        BoardElement ship = new BoardElement();
        ship.imageID = R.drawable.ship;
        ship.soundID = R.raw.ship;

        BoardElement car = new BoardElement();
        car.imageID = R.drawable.car;
        car.soundID = R.raw.car;

        BoardElement bycicle = new BoardElement();
        bycicle.imageID = R.drawable.bike;
        bycicle.soundID = R.raw.bell;

        BoardElement helicopter = new BoardElement();
        helicopter.imageID = R.drawable.helicopter;
        helicopter.soundID = R.raw.helicopter;

        BoardElement train = new BoardElement();
        train.imageID = R.drawable.train;
        train.soundID = R.raw.train;

        BoardElement submarine = new BoardElement();
        submarine.imageID = R.drawable.submarine;
        submarine.soundID = R.raw.submarine;

        BoardElement plane = new BoardElement();
        plane.imageID = R.drawable.plane;
        plane.soundID = R.raw.plane;

        BoardElement motor = new BoardElement();
        motor.imageID = R.drawable.motor;
        motor.soundID = R.raw.motor;

        mElements = new ArrayList<>();
        mElements.add(ship);
        mElements.add(car);
        mElements.add(plane);
        mElements.add(train);
        mElements.add(submarine);
        mElements.add(bycicle);
        mElements.add(helicopter);
        mElements.add(motor);
    }
}
