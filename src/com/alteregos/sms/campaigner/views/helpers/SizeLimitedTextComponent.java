package com.alteregos.sms.campaigner.views.helpers;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John Emmanuel
 */
public class SizeLimitedTextComponent extends PlainDocument {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SizeLimitedTextComponent.class);
    private int maxLength = 160;
    private JTextField counter;
    private JTextArea preview;
    private String footer;

    public SizeLimitedTextComponent(int maxLength, JTextField counter) {
        super();
        LOGGER.debug("** SizeLimitedTextComponent({}, {})", maxLength, counter.getName());
        this.maxLength = maxLength;
        this.counter = counter;
        this.counter.setText(String.valueOf(this.maxLength));
    }

    public SizeLimitedTextComponent(int maxLength, JTextField counter, JTextArea preview, String footer) {
        super();
        String[] args = new String[]{String.valueOf(maxLength), counter.getName(), preview.getName(), footer};
        LOGGER.debug("** SizeLimitedTextComponent({}, {}, {}, {})", args);
        this.maxLength = maxLength;
        this.counter = counter;
        this.preview = preview;
        this.footer = footer == null ? "" : footer;
        this.counter.setText(String.valueOf(this.maxLength));
        this.preview.setText(footer);
    }

    @Override
    public void insertString(int offset, String content, AttributeSet attributeSet) {
        int currentContentLength = getLength();
        int newContentLength = content.length();
        int finalContentLength = currentContentLength + newContentLength;
        updateRemainingLength(finalContentLength);
        if (finalContentLength > maxLength) {
        } else {
            try {
                super.insertString(offset, content, attributeSet);
                if (this.preview != null) {
                    updatePreviewContent();
                }
            } catch (BadLocationException ex) {
                LOGGER.debug("-- size of text component exceeded");
            }
        }
    }

    @Override
    protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
        super.insertUpdate(chng, attr);
        updateRemainingLength(getLength());
        if (this.preview != null) {
            try {
                updatePreviewContent();
            } catch (BadLocationException ex) {
                LOGGER.debug("-- size of text component exceeded");
            }
        }
    }

    @Override
    protected void postRemoveUpdate(DefaultDocumentEvent chng) {
        super.postRemoveUpdate(chng);
        updateRemainingLength(getLength());
        if (this.preview != null) {
            try {
                StringBuilder builder = new StringBuilder();
                builder.append(getText(0, getLength()));
                builder.append(footer);
                this.preview.setText(builder.toString());
            } catch (BadLocationException ex) {
                LOGGER.debug("-- size of text component exceeded");
            }
        }
    }

    private void updateRemainingLength(int currentLength) {
        int remainingCount = maxLength - currentLength;
        if (remainingCount <= 0) {
            remainingCount = 0;
        }
        counter.setText(String.valueOf(remainingCount));
    }

    private void updatePreviewContent() throws BadLocationException {
        StringBuilder builder = new StringBuilder();
        builder.append(getText(0, getLength()));
        builder.append(footer);
        this.preview.setText(builder.toString());
    }
}
