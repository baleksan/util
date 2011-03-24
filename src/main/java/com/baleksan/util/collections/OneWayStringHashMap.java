package com.baleksan.util.collections;

import com.baleksan.util.hash.JenkinsHash;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class OneWayStringHashMap implements Externalizable {
    private Long2IntMap _map;
    private JenkinsHash hash;

    public OneWayStringHashMap() {
        _map = new Long2IntOpenHashMap();
        hash = JenkinsHash.get64BitHash();
    }

    public void putAll(Long2IntMap otherMap) {
        _map.putAll(otherMap);
    }

    public void put(String key, int value) {
        long hashKey = hash.hash64bit(key.getBytes());
        _map.put(hashKey, value);

    }

    public void put(long key, int value) {
        _map.put(key, value);
    }

    public Long2IntMap getMap() {
        return _map;
    }

    public int get(String key) {
        long hashKey = hash.hash64bit(key.getBytes());
        return _map.get(hashKey);
    }

    public boolean containsKey(String key) {
        return _map.containsKey(hash.hash64bit(key.getBytes()));
    }

    public boolean containsKey(long hashKey) {
        return _map.containsKey(hashKey);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        ObjectSet<Map.Entry<Long, Integer>> entries = _map.entrySet();
        out.writeInt(entries.size());
        for (Map.Entry<Long, Integer> entry : entries) {
            out.writeLong(entry.getKey());
            out.writeInt(entry.getValue());
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        _map = new Long2IntOpenHashMap(size);
        for (int i = 0; i < size; i++) {
            long key = in.readLong();
            int value = in.readInt();
            _map.put(key, value);
        }
    }
}
