package com.ebsoft.babytoy.Boards;

import com.ebsoft.babytoy.R;

import java.util.ArrayList;

/**
 * Created by Endre on 02/04/2017.
 */

public class Music extends Board{

    public static final String BOARD_INSTRUMENTS = "Music";

    public Music() {
        mBoardName = BOARD_INSTRUMENTS;

        BoardElement akkordeon = new BoardElement();
        akkordeon.imageID = R.drawable.akkordeon;
        akkordeon.soundID = R.raw.dog;

        BoardElement drums = new BoardElement();
        drums.imageID = R.drawable.drums;
        drums.soundID = R.raw.cat;

        BoardElement piano = new BoardElement();
        piano.imageID = R.drawable.piano;
        piano.soundID = R.raw.cow;

        BoardElement trumpet = new BoardElement();
        trumpet.imageID = R.drawable.trumpet;
        trumpet.soundID = R.raw.duck;

        BoardElement violin = new BoardElement();
        violin.imageID = R.drawable.violin;
        violin.soundID = R.raw.goat;

        BoardElement saxophone = new BoardElement();
        saxophone.imageID = R.drawable.saxophone;
        saxophone.soundID = R.raw.hen;

        BoardElement maraccas = new BoardElement();
        maraccas.imageID = R.drawable.maraccas;
        maraccas.soundID = R.raw.horse;

        BoardElement guitar = new BoardElement();
        guitar.imageID = R.drawable.guitar;
        guitar.soundID = R.raw.sheep;

        mElements = new ArrayList<>();
        mElements.add(akkordeon);
        mElements.add(trumpet);
        mElements.add(maraccas);
        mElements.add(piano);
        mElements.add(guitar);
        mElements.add(saxophone);
        mElements.add(drums);
        mElements.add(violin);
    }
}
