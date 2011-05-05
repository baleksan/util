package com.baleksan.util.collections;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TestMessage implements Comparable<TestMessage> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestMessage that = (TestMessage) o;

        if (messageId != that.messageId) return false;
        if (replyToId != that.replyToId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (messageId ^ (messageId >>> 32));
        result = 31 * result + (int) (replyToId ^ (replyToId >>> 32));
        return result;
    }

    @Override
    public int compareTo(TestMessage o) {
        return (int) (messageId - o.getMessageId());
    }
}

