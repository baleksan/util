package com.baleksan.util.searchtree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class StringSearchTree extends SearchTree<Character> {
    public StringSearchTree() {
        SearchTreeNode<Character> root = new SearchTreeNode<Character>('&');
        setRoot(root);
    }
    
    public boolean accept(String string) {
        List<Character> list = new ArrayList<Character>();
        for (char character : string.toCharArray()) {
            list.add(character);
        }
        return accept(list);
    }

    public String acceptPrefix(String string) {
        if(string == null) {
            return null;
        }
        
        List<Character> list = new ArrayList<Character>();
        for (char character : string.toCharArray()) {
            list.add(character);
        }

        List<Character> result = acceptPrefix(list);
        if(result == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for(char character : result) {
            builder.append(character);
        }

        return builder.toString();
    }

    public void add(String string) {
        Character[] data = new Character[string.length()];
        for(int i = 0; i < string.length(); i++) {
            data[i] = string.charAt(i);
        }

        addSequence(data);
    }
}
