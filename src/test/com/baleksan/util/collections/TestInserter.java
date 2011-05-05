package com.baleksan.util.collections;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class TestInserter implements TreeInserter<TestMessage> {
    @Override
    public boolean insertHere(TestMessage potentialParent, TestMessage newValue) {
//        if(newValue.getReplyToId() == 0) {
//            return true;
//        }

        return newValue.getReplyToId() == potentialParent.getMessageId();
    }
}
