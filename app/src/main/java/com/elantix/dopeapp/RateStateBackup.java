package com.elantix.dopeapp;

/**
 * Created by oleh on 4/11/16.
 */
public class RateStateBackup {

    public ChoiceAnimationHelper.ChoiceSide chosenSide;
    public int leftRate;
    public Boolean directionInside;

    public RateStateBackup(ChoiceAnimationHelper.ChoiceSide chosenSide, int leftRate, Boolean directionInside) {
        this.chosenSide = chosenSide;
        this.leftRate = leftRate;
        this.directionInside = directionInside;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("side: "+chosenSide+"\nleft rate: "+leftRate+"\ndirectionInside: "+directionInside);
        return result.toString();
    }
}
