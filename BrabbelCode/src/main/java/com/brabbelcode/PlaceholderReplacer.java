package com.brabbelcode;

/**
 * Created by Chris on 26.11.13.
 */
public class PlaceholderReplacer {

    private String commandToModify;
    private boolean hasStringToModify;
    private boolean isClass;
    private static PlaceholderReplacer instance = null;

    /**
     * Private constructor because class follows Singleton pattern
     */
    private PlaceholderReplacer() {
    }

    /**
     * Returns the single instance of this class
     * @return Instance
     */
    public static PlaceholderReplacer getInstance() {
        if (PlaceholderReplacer.instance == null) {
            PlaceholderReplacer.instance = new PlaceholderReplacer();
        }
        return PlaceholderReplacer.instance;
    }

    /**
     * Modifies a string that contains placeholders so that the final result does not anymore
     * @param results
     * @return Result without placeholders
     */
    public String replacePlaceholders(String[] results) {
        String convertedResult = "";
        if (isClass) {
            convertedResult = this.convertStringsToClassName(results);
        }
        else {
            convertedResult = this.convertToFunctionName(results);
        }
        this.commandToModify = this.commandToModify.replace("XPlaceholderX", convertedResult);
        return this.commandToModify;
    }

    /**
     * Set the command that should be modified
     * @param commandToModify
     */
    public void setCommandToModify(String commandToModify) {
        this.commandToModify = commandToModify;
        if (this.commandToModify.contains("class"))
            this.isClass = true;
    }

    /**
     * Gets the ready state if there was a command with placeholders before
     * @return
     */
    public boolean getReadystate() {
        return this.hasStringToModify;
    }

    /**
     * Sets the ready state if there was a command with placeholders before
     * @param readyState
     */
    public void setReadyState(boolean readyState) {
        this.hasStringToModify = readyState;
    }

    /**
     * Converts the recognized words to a class name
     * @param results
     * @return
     */
    private String convertStringsToClassName(String[] results) {
        String result = "";
        for (String s : results) {
            String temp = this.firstCharToUpperCase(s);
            result += temp;
        }
        return result;
    }

    /**
     * Converts the recognized words to a function or variable name
     * @param results
     * @return
     */
    private String convertToFunctionName(String[] results) {
        String result = "";
        result += results[0];
        for (int i = 1; i < results.length; i++) {
            String temp = this.firstCharToUpperCase(results[i]);
            result += temp;
        }
        return result;
    }

    /**
     * Converts the first char of a string to upper case
     * @param s
     * @return
     */
    private String firstCharToUpperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
