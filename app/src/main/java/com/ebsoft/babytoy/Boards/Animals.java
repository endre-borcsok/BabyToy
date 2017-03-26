package com.ebsoft.babytoy.Boards;

import com.ebsoft.babytoy.R;

import java.util.ArrayList;

/**
 * Created by Endre on 26/03/2017.
 */

public class Animals {
    private ArrayList<BoardElement> mElements;

    public Animals() {
        BoardElement dog = new BoardElement();
        dog.imageID = R.drawable.dog;
        dog.soundID = R.raw.beep;

        mElements = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            mElements.add(dog);
        }
    }

    public ArrayList<BoardElement> getElements() {
        return mElements;
    }
}
