package com.ToxicBakery.app.tabsontallahassee.db;

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SimpleIdReferenceDBTest extends AndroidTestCase {
    
    public void testGetById() throws Exception {

        IdTaggedString[] idTaggedStrings = {
                new IdTaggedString("a", "1"),
                new IdTaggedString("b", "2"),
                new IdTaggedString("c", "3"),
                new IdTaggedString("d", "4"),
        };

        try (SimpleIdReferenceDB<IdTaggedString> db = new SimpleIdReferenceDB<IdTaggedString>(getContext()) {
            @Override
            protected File getDbFile() {
                return new File(getContext().getFilesDir(), UUID.randomUUID().toString());
            }
        }) {
            db.addOrReplaceAll(idTaggedStrings);

            for (IdTaggedString taggedString : idTaggedStrings) {
                assertEquals(taggedString, db.getById(taggedString.getId()));
            }
        }
    }

    public void testGetAt() throws Exception {

        IdTaggedString[] idTaggedStrings = {
                new IdTaggedString("a", "1"),
                new IdTaggedString("b", "2"),
                new IdTaggedString("c", "3"),
                new IdTaggedString("d", "4"),
        };

        try (SimpleIdReferenceDB<IdTaggedString> db = new SimpleIdReferenceDB<IdTaggedString>(getContext()) {
            @Override
            protected File getDbFile() {
                return new File(getContext().getFilesDir(), UUID.randomUUID().toString());
            }
        }) {
            db.addOrReplaceAll(idTaggedStrings);

            for (int i = 0, j = db.size(); i < j; ++i) {
                IdTaggedString taggedString = db.getAt(i);
                assertTrue(idTaggedStrings[i].equals(taggedString));
            }
        }
    }

    public void testAddAll() throws Exception {

        IdTaggedString[] idTaggedStrings = {
                new IdTaggedString("a", "1"),
                new IdTaggedString("b", "2"),
                new IdTaggedString("c", "3"),
                new IdTaggedString("d", "4"),
        };

        try (SimpleIdReferenceDB<IdTaggedString> db = new SimpleIdReferenceDB<IdTaggedString>(getContext()) {
            @Override
            protected File getDbFile() {
                return new File(getContext().getFilesDir(), UUID.randomUUID().toString());
            }
        }) {
            db.addOrReplaceAll(idTaggedStrings);
        }
    }

    public void testSize() throws Exception {
        try (SimpleIdReferenceDB db = new SimpleIdReferenceDB(getContext()) {
            @Override
            protected File getDbFile() {
                return new File(getContext().getFilesDir(), UUID.randomUUID().toString());
            }
        }) {
            assertEquals(0, db.size());
        }
    }

    public void testGetContext() throws Exception {
        try (SimpleIdReferenceDB db = new SimpleIdReferenceDB(getContext()) {
        }) {
            db.getContext();
        }
    }

    public void testGetDb() throws Exception {
        try (SimpleIdReferenceDB db = new SimpleIdReferenceDB(getContext()) {
        }) {
            db.getDb();
        }
    }

    public void testGetDbFile() throws Exception {
        try (SimpleIdReferenceDB db = new SimpleIdReferenceDB(getContext()) {
        }) {
            db.getDbFile();
        }
    }

    public void testCreateDb() throws Exception {
        // Semaphore not for threading just for validation of call
        final Semaphore semaphore = new Semaphore(0);
        SimpleIdReferenceDB db = new SimpleIdReferenceDB(getContext()) {
            @Override
            protected DB createDb() {
                DB db = DBMaker.memoryDB()
                        .make();

                try {
                    return db;
                } finally {
                    semaphore.release();
                }
            }
        };

        try {
            assertTrue(semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS));
        } finally {
            db.getDb().close();
        }
    }

    /**
     * Simple concrete implementation of an IdTagged string to enable quick testing of database.
     */
    static class IdTaggedString implements IdTagged {

        private String string;
        private String id;

        public IdTaggedString(@NonNull String string,
                              @NonNull String id) {

            this.string = string;
            this.id = id;
        }

        @NonNull
        @Override
        public String getId() {
            return id;
        }

        @NonNull
        public String getString() {
            return string;
        }

        @Override
        public boolean equals(Object o) {
            if (o != null
                    && o instanceof IdTaggedString) {

                IdTaggedString other = (IdTaggedString) o;
                return id.equals(other.getId())
                        && string.equals(other.getString());
            }

            return false;
        }
    }

}