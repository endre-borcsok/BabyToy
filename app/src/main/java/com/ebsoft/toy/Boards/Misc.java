package com.ebsoft.toy.Boards;

import com.ebsoft.toy.R;

import java.util.ArrayList;

/**
 * Created by Endre on 22/04/2017.
 */

public class Misc extends Board {
    public static final String BOARD_MISC = "Misc";

    public Misc() {
        mBoardName = BOARD_MISC;

        BoardElement bell = new BoardElement();
        bell.imageID = R.drawable.bell;
        bell.soundID = R.raw.bellbell;

        BoardElement mobile = new BoardElement();
        mobile.imageID = R.drawable.phone;
        mobile.soundID = R.raw.dialing;

        BoardElement glass = new BoardElement();
        glass.imageID = R.drawable.glass;
        glass.soundID = R.raw.clink;

        BoardElement dice = new BoardElement();
        dice.imageID = R.drawable.dice;
        dice.soundID = R.raw.dice;

        BoardElement oldPhone = new BoardElement();
        oldPhone.imageID = R.drawable.oldphone;
        oldPhone.soundID = R.raw.ring;

        BoardElement keyboard = new BoardElement();
        keyboard.imageID = R.drawable.keyboard;
        keyboard.soundID = R.raw.typing;

        BoardElement clap = new BoardElement();
        clap.imageID = R.drawable.clap;
        clap.soundID = R.raw.applause;

        BoardElement whistle = new BoardElement();
        whistle.imageID = R.drawable.whistle;
        whistle.soundID = R.raw.whistle;

        mElements = new ArrayList<>();
        mElements.add(mobile);
        mElements.add(whistle);
        mElements.add(glass);
        mElements.add(keyboard);
        mElements.add(oldPhone);
        mElements.add(dice);
        mElements.add(bell);
        mElements.add(clap);
    }
}
