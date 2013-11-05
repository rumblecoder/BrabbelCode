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

    public String translateInput(String speechResult)
    {
        String result = "test";
        String [] speechResultArray = speechResult.split(" ");

        if(speechResultArray.length == 3)
        {
            if(compareWords(speechResultArray[0], "create") || compareWords(speechResultArray[0], "make"))
            {
                if(compareWords(speechResultArray[1], "class"))
                {
                    if(compareWords(speechResultArray[2], "person"))

                        result = "public class person{\n\n\tpublic person(){\n\t}\n\n}";
                }

            }


        }
        if(speechResultArray.length == 2)
        {
            if(compareWords(speechResultArray[0], "new"))
                if(compareWords(speechResultArray[1],"line"))
                    result = "\n";
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
