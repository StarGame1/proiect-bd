package sample;

import java.sql.Timestamp;

public class GroupChatMessage {
    private int messageId;
    private int senderId;
    private int groupId;
    private String messageText;
    private Timestamp timestamp;

    public GroupChatMessage() {
        // Default constructor
    }

    public GroupChatMessage(int messageId, int senderId, int groupId, String messageText, Timestamp timestamp) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.groupId = groupId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GroupChatMessage{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", groupId=" + groupId +
                ", messageText='" + messageText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
