package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static com.urise.webapp.TestData.*;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    protected Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID_1, "New Name");
        newResume.setContact(ContactType.MAIL, "mail@google.com");
        newResume.setContact(ContactType.SKYPE, "newSkype");
        newResume.setContact(ContactType.PHONE, "+7 921 222-22-22");
        storage.update(newResume);
        assertEquals(newResume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistException() {
        storage.get("dummy");
    }

    @Test
    public void getAllSorted() {
        List<Resume> actual = storage.getAllSorted();
        assertEquals(3, actual.size());
        List<Resume> expected = Arrays.asList(R1, R2, R3);
        Collections.sort(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void save() {
        storage.save(R4);
        assertSize(4);
        assertGet(R4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistStorageException() {
        storage.save(R1);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistStorageException() {
        storage.delete("dummy");
    }

    @Test
    public void get() {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistStorageException() {
        storage.get("dummy");
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}