package com.brabbelcode;



/**
 * Created by andypf on 03.11.13.
 */
public class InputTranslator {

    private double tolerance;
    private String mode;

    public InputTranslator()
    {
        this.tolerance = 0.6;
        this.mode = "freeMode";
    }

    public String translateInput(String speechResult)
    {
        String result = "test";
        String [] speechResultArray = speechResult.split(" ");

        if(speechResultArray.length == 3)
        {
            if(compareWords(speechResultArray[0], "create") || compareWords(speechResultArray[0], "make"))
            {
                mode = "create";
                if(compareWords(speechResultArray[1], "class"))
                {
                    result = "public class "+speechResultArray[2]+ "{\n\n\tpublic "+speechResultArray[2]+"(){\n\t}\n\n}";
                }

            }


        }
        else if(speechResultArray.length == 2)
        {
            if(compareWords(speechResultArray[0], "next"))
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


    private boolean compareWords(String speechResult, String originalWord)
    {
        int levDist = LevenshteinDistance.computeLevenshteinDistance(speechResult, originalWord);
        if(levDist <= originalWord.length()*tolerance)
            return true;
        else
            return false;
    }


    public String Mode()
    {
        return this.mode;
    }




}
