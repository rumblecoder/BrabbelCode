package com.brabbelcode;

/**
 * Created by bytemares on 11/12/13.
 */
public class IndexList {
    private int start = 0;
    private int end = 0;

    public IndexList(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

}
