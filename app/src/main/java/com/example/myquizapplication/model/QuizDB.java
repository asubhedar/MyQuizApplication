package com.example.myquizapplication.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

public class QuizDB {
    public static final String DATABASE_NAME = "QuizDB";
    public static final String CARDS_TABLE = "CardsTable";
    public static final String SETS_TABLE = "SetsTable";
    public static final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public QuizDB(Context context) {
        ourContext = context;
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public static final String KEY_ROW_ID = "_id";
        public static final String KEY_SET_ID = "setId";
        public static final String KEY_TERM = "term";
        public static final String KEY_DEFINITION = "definition";
        public static final String KEY_SET_TITLE = "setTitle";


        private DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String dropTableSqlCode = "DROP TABLE IF EXISTS " + CARDS_TABLE;
            db.execSQL(dropTableSqlCode);
            dropTableSqlCode = "DROP TABLE IF EXISTS " + SETS_TABLE;
            db.execSQL(dropTableSqlCode);

            String sqlCode = "CREATE TABLE " + CARDS_TABLE + " ("
                    + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_SET_ID + " INT NOT NULL,"
                    + KEY_TERM + " TEXT NOT NULL,"
                    + KEY_DEFINITION + " TEXT NOT NULL);";
            db.execSQL(sqlCode);
            sqlCode = "CREATE TABLE " + SETS_TABLE + " ("
                    + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_SET_TITLE + " TEXT NOT NULL);";
            db.execSQL(sqlCode);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntry(String tableName, Object object) {
        if (tableName.equals("CardsTable")) {
            Card card = (Card) object;
            ContentValues cv = new ContentValues();
            cv.put("setId", card.getSetId());
            cv.put("term", card.getTerm());
            cv.put("definition", card.getDefinition());
            return ourDatabase.insert(tableName, null, cv);
        } else if (tableName.equals("SetsTable")) {
            String setTitle = (String) object;
            ContentValues cv = new ContentValues();
            cv.put("setTitle", setTitle);
            return ourDatabase.insert(tableName, null, cv);
        } else return 0;
    }

    public long updateEntry(String tableName, Object object) {
        if (tableName.equals("CardsTable")) {
            Card card = (Card) object;
            ContentValues cv = new ContentValues();
            cv.put("_id", card.getId());
            cv.put("setId", card.getSetId());
            cv.put("term", card.getTerm());
            cv.put("definition", card.getDefinition());
            return ourDatabase.replace(tableName, null, cv);
        } else if (tableName.equals("SetsTable")) {
            String setTitle = (String) object;
            ContentValues cv = new ContentValues();
            cv.put("setTitle", setTitle);
            return ourDatabase.insert(tableName, null, cv);
        } else return 0;
    }

    public List<Set> getSetList() {
        List<Set> setList = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + SETS_TABLE;
        Cursor c = ourDatabase.rawQuery(selectSQL, null);
        if (c.moveToFirst()) {
            do {
                Set set = new Set();
                set.setId(c.getInt(0));
                set.setTitle(c.getString(1));
                setList.add(set);
            } while (c.moveToNext());
        }
        c.close();
        return setList;
    }

    public List<Card> getSetById(int setId) {
        List<Card> cardSet = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + CARDS_TABLE + " WHERE setId = " + setId;
        Cursor c = ourDatabase.rawQuery(selectSQL, null);
        if (c.moveToFirst()) {
            do {
                Card card = new Card();
                card.setId(c.getInt(0));
                card.setSetId(c.getInt(1));
                card.setTerm(c.getString(2));
                card.setDefinition(c.getString(3));
                cardSet.add(card);
            } while (c.moveToNext());
        }
        c.close();
        return cardSet;
    }

    public void deleteSet(Integer setId) {
        ourDatabase.delete(CARDS_TABLE, "setID = " + setId, null);
        ourDatabase.delete(SETS_TABLE, "_id = " + setId, null);
    }
}

    /*public String getData(String searchString) {
        String selection = null;
        if (!searchString.isEmpty()) {
            selection = KEY_ITEM + " LIKE '%" + searchString + "%'";
        }
        String[] columns = new String[]{KEY_ROW_ID, KEY_ITEM};
        Cursor cursor = ourDatabase.query(DATABASE_TABLE, columns, selection, null, null, null, null);
        String result = "";
        int iItem = cursor.getColumnIndex(KEY_ITEM);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String item = cursor.getString(iItem);
            if (!item.isEmpty())
                result = result + item + "\n";
        }
        cursor.close();
        return result;
    }
    */


