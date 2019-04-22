package com.baycosinus.chatdroid;

import java.util.Date;

public class Message
{
    private User sender;
    private User receiver;
    private String message;
    private Date date;

    Message(User sender, User receiver, String message, Date date)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
    }
}
