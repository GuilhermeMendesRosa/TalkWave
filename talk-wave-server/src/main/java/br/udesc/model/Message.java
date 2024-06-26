package br.udesc.model;

import br.udesc.enums.Command;

import java.util.Date;
import java.util.List;

public class Message {

    private String sender;
    private List<String> recipients;
    private String content;
    private Command command;
    private Date sendDate;
    private FileDTO file;

    public Message(String sender, Command command) {
        this.sender = sender;
        this.command = command;
    }

    public Message(String sender, List<String> recipients, Command command) {
        this.sender = sender;
        this.recipients = recipients;
        this.command = command;
    }
    public Message(String sender, List<String> recipients, String content, Command command) {
        this.sender = sender;
        this.recipients = recipients;
        this.content = content;
        this.command = command;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public FileDTO getFile() {
        return file;
    }

    public void setFile(FileDTO file) {
        this.file = file;
    }

}