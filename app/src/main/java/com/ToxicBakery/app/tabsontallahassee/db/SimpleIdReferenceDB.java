package com.ToxicBakery.app.tabsontallahassee.db;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mapdb.Atomic;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.Closeable;
import java.io.File;
import java.util.Map;

/**
 * Abstraction for creating an add and replace only database accessible by index and by id for a
 * given type.
 *
 * @param <T>
 */
abstract class SimpleIdReferenceDB<T extends IdTagged> implements Closeable {

    private static final String INDEX_GENERATOR = "INDEX_GENERATOR";
    private static final String TYPE_MAP = "TYPE_MAP";
    private static final String REFERENCE_MAP = "REFERENCE_MAP";

    final Map<Integer, T> typeMap;
    final Map<String, Integer> indirectReferenceMap;
    final Atomic.Integer idGenerator;
    final Context context;
    final DB db;

    protected SimpleIdReferenceDB(@NonNull Context context) {
        this.context = context.getApplicationContext();

        db = createDb();

        idGenerator = db.atomicInteger(INDEX_GENERATOR);
        typeMap = db.hashMap(TYPE_MAP);
        indirectReferenceMap = db.hashMap(REFERENCE_MAP);
    }

    @Nullable
    public T getById(String id) {
        Integer idx = indirectReferenceMap.get(id);
        if (idx == null) {
            return null;
        } else {
            return typeMap.get(idx);
        }
    }

    @Nullable
    public T getAt(@IntRange(from = 0) int idx) {
        return typeMap.get(idx);
    }

    public void addOrReplace(@NonNull T value) {
        final String id = value.getId();
        Integer storedIdx = indirectReferenceMap.get(id);
        if (storedIdx == null) {
            final int idx = idGenerator.getAndIncrement();
            typeMap.put(idx, value);
            indirectReferenceMap.put(id, idx);
        } else {
            typeMap.put(storedIdx, value);
        }
    }

    /**
     * Add a list of types to the store.
     *
     * @param valueList list of the defined type to store
     */
    public void addOrReplaceAll(@NonNull T[] valueList) {
        for (T type : valueList) {
            if (type != null) {
                addOrReplace(type);
            }
        }
    }

    /**
     * Size of the store.
     *
     * @return size of the store
     */
    public int size() {
        // Always getAndIncrement use when adding types so generator represents current size.
        return idGenerator.get();
    }

    @Override
    public void close() {
        getDb().close();
    }

    protected Context getContext() {
        return context;
    }

    protected DB getDb() {
        return db;
    }

    protected File getDbFile() {
        return new File(getContext().getFilesDir(), getClass().getName());
    }

    protected DB createDb() {
        return DBMaker.fileDB(getDbFile())
                .storeExecutorEnable()
                .cacheSize(1024)
                .cacheLRUEnable()
                .asyncWriteEnable()
                .closeOnJvmShutdown()
                .make();
    }

}
