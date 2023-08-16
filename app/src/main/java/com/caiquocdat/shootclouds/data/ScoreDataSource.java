package com.caiquocdat.shootclouds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.caiquocdat.shootclouds.model.ScoreModel;

import java.util.ArrayList;
import java.util.List;

public class ScoreDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ScoreDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertScore(int rank, int point) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RANK, rank);
        values.put(DatabaseHelper.COLUMN_POINT, point);

        return database.insert(DatabaseHelper.TABLE_SCORE, null, values);
    }

    public void deleteLowestScore() {
        // Query để tìm điểm thấp nhất
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_SCORE + " ORDER BY " + DatabaseHelper.COLUMN_POINT + " ASC LIMIT 1", null);
        if(cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            database.delete(DatabaseHelper.TABLE_SCORE, DatabaseHelper.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        }
        cursor.close();
    }

    public List<ScoreModel> getAllScores() {
        List<ScoreModel> scores = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_SCORE, null, null, null, null, null, DatabaseHelper.COLUMN_POINT + " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ScoreModel score = cursorToScore(cursor);
            scores.add(score);
            cursor.moveToNext();
        }
        cursor.close();
        return scores;
    }

    private ScoreModel cursorToScore(Cursor cursor) {
        ScoreModel score = new ScoreModel();
        score.setId(cursor.getLong(0));
        score.setRank(cursor.getInt(1));
        score.setPoint(cursor.getInt(2));
        return score;
    }

    // Hàm này kiểm tra và cập nhật điểm nếu cần thiết
    public void checkAndUpdateScores(int point) {
        List<ScoreModel> scores = getAllScores();

        if (scores.size() < 5) {
            insertScore(scores.size() + 1, point);
        } else {
            if (point > scores.get(4).getPoint()) {
                deleteLowestScore();
                insertScore(5, point);
            }
        }
    }
}
