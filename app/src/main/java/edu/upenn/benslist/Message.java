package edu.upenn.benslist;

/**
 * Created by joshross on 3/30/17.
 */

public class Message {
    private String id;
    private String text;
    private String name;

    public Message() {
    }

    public Message(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

}
