package ru.mipt1c.homework.task1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KeyValueStorageImpl<K, V> implements KeyValueStorage<K, V> {

    private static final String FILE_NAME = ".key_value_storage";
    private boolean isOpen;
    private final Path storagePath;
    private Map<K, V> storageState;

    public KeyValueStorageImpl(String path) {
        storagePath = Paths.get(path);

        if (!Files.exists(storagePath)) {
            throw new MalformedDataException("'" + storagePath + "' file path does not exist");
        }

        Path filePath = Paths.get(storagePath.toString(), FILE_NAME);
        if (Files.exists(filePath)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                try {
                    storageState = (Map<K, V>) objectInputStream.readObject();
                } catch (ClassCastException ex) {
                    throw new MalformedDataException("Object in storage is not correct Map");
                }
            } catch (ClassNotFoundException | IOException exception) {
                throw new MalformedDataException("Error in reading key-value storage", exception);
            }
        }

        if (storageState == null) {
            storageState = new HashMap<>();
        }
        isOpen = true;
    }

    private void checkAccess() {
        if (!isOpen) {
            throw new MalformedDataException("Storage is already closed");
        }
    }

    @Override
    public V read(K key) {
        checkAccess();
        return storageState.get(key);
    }

    @Override
    public boolean exists(K key) {
        checkAccess();
        return storageState.containsKey(key);
    }

    @Override
    public void write(K key, V value) {
        checkAccess();
        storageState.put(key, value);
    }

    @Override
    public void delete(K key) {
        checkAccess();
        storageState.remove(key);
    }

    @Override
    public Iterator<K> readKeys() {
        checkAccess();
        return storageState.keySet().iterator();
    }

    @Override
    public int size() {
        checkAccess();
        return storageState.size();
    }

    @Override
    public void close() {
        flush();
        isOpen = false;
    }

    @Override
    public void flush() {
        checkAccess();
        Path filePath = Paths.get(storagePath.toString(), FILE_NAME);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            objectOutputStream.writeObject(storageState);
        } catch (IOException exception) {
            throw new MalformedDataException("Error in flushing key-value storage", exception);
        }
    }
}
