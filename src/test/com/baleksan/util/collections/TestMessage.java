package com.baleksan.util.collections;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TestMessage {
    public long messageId;
    public long replyToId;

    public TestMessage(long messageId, long replyToId) {
        this.messageId = messageId;
        this.replyToId = replyToId;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getReplyToId() {
        return replyToId;
    }

    @Override
    public String toString() {
        return messageId + ":" + replyToId;
    }
}

