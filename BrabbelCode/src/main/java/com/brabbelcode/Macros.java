package com.brabbelcode;

import java.util.Hashtable;

public class Macros {

    private Hashtable<String,String> createHashtable;
    private Hashtable<String,String> selectionHashtable;
    private Hashtable<String, String> deleteHashtable;

    public Macros(){
        createHashtable = new Hashtable<String,String>();
        createHashtable.put("class", "XAccessorX class XPlaceholderX { \n \n \t XAccessorX XPlaceholderX () { \n XLastPlaceholderX \t } \n \n }");
        createHashtable.put("function", "XAccessorX XReturnValueX XPlaceholderX () { \n XLastPlaceholderX \n return null; \n }");
        createHashtable.put("integer", "XAccessorX int XPlaceholderX ;");
        createHashtable.put("string", "XAccessorX String XPlaceholderX ;");
        createHashtable.put("double", "XAccessorX double XPlaceholderX ;");
        createHashtable.put("new line","");
        createHashtable.put("comment", "comment");

        selectionHashtable = new Hashtable<String,String>();
        selectionHashtable.put("all","all");
        selectionHashtable.put("none","none" );
        selectionHashtable.put("line","line");
        selectionHashtable.put("word","word");
        selectionHashtable.put("next","next");

        deleteHashtable = new Hashtable<String, String>();
        deleteHashtable.put("all","all");
        deleteHashtable.put("selection","selection");
    }

    public Hashtable<String,String> getWholeCommandList(){
        Hashtable<String,String> commandHashtable = new Hashtable<String,String>();
        commandHashtable.putAll(deleteHashtable);
        commandHashtable.putAll(selectionHashtable);
        commandHashtable.putAll(createHashtable);
        return commandHashtable;
    }

    public Hashtable<String,String> getCreateHashtable(){
        return createHashtable;
    }

    public Hashtable<String,String> getSelectionHashtable(){
        return selectionHashtable;
    }

    public Hashtable<String,String> getDeleteHashtable(){
        return deleteHashtable;
    }
}
