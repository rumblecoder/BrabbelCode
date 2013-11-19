package com.brabbelcode;
import java.util.*;

/**
 * Created by andypf on 12.11.13.
 */
public class Macros {
    private Hashtable<String,String>  createHashtable;
    private Hashtable<String,String> selectionHashtable;
    private Hashtable<String, String> deleteHashtable;

    public Macros()
    {
        createHashtable = new Hashtable<String,String>();
        createHashtable.put("class", "public class XPlaceholderX{\n\n\tpublic XPlaceholderX(){\n\t}\n\n}");
        createHashtable.put("function", "function XPlaceholderX(){\n\n}");
        createHashtable.put("integer variable", "int XPlaceholderX;");
        createHashtable.put("string variable", "string XPlaceholderX;");
        createHashtable.put("double variable", "double XPlaceholderX;");
        createHashtable.put("next line", "line break");
        createHashtable.put("new line","line break");
        createHashtable.put("exit mode", "leave current mode");
        createHashtable.put("leave mode", "leave current mode");


        selectionHashtable = new Hashtable<String,String>();
        selectionHashtable.put("all","all");
        selectionHashtable.put("none","none");


        deleteHashtable = new Hashtable<String, String>();
        deleteHashtable.put("all","all");
    }

    public Hashtable<String,String> getWholeCommandList()
    {
        Hashtable<String,String> commandHashtable = new Hashtable<String,String>();
        commandHashtable.putAll(deleteHashtable);
        commandHashtable.putAll(selectionHashtable);
        commandHashtable.putAll(createHashtable);
        return commandHashtable;
    }

    public Hashtable<String,String> getCreateHashtable()
    {
        return createHashtable;
    }
    public Hashtable<String,String> getSelectionHashtable()
    {
        return selectionHashtable;
    }
    public Hashtable<String,String> getDeleteHashtable()
    {
        return deleteHashtable;
    }


}
