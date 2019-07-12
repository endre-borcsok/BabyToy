package com.ebsoft.toy.Boards;

import com.ebsoft.toy.R;

import java.util.ArrayList;

/**
 * Created by Endre on 02/04/2017.
 */

public class Jungle extends Board{

    public static final String BOARD_JUNGLE = "Jungle";

    public Jungle() {
        mBoardName = BOARD_JUNGLE;

        BoardElement lion = new BoardElement();
        lion.imageID = R.drawable.lion;
        lion.soundID = R.raw.dog;

        BoardElement monkey = new BoardElement();
        monkey.imageID = R.drawable.monkey;
        monkey.soundID = R.raw.cat;

        BoardElement zebra = new BoardElement();
        zebra.imageID = R.drawable.zebra;
        zebra.soundID = R.raw.cow;

        BoardElement snake = new BoardElement();
        snake.imageID = R.drawable.snake;
        snake.soundID = R.raw.duck;

        BoardElement parrot = new BoardElement();
        parrot.imageID = R.drawable.parrot;
        parrot.soundID = R.raw.goat;

        BoardElement frog = new BoardElement();
        frog.imageID = R.drawable.frog;
        frog.soundID = R.raw.hen;

        BoardElement elephant = new BoardElement();
        elephant.imageID = R.drawable.elephant;
        elephant.soundID = R.raw.horse;

        BoardElement giraffe = new BoardElement();
        giraffe.imageID = R.drawable.giraffe;
        giraffe.soundID = R.raw.sheep;

        mElements = new ArrayList<>();
        mElements.add(lion);
        mElements.add(monkey);
        mElements.add(zebra);
        mElements.add(giraffe);
        mElements.add(frog);
        mElements.add(parrot);
        mElements.add(snake);
        mElements.add(elephant);
    }
}
