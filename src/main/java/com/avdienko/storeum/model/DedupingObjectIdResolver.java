package com.avdienko.storeum.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;

public class DedupingObjectIdResolver extends SimpleObjectIdResolver {
    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object ob) {
        if (_items == null) {
            _items = new HashMap<>();
        }
        _items.put(id, ob);
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new DedupingObjectIdResolver();
    }
}