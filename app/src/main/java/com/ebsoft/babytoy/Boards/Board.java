package com.ebsoft.babytoy.Boards;

import java.util.ArrayList;

/**
 * Created by Endre on 01/04/2017.
 */

public class Board {

    protected String mBoardName;
    protected ArrayList<BoardElement> mElements;

    public ArrayList<BoardElement> getElements() {
        return mElements;
    }
    public String getBoardName() {return mBoardName;}
}
