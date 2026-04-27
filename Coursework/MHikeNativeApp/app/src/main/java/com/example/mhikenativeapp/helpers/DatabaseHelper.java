package com.example.mhikenativeapp.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mhikenativeapp.models.Hike;
import com.example.mhikenativeapp.models.Observation;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MHike.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_HIKES = "hikes";
    private static final String COLUMN_HIKE_ID = "hike_id";
    private static final String COLUMN_HIKE_NAME = "name";
    private static final String COLUMN_HIKE_LOCATION = "location";
    private static final String COLUMN_HIKE_DATE = "date";
    private static final String COLUMN_HIKE_PARKING = "parking_available";
    private static final String COLUMN_HIKE_LENGTH = "length";
    private static final String COLUMN_HIKE_DIFFICULTY = "difficulty";
    private static final String COLUMN_HIKE_DESCRIPTION = "description";
    private static final String COLUMN_HIKE_WEATHER = "weather";
    private static final String COLUMN_HIKE_TRAIL_CONDITION = "trail_condition";

    private static final String TABLE_OBSERVATIONS = "observations";
    private static final String COLUMN_OBS_ID = "obs_id";
    private static final String COLUMN_OBS_TEXT = "observation_text";
    private static final String COLUMN_OBS_TIME = "time";
    private static final String COLUMN_OBS_COMMENTS = "comments";
    private static final String COLUMN_OBS_HIKE_ID_FK = "hike_id";


    private static final String CREATE_TABLE_HIKES = "CREATE TABLE " + TABLE_HIKES + "("
            + COLUMN_HIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_HIKE_NAME + " TEXT NOT NULL,"
            + COLUMN_HIKE_LOCATION + " TEXT NOT NULL,"
            + COLUMN_HIKE_DATE + " TEXT NOT NULL,"
            + COLUMN_HIKE_PARKING + " TEXT NOT NULL,"
            + COLUMN_HIKE_LENGTH + " TEXT NOT NULL,"
            + COLUMN_HIKE_DIFFICULTY + " TEXT NOT NULL,"
            + COLUMN_HIKE_DESCRIPTION + " TEXT,"
            + COLUMN_HIKE_WEATHER + " TEXT,"
            + COLUMN_HIKE_TRAIL_CONDITION + " TEXT"
            + ")";

    private static final String CREATE_TABLE_OBSERVATIONS = "CREATE TABLE " + TABLE_OBSERVATIONS + "("
            + COLUMN_OBS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_OBS_TEXT + " TEXT NOT NULL,"
            + COLUMN_OBS_TIME + " TEXT NOT NULL,"
            + COLUMN_OBS_COMMENTS + " TEXT,"
            + COLUMN_OBS_HIKE_ID_FK + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_OBS_HIKE_ID_FK + ") REFERENCES " + TABLE_HIKES + "(" + COLUMN_HIKE_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HIKES);
        db.execSQL(CREATE_TABLE_OBSERVATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }


    public long addHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_HIKE_NAME, hike.getName());
        values.put(COLUMN_HIKE_LOCATION, hike.getLocation());
        values.put(COLUMN_HIKE_DATE, hike.getDate());
        values.put(COLUMN_HIKE_PARKING, hike.getParkingAvailable());
        values.put(COLUMN_HIKE_LENGTH, hike.getLength());
        values.put(COLUMN_HIKE_DIFFICULTY, hike.getDifficulty());
        values.put(COLUMN_HIKE_DESCRIPTION, hike.getDescription());
        values.put(COLUMN_HIKE_WEATHER, hike.getWeather());
        values.put(COLUMN_HIKE_TRAIL_CONDITION, hike.getTrailCondition());

        long id = db.insert(TABLE_HIKES, null, values);
        db.close();
        return id;
    }


    public ArrayList<Hike> getAllHikes() {
        ArrayList<Hike> hikeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_HIKES + " ORDER BY " + COLUMN_HIKE_NAME + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HIKE_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LOCATION)));
                hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DATE)));
                hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_PARKING)));
                hike.setLength(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LENGTH)));
                hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DIFFICULTY)));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DESCRIPTION)));
                hike.setWeather(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_WEATHER)));
                hike.setTrailCondition(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_TRAIL_CONDITION)));

                hikeList.add(hike);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return hikeList;
    }

    public ArrayList<Hike> searchHikes(String query) {
        ArrayList<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        String selection = COLUMN_HIKE_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        try {
            cursor = db.query(
                    TABLE_HIKES,
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    COLUMN_HIKE_NAME + " ASC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Hike hike = new Hike();
                    hike.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HIKE_ID)));
                    hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME)));
                    hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LOCATION)));
                    hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DATE)));
                    hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_PARKING)));
                    hike.setLength(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LENGTH)));
                    hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DIFFICULTY)));
                    hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DESCRIPTION)));
                    hike.setWeather(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_WEATHER)));
                    hike.setTrailCondition(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_TRAIL_CONDITION)));

                    hikeList.add(hike);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return hikeList;
    }


    public Hike getHikeById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Hike hike = null;
        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_HIKES,
                    null,
                    COLUMN_HIKE_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                hike = new Hike();
                hike.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_HIKE_ID)));
                hike.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_NAME)));
                hike.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LOCATION)));
                hike.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DATE)));
                hike.setParkingAvailable(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_PARKING)));
                hike.setLength(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_LENGTH)));
                hike.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DIFFICULTY)));
                hike.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_DESCRIPTION)));
                hike.setWeather(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_WEATHER)));
                hike.setTrailCondition(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HIKE_TRAIL_CONDITION)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return hike;
    }

    public int updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_HIKE_NAME, hike.getName());
        values.put(COLUMN_HIKE_LOCATION, hike.getLocation());
        values.put(COLUMN_HIKE_DATE, hike.getDate());
        values.put(COLUMN_HIKE_PARKING, hike.getParkingAvailable());
        values.put(COLUMN_HIKE_LENGTH, hike.getLength());
        values.put(COLUMN_HIKE_DIFFICULTY, hike.getDifficulty());
        values.put(COLUMN_HIKE_DESCRIPTION, hike.getDescription());
        values.put(COLUMN_HIKE_WEATHER, hike.getWeather());
        values.put(COLUMN_HIKE_TRAIL_CONDITION, hike.getTrailCondition());

        return db.update(TABLE_HIKES, values, COLUMN_HIKE_ID + " = ?",
                new String[]{String.valueOf(hike.getId())});
    }

    public void deleteHike(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.delete(TABLE_OBSERVATIONS, COLUMN_OBS_HIKE_ID_FK + " = ?",
                new String[]{String.valueOf(id)});
        db.delete(TABLE_HIKES, COLUMN_HIKE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, null, null);
        db.delete(TABLE_HIKES, null, null);
        db.close();
    }


    public long addObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_OBS_TEXT, observation.getObservation());
        values.put(COLUMN_OBS_TIME, observation.getTime());
        values.put(COLUMN_OBS_COMMENTS, observation.getComments());
        values.put(COLUMN_OBS_HIKE_ID_FK, observation.getHikeId());

        long id = db.insert(TABLE_OBSERVATIONS, null, values);
        db.close();
        return id;
    }

    public ArrayList<Observation> getAllObservationsForHike(long hikeId) {
        ArrayList<Observation> obsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_OBSERVATIONS,
                null,
                COLUMN_OBS_HIKE_ID_FK + " = ?",
                new String[]{String.valueOf(hikeId)},
                null,
                null,
                COLUMN_OBS_TIME + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                Observation obs = new Observation();
                obs.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_OBS_ID)));
                obs.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_TEXT)));
                obs.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_TIME)));
                obs.setComments(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_COMMENTS)));
                obs.setHikeId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_OBS_HIKE_ID_FK)));

                obsList.add(obs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return obsList;
    }

    public void deleteObservation(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OBSERVATIONS, COLUMN_OBS_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public Observation getObservationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Observation obs = null;
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_OBSERVATIONS, null,
                    COLUMN_OBS_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                obs = new Observation();
                obs.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_OBS_ID)));
                obs.setObservation(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_TEXT)));
                obs.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_TIME)));
                obs.setComments(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBS_COMMENTS)));
                obs.setHikeId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_OBS_HIKE_ID_FK)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return obs;
    }


    public int updateObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_OBS_TEXT, observation.getObservation());
        values.put(COLUMN_OBS_TIME, observation.getTime());
        values.put(COLUMN_OBS_COMMENTS, observation.getComments());

        int rows = db.update(TABLE_OBSERVATIONS, values,
                COLUMN_OBS_ID + " = ?",
                new String[]{String.valueOf(observation.getId())});
        db.close();
        return rows;
    }

}