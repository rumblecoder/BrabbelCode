package com.brabbelcode;

/**
 * Created by Chris on 26.11.13.
 */
public class PlaceholderReplacer {

    private String commandToModify;
    private boolean hasStringToModify;
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
     * @param translateResults
     * @return Result without placeholders
     */
    public String replacePlaceholders(String translateResults) {
        this.commandToModify.replace("XPlaceholderX", translateResults);
        return this.commandToModify;
    }

    /**
     * Set the command that should be modified
     * @param commandToModify
     */
    public void setCommandToModify(String commandToModify) {
        this.commandToModify = commandToModify;
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
}
