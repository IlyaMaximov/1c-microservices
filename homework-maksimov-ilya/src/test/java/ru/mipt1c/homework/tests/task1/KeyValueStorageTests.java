package ru.mipt1c.homework.tests.task1;

import ru.mipt1c.homework.task1.KeyValueStorage;
import ru.mipt1c.homework.task1.KeyValueStorageImpl;
import ru.mipt1c.homework.task1.MalformedDataException;

public class KeyValueStorageTests extends AbstractSingleFileStorageTest {
    @Override
    protected KeyValueStorage<String, String> buildStringsStorage(String path) throws MalformedDataException {
        return new KeyValueStorageImpl<>(path);
    }

    @Override
    protected KeyValueStorage<Integer, Double> buildNumbersStorage(String path) throws MalformedDataException {
        return new KeyValueStorageImpl<>(path);
    }

    @Override
    protected KeyValueStorage<StudentKey, Student> buildPojoStorage(String path) throws MalformedDataException {
        return new KeyValueStorageImpl<>(path);
    }
}
