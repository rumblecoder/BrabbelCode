package com.brabbelcode;

import android.widget.EditText;

public class PlaceholderReplacer {

    private String commandToModify;
    private boolean hasStringToModify;
    private boolean isClass;
    private boolean accessorIsSelected;
    private boolean returnValueIsSelected;
    private boolean lastPlaceholderIsSelected;
    private SelectionHandler selectionHandler;
    private EditText textView;
    private static PlaceholderReplacer instance = null;

    /**
     * Private constructor because class follows Singleton pattern
     */
    private PlaceholderReplacer() {
        this.selectionHandler = SelectionHandler.getInstance();
    }

    /**
     * Initializes a new replacer with a frequent textBox
     * @param textBox The tesxtBoxthat is used
     */
    public void init(EditText textBox) {
        this.textView = textBox;
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
     * Set the command that should be modified
     * @param commandToModify the given command that contains placeholders
     */
    public void setCommandToModify(String commandToModify) {
        this.commandToModify = commandToModify;
        if (this.commandToModify.contains("class"))
            this.isClass = true;
    }

    /**
     * Gets the ready state if there was a command with placeholders before
     * @return If the replacer is ready to work
     */
    public boolean getReadystate() {
        return this.hasStringToModify;
    }

    /**
     * Sets the ready state if there was a command with placeholders before
     * @param readyState Sets if the replacer should be ready to work
     */
    public void setReadyState(boolean readyState) {
        this.hasStringToModify = readyState;
    }

    /**
     * Sets the replacer ready for replacing an accessor
     * @param state Sets if the the current selection is an accessor or not
     */
    public void setAccessorIsSelected(boolean state) {
        this.accessorIsSelected = state;
    }

    /**
     * Gets the information if the replacer expects an accessor or not.
     *
     * @return Returns if the current selection is an accessor or not
     */
    public boolean getAccessorIsSelected() {
        return this.accessorIsSelected;
    }

    /**
     * Sets the state if a return value is selected
     *
     * @param state Sets if the the current selection is a return value or not
     */
    public void setReturnValueIsSelected(boolean state) {
        this.returnValueIsSelected = state;
    }

    /**
     * Gets the state if a return value is selected
     *
     * @return Returns if the current selection is a return value or not
     */
    public boolean getReturnValueIsSelected() {
        return this.returnValueIsSelected;
    }

    /**
     * Sets the state if the last placeholder is selected
     *
     * @param state Sets if the the current selection is the last placeholder of a command
     */
    public void setLastPlaceholderIsSelected(boolean state) {
        this.lastPlaceholderIsSelected = state;
    }

    /**
     * Gets the state if the last placeholder is selected
     *
     * @return Returns if the the current selection is the last placeholder of a command
     */
    public boolean getLastPlaceholderIsSelected() {
        return this.lastPlaceholderIsSelected;
    }

    /**
     * Replaces given accssors and placeholders
     *
     * @param results The results of the voice recognition
     */
    public void replace(String[] results) {
        /*
        String replacement = "";
        for (String s : results) {
            replacement += s;
        }
        int start = SelectionHandler.getInstance().getStartIndex();
        int end = SelectionHandler.getInstance().getEndIndex();
        this.textView.getText().replace(start, end, replacement + " "); */
        if (this.accessorIsSelected) {
            this.replaceAccessor(results);
        } else if (this.returnValueIsSelected) {
            this.replaceReturnValue(results);
        } else this.replacePlaceholders(results);
    }

    /**
     * Replaces the selected return value
     *
     * @param results The results of the voice recognition
     */
    private void replaceReturnValue(String[] results) {
        String convertedResult = results[0];
        this.replaceSelection(convertedResult);
    }

    /**
     * Converts the recognized words to a class name
     * @param results The results of the voice recognition
     * @return The result in form of a proper class name
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
     * @param results The results of the voice recognition
     * @return The result in form of a proper function name
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
     * @param s The String that should be converted to have a first capital letter
     * @return The same String with a capital letter in front
     */
    private String firstCharToUpperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * Modifies a string that contains placeholders so that the final result does not anymore
     *
     * @param results The results of the voice recognition
     */
    private void replacePlaceholders(String[] results) {
        String convertedResult = "";
        if (isClass) {
            convertedResult = this.convertStringsToClassName(results);
        } else {
            convertedResult = this.convertToFunctionName(results);
        }
        if (this.commandToModify.endsWith(" ;")) {
            SelectionHandler.getInstance().setEndIndex(SelectionHandler.getInstance().getEndIndex() + 1);
            convertedResult += "; \n";
        }
        this.replaceSelection(convertedResult);
    }

    /**
     * Replaces accessors that might be inherent in the given array
     *
     * @param results The results of the voice recognition
     */
    private void replaceAccessor(String[] results) {
        String convertedResult = results[0];
        this.replaceSelection("\n" + convertedResult);
    }

    /**
     * Replaces the placeholders in the last given command (Author: Chris)
     *
     * @param replacement The proper String that should be inserted instead of the placeholder
     */
    public void replaceSelection(String replacement) {
        int start = SelectionHandler.getInstance().getStartIndex();
        int end = SelectionHandler.getInstance().getEndIndex();
        this.textView.getText().replace(start, end, replacement + " ");
        if (this.isClass && !this.accessorIsSelected && !this.returnValueIsSelected && this.selectionHandler.getInstance().selectWord("XPlaceholderX")) {
            this.replaceSelection(replacement);
        }
    }
}
