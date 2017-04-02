package com.ebsoft.babytoy.Boards;

import com.ebsoft.babytoy.R;

import java.util.ArrayList;

/**
 * Created by Endre on 26/03/2017.
 */

public class Animals extends Board{

    public static final String BOARD_ANIMALS = "Animals";

    public Animals() {
        mBoardName = BOARD_ANIMALS;

        BoardElement dog = new BoardElement();
        dog.imageID = R.drawable.dog;
        dog.soundID = R.raw.dog;

        BoardElement cat = new BoardElement();
        cat.imageID = R.drawable.cat;
        cat.soundID = R.raw.cat;

        BoardElement cow = new BoardElement();
        cow.imageID = R.drawable.cow;
        cow.soundID = R.raw.cow;

        BoardElement duck = new BoardElement();
        duck.imageID = R.drawable.duck;
        duck.soundID = R.raw.duck;

        BoardElement goat = new BoardElement();
        goat.imageID = R.drawable.goat;
        goat.soundID = R.raw.goat;

        BoardElement hen = new BoardElement();
        hen.imageID = R.drawable.hen;
        hen.soundID = R.raw.hen;

        BoardElement horse = new BoardElement();
        horse.imageID = R.drawable.horse;
        horse.soundID = R.raw.horse;

        BoardElement sheep = new BoardElement();
        sheep.imageID = R.drawable.sheep;
        sheep.soundID = R.raw.sheep;

        mElements = new ArrayList<>();
        mElements.add(dog);
        mElements.add(hen);
        mElements.add(sheep);
        mElements.add(cat);
        mElements.add(horse);
        mElements.add(goat);
        mElements.add(duck);
        mElements.add(cow);
    }
}
