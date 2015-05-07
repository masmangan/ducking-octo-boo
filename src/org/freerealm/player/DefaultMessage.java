package org.freerealm.player;

/**
 *
 * @author Deniz ARIKAN
 */
public class DefaultMessage implements Message {

    private String subject;
    private StringBuffer text;
    private boolean read = false;
    private int turnSent;

    // <editor-fold defaultstate="collapsed" desc="Getters & setters">
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public StringBuffer getText() {
        return text;
    }

    public void setText(StringBuffer text) {
        this.text = text;
    }

    public void setText(String text) {
        setText(new StringBuffer(text));
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getTurnSent() {
        return turnSent;
    }

    public void setTurnSent(int turnSent) {
        this.turnSent = turnSent;
    }
    // </editor-fold>
}
