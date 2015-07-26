package com.kayulab.android.coderadar.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by kevinyu on 5/20/15.
 */
public class TestDb extends AndroidTestCase{

    private static final long TEST_START_DATE = 1419033600L;
    private static final long TEST_FINISH_DATE = 1419043600L;

    @Override
    protected void setUp() throws Exception {
        mContext.deleteDatabase(ContestDBHelper.DATABASE_NAME);
    }

    public void testCreateDatabase() {

        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(ContestContract.ContestEntry.TABLE_NAME);

        SQLiteDatabase db = new ContestDBHelper(mContext)
                .getWritableDatabase();
        assertTrue(db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'",null);

        assertTrue("Error : This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error : Database was created without contest table",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + ContestContract.ContestEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(ContestContract.ContestEntry._ID);
        locationColumnHashSet.add(ContestContract.ContestEntry.COLUMN_TITLE);
        locationColumnHashSet.add(ContestContract.ContestEntry.COLUMN_DESCRIPTION);
        locationColumnHashSet.add(ContestContract.ContestEntry.COLUMN_URL);
        locationColumnHashSet.add(ContestContract.ContestEntry.COLUMN_SOURCE);
        locationColumnHashSet.add(ContestContract.ContestEntry.COLUMN_START_TIME);
        locationColumnHashSet.add(ContestContract.ContestEntry.COLUMN_END_TIME);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required Contest entry columns",
                locationColumnHashSet.isEmpty());
        db.close();

    }

    public void testContestTable() {

        ContentValues contestValues = createMockContestValues();

        ContestDBHelper dbHelper = new ContestDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowId = db.insert(ContestContract.ContestEntry.TABLE_NAME,null,contestValues);

        assertTrue("Error : cannot insert contest.",rowId!=-1);

        Cursor contestCursor = db.query(
                ContestContract.ContestEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        validateCursor("Cursor does not match with inserted value",contestCursor,contestValues);

        assertFalse("Error : cursor return more than one record.",contestCursor.moveToNext());

        contestCursor.close();
        dbHelper.close();
    }

    public static ContentValues createMockContestValues() {
        ContentValues contestValues = new ContentValues();
        contestValues.put(ContestContract.ContestEntry.COLUMN_TITLE,"codeforces div 1");
        contestValues.put(ContestContract.ContestEntry.COLUMN_DESCRIPTION,"No description");
        contestValues.put(ContestContract.ContestEntry.COLUMN_SOURCE,"codeforces");
        contestValues.put(ContestContract.ContestEntry.COLUMN_URL,"http://www.codeforces.com");
        contestValues.put(ContestContract.ContestEntry.COLUMN_START_TIME,TEST_START_DATE);
        contestValues.put(ContestContract.ContestEntry.COLUMN_END_TIME,TEST_FINISH_DATE);
        return contestValues;
    }

    public static void validateCursor(String errorMessage, Cursor valueCursor,
                                      ContentValues expectedValues) {
        assertTrue("Error : empty cursor returned.", valueCursor.moveToFirst());

        Set<Map.Entry<String,Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String,Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Error : Column "+ columnName + " not found.",idx == -1);
            String expectedValue = entry.getValue().toString();
            String cursorValue = valueCursor.getString(idx);
            assertEquals("column "+ columnName + " does not match."+errorMessage,expectedValue,cursorValue);
        }

    }


}
