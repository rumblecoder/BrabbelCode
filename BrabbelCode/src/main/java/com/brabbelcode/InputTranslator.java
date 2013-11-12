package com.brabbelcode;



/**
 * Created by andypf on 03.11.13.
 */
public class InputTranslator {

    private double tolerance;

    public InputTranslator()
    {
        this.tolerance = 0.6;
    }

    public String translateCreate(String[] speechResultArray)
    {
        String result = "";

        if(speechResultArray.length == 2)
        {
            if(compareWords(speechResultArray[0], "class"))
            {
                result = "public class "+speechResultArray[1]+ "{\n\n\tpublic "+speechResultArray[1]+"(){\n\t}\n\n}";
            }
            else if(compareWords(speechResultArray[0], "next"))
            {
                if(compareWords(speechResultArray[1],"line"))
                    result = "neue Line\n";
            }
            else if(compareWords(speechResultArray[0], "exit"))
            {
                if(compareWords(speechResultArray[1],"mode"))
                    result = "levaing mode";
            }
            else if (compareWords(speechResultArray[0], "create"))
            {
                if(compareWords(speechResultArray[1],"function"))
                    result = "function(){\n}";
            }
        }

        return result;
    }
    public String translateSelect(String[] speechResultArray) {
        String result = "";

        if (compareWords(speechResultArray[0], "all")) {
            result = "all";
        } else if (compareWords(speechResultArray[0], "none")) {
            result = "none";
        }
        return result;
    }
    public String translateDelete(String[] speechResultArray) {
        String result = "";

        if (compareWords(speechResultArray[0], "all")) {
            result = "all";
        }
        return result;
    }
    public String translateFree(String[] speechResultArray) {
        String result = "";

        for(int i = 0; i < speechResultArray.length; i++) {
            result = result + speechResultArray[i] + " ";
        }
        return result;
    }

    private boolean compareWords(String speechResult, String originalWord)
    {
        int levDist = LevenshteinDistance.computeLevenshteinDistance(speechResult, originalWord);
        if(levDist <= originalWord.length()*tolerance)
            return true;
        else
            return false;
    }
}
