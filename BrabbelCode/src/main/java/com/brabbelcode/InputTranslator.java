package com.brabbelcode;

import java.util.Hashtable;

public class InputTranslator {

    private double tolerance;
    private Macros macros;
    private boolean easyMode;

    public InputTranslator(boolean useTolerance){
        this.tolerance = 0.6;
        this.macros = new Macros();
        this.easyMode = useTolerance;
    }

    public String translateCreate(String[] speechResultArray){
        String result = translateFromHashtable(convertStringArrayToString(speechResultArray),macros.getCreateHashtable());
        return result;
    }

    public String translateSelect(String[] speechResultArray){
        String result = translateFromHashtable(convertStringArrayToString(speechResultArray),macros.getSelectionHashtable());
        return result;
    }

    public String translateDelete(String[] speechResultArray){
        String result = translateFromHashtable(convertStringArrayToString(speechResultArray),macros.getDeleteHashtable());
        return result;
    }

    public String translateFree(String[] speechResultArray){
        String result = "";

        for(int i = 0; i < speechResultArray.length; i++) {
            result = result + speechResultArray[i] + " ";
        }
        return result;
    }

    private String translateFromHashtable(String speechResult, Hashtable<String,String> commandHashtable){
        String result = "";
        int levDist = 1000;
        String priorKey = null;
        for(Object key :commandHashtable.keySet())
        {

            int tempLevDist = LevenshteinDistance.computeLevenshteinDistance(key.toString(), speechResult);
            if(tempLevDist < levDist)
            {
                levDist = tempLevDist;
                priorKey = key.toString();
            }
        }
        if(priorKey != null)
        {
            if(!easyMode)
                result = commandHashtable.get(priorKey);
            else
            {
                if(levDist <= speechResult.length()*tolerance)
                    result = commandHashtable.get(priorKey);
            }

        }
        return result;
    }

    private String convertStringArrayToString(String[] stringArray){
        StringBuilder strBuilder = new StringBuilder();
        for(String s :stringArray)
        {
            strBuilder.append(s);
            strBuilder.append(" ");
        }
        String result = strBuilder.toString();
        return result;
    }

    private boolean compareWords(String speechResult, String originalWord){
        int levDist = LevenshteinDistance.computeLevenshteinDistance(speechResult, originalWord);
        if(levDist <= originalWord.length()*tolerance)
            return true;
        else
            return false;
    }
}
