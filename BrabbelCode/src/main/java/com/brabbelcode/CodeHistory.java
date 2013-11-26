package com.brabbelcode;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.LinkedList;

public class CodeHistory {

    private static final CodeHistory instance = new CodeHistory();
    private boolean isUndoOrRedo = false;
    private EditHistory editHistory;
    private EditTextChangeListener changeListener;
    private TextView textView;

    private CodeHistory(){

    }

    public static CodeHistory getInstance() {
        return instance;
    }

    public void watchTextView(TextView codeEditor, int maxHistorySize){
        textView = codeEditor;
        editHistory = new EditHistory();
        editHistory.setMaxHistorySize(maxHistorySize);
        changeListener = new EditTextChangeListener();
        textView.addTextChangedListener(changeListener);
    }

    public void disconnect() {
        textView.removeTextChangedListener(changeListener);
    }

    public void setMaxHistorySize(int maxHistorySize) {
        editHistory.setMaxHistorySize(maxHistorySize);
    }

    public void clearHistory() {
        editHistory.clear();
    }

    public boolean getCanUndo() {
        return (editHistory.position > 0);
    }

    public void undo() {
        EditItem edit = editHistory.getPrevious();
        if (edit == null) {
            return;
        }

        Editable text = textView.getEditableText();
        int start = edit.start;
        int end = start + (edit.after != null ? edit.after.length() : 0);

        isUndoOrRedo = true;
        text.replace(start, end, edit.before);
        isUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0, text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text, edit.before == null ? start
                : (start + edit.before.length()));
    }

    /**
     * Replaces the placeholders in the last given command (Author: Chris)
     * @param replacement
     */
    public void replace(String replacement) {
        EditItem edit = editHistory.getPrevious();
        if (edit == null) {
            return;
        }

        Editable text = textView.getEditableText();
        int start = edit.start;
        int end = start + (edit.after != null ? edit.after.length() : 0);

        isUndoOrRedo = true;
        text.replace(start, end, replacement);
        isUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0, text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text, edit.before == null ? start
                : (start + edit.before.length()));
    }

    public boolean getCanRedo() {
        return (editHistory.position < editHistory.history.size());
    }

    public void redo() {
        EditItem edit = editHistory.getNext();
        if (edit == null) {
            return;
        }

        Editable text = textView.getEditableText();
        int start = edit.start;
        int end = start + (edit.before != null ? edit.before.length() : 0);

        isUndoOrRedo = true;
        text.replace(start, end, edit.after);
        isUndoOrRedo = false;

        // This will get rid of underlines inserted when editor tries to come
        // up with a suggestion.
        for (Object o : text.getSpans(0, text.length(), UnderlineSpan.class)) {
            text.removeSpan(o);
        }

        Selection.setSelection(text, edit.after == null ? start
                : (start + edit.after.length()));
    }

    public void storePersistentState(SharedPreferences.Editor editor, String prefix) {
        // Store hash code of text in the editor so that we can check if the
        // editor contents has changed.
        editor.putString(prefix + ".hash",
                String.valueOf(textView.getText().toString().hashCode()));
        editor.putInt(prefix + ".maxSize", editHistory.maxHistorySize);
        editor.putInt(prefix + ".position", editHistory.position);
        editor.putInt(prefix + ".size", editHistory.history.size());

        int i = 0;
        for (EditItem ei : editHistory.history) {
            String pre = prefix + "." + i;

            editor.putInt(pre + ".start", ei.start);
            editor.putString(pre + ".before", ei.before.toString());
            editor.putString(pre + ".after", ei.after.toString());

            i++;
        }
    }

    public boolean restorePersistentState(SharedPreferences sp, String prefix)
            throws IllegalStateException {

        boolean ok = doRestorePersistentState(sp, prefix);
        if (!ok) {
            editHistory.clear();
        }

        return ok;
    }

    private boolean doRestorePersistentState(SharedPreferences sp, String prefix) {

        String hash = sp.getString(prefix + ".hash", null);
        if (hash == null) {
            // No state to be restored.
            return true;
        }

        if (Integer.valueOf(hash) != textView.getText().toString().hashCode()) {
            return false;
        }

        editHistory.clear();
        editHistory.maxHistorySize = sp.getInt(prefix + ".maxSize", -1);

        int count = sp.getInt(prefix + ".size", -1);
        if (count == -1) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            String pre = prefix + "." + i;

            int start = sp.getInt(pre + ".start", -1);
            String before = sp.getString(pre + ".before", null);
            String after = sp.getString(pre + ".after", null);

            if (start == -1 || before == null || after == null) {
                return false;
            }
            editHistory.add(new EditItem(start, before, after));
        }

        editHistory.position = sp.getInt(prefix + ".position", -1);
        if (editHistory.position == -1) {
            return false;
        }

        return true;
    }

    /**
     * Keeps track of all the edit history of a text.
     */
    private final class EditHistory {

        private int position = 0;
        private int maxHistorySize = -1;
        private final LinkedList<EditItem> history = new LinkedList<EditItem>();

        private void clear() {
            position = 0;
            history.clear();
        }

        private void add(EditItem item) {
            while (history.size() > position) {
                history.removeLast();
            }
            history.add(item);
            position++;

            if (maxHistorySize >= 0) {
                trimHistory();
            }
        }

        private void setMaxHistorySize(int newMaxHistorySize) {
            maxHistorySize = newMaxHistorySize;
            if (maxHistorySize >= 0) {
                trimHistory();
            }
        }

        private void trimHistory() {
            while (history.size() > maxHistorySize) {
                history.removeFirst();
                position--;
            }

            if (position < 0) {
                position = 0;
            }
        }

        private EditItem getPrevious() {
            if (position == 0) {
                return null;
            }
            position--;
            return history.get(position);
        }

        private EditItem getNext() {
            if (position >= history.size()) {
                return null;
            }

            EditItem item = history.get(position);
            position++;
            return item;
        }
    }

    /**
     * Represents the changes performed by a single edit operation.
     */
    private final class EditItem {
        private final int start;
        private final CharSequence before;
        private final CharSequence after;

        public EditItem(int start, CharSequence before, CharSequence after) {
            this.start = start;
            this.before = before;
            this.after = after;
        }
    }

    /**
     * Class that listens to changes in the text.
     */
    private final class EditTextChangeListener implements TextWatcher {

        private CharSequence beforeChange;
        private CharSequence afterChange;

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (isUndoOrRedo) {
                return;
            }

            beforeChange = s.subSequence(start, start + count);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (isUndoOrRedo) {
                return;
            }

            afterChange = s.subSequence(start, start + count);
            editHistory.add(new EditItem(start, beforeChange, afterChange));
        }

        public void afterTextChanged(Editable s) {
        }
    }
}
