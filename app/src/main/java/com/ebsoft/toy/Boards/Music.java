package com.ebsoft.toy.Boards;

import com.ebsoft.toy.R;

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
        akkordeon.soundID = R.raw.akkordeon;

        BoardElement drums = new BoardElement();
        drums.imageID = R.drawable.drums;
        drums.soundID = R.raw.drums;

        BoardElement piano = new BoardElement();
        piano.imageID = R.drawable.piano;
        piano.soundID = R.raw.piano;

        BoardElement trumpet = new BoardElement();
        trumpet.imageID = R.drawable.trumpet;
        trumpet.soundID = R.raw.trumpet;

        BoardElement violin = new BoardElement();
        violin.imageID = R.drawable.violin;
        violin.soundID = R.raw.violin;

        BoardElement saxophone = new BoardElement();
        saxophone.imageID = R.drawable.saxophone;
        saxophone.soundID = R.raw.saxophone;

        BoardElement maraccas = new BoardElement();
        maraccas.imageID = R.drawable.maraccas;
        maraccas.soundID = R.raw.maraccas;

        BoardElement guitar = new BoardElement();
        guitar.imageID = R.drawable.guitar;
        guitar.soundID = R.raw.guitar;

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
