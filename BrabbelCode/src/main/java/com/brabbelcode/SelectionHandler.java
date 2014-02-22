package com.brabbelcode;

import android.widget.EditText;

import java.util.ArrayList;

public class SelectionHandler {

    private static final SelectionHandler instance = new SelectionHandler();
    private EditText textBox;
    private int start = 0;
    private int end = 0;
    private int length = 0;
    private ArrayList<IndexList> indexList = new ArrayList<IndexList>();
    private int currentIndex = 0;
    private IndexList currentWord = new IndexList(0, 0);
    private String word = "";

    private SelectionHandler() {
    }

    public void init(EditText textBox) {
        this.textBox = textBox;
    }

    public static SelectionHandler getInstance() {
        return instance;
    }

    public void setStartIndex(int start) {
        this.start = start;
        this.calculateLength();
    }

    public void setEndIndex(int end) {
        this.end = end;
        this.calculateLength();
    }

    public int getStartIndex() {
        return this.start;
    }

    public int getEndIndex() {
        return this.end;
    }

    public int getSelectionLength() {
        return this.length;
    }

    public void selectAll() {
        this.textBox.selectAll();
    }

    public void selectNone() {
        this.textBox.setSelection(this.textBox.getText().length());
    }

    public void selectLine(String[] line) {
        if(line.length > 1 && line[1] != null) {
            if(Util.isInteger(line[1])) {
                int lineNumber = Integer.parseInt(line[1]);

                String[] lines = this.textBox.getText().toString().split("\r?\n");

                if(lines.length > lineNumber) {
                    int end = 0;
                    int start = 0;
                    for(int i = 0; i < lineNumber; i++) {
                        start = end;
                        end += lines[i].length() + 1; // +1 for \n
                    }
                    if(start < end) {
                        this.setStartIndex(start);
                        this.setEndIndex(end);
                        this.textBox.setSelection(start, end);
                    }
                }
            }
        }
        //TODO: unable to select last line!
    }

    /* word selection methods */
    public boolean selectNextWord(String[] word) {
        if(word.length > 1 && word[1] != null) {
            this.word = word[1];
            this.createIndex(this.word);
            return this.selectNext();
        }
        return false;
    }

    /**
     * Searches and if found selects a  given word in the text
     *
     * @param word
     * @return
     */
    public boolean selectWord(String word) {
        this.word = word;
        this.createIndex(this.word);
        return this.selectNext();
    }

    public boolean selectNext() {
        if(this.currentIndex < this.indexList.size()) {
            this.currentWord.setStart(this.indexList.get(this.currentIndex).getStart());
            this.currentWord.setEnd(this.indexList.get(this.currentIndex).getEnd());
            this.textBox.setSelection(this.currentWord.getStart(), this.currentWord.getEnd());
            this.start = this.currentWord.getStart();
            this.end = this.currentWord.getEnd();
            this.currentIndex = (this.currentIndex + 1) % this.indexList.size();
            return true;
        } else {
            this.resetIndex();
            return false;
        }
    }

    public void createIndex(String word) {
        this.resetIndex();
        String regex = "(?i:" + word + ")";
        String[] words = this.textBox.getText().toString().split(" ");

        if(words.length > 0) {
            int end = 0;
            int start = 0;
            for(int i = 0; i < words.length; i++) {
                start = end;
                end += words[i].length() + 1;
                /*
                if(words[i].matches("\n") || words[i].matches("\t") || words[i].matches("\r")) {
                    end += 1;
                }
                int noOfNewlines = this.countLines(words[i]);
                end += noOfNewlines;
                */
                String cleanStr = words[i].replace("\n", "").replace("\t", "").replace("\r", "");
                if(cleanStr.matches(regex) && start < end) {
                    this.indexList.add(new IndexList(start, end));
                }
            }
        }
    }

    /* private methods */
    private void resetIndex() {
        this.currentIndex = 0;
        this.indexList.clear();
    }

    private void calculateLength() {
        this.length = this.end - this.start;
    }

    private int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }
}
