package io.orbit.api;

import javafx.scene.input.KeyEvent;

/**
 * Created by Tyler Swann on Wednesday February 21, 2018 at 14:50
 */
@Deprecated
public class CharacterPair
{
    public final char leftChar;
    public final char rightChar;
    public final String left;
    public final String right;
    public final boolean isEmpty;
    public static final CharacterPair EMPTY = new CharacterPair('\u0000', '\u0000');

    public CharacterPair(char left, char right)
    {
        this.left = String.valueOf(left);
        this.right = String.valueOf(right);
        this.leftChar = left;
        this.rightChar = right;
        this.isEmpty = left == '\u0000' && right == '\u0000';
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", left, right);
    }

    public static String keyEventText(KeyEvent event)
    {
        String key = event.getText();
        if (!event.isShiftDown())
            return event.getText();
        switch (key)
        {
            case "a": return "A";
            case "b": return "B";
            case "c": return "C";
            case "d": return "D";
            case "e": return "E";
            case "f": return "F";
            case "g": return "G";
            case "h": return "H";
            case "i": return "I";
            case "j": return "J";
            case "k": return "K";
            case "l": return "L";
            case "m": return "M";
            case "n": return "N";
            case "o": return "O";
            case "p": return "P";
            case "q": return "Q";
            case "r": return "R";
            case "s": return "S";
            case "t": return "T";
            case "u": return "U";
            case "v": return "V";
            case "w": return "W";
            case "x": return "X";
            case "y": return "Y";
            case "z": return "Z";
            case "0": return ")";
            case "1": return "!";
            case "2": return "@";
            case "3": return "#";
            case "4": return "$";
            case "5": return "%";
            case "6": return "^";
            case "7": return "&";
            case "8": return "*";
            case "9": return "(";
            case "`": return "~";
            case "-": return "_";
            case "=": return "+";
            case "[": return "{";
            case "]": return "}";
            case ";": return ":";
            case "\'": return "\"";
            case ",": return "<";
            case ".": return ">";
            case "/": return "?";
            case "\\": return "|";
        }
        return "null";
    }
}
