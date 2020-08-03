package com.example.citizensapp.LocalDatabase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocalTripTableDao {
    @Query("SELECT * FROM localTripTable")
    LiveData<List<LocalTripEntity>> getAllTrips();

    @Query("SELECT trip_id FROM localtriptable")
    String getTrip_id();

    @Query("SELECT probablePotholeCount FROM localtriptable")
    int getProbablePotholeCount();

    @Query("SELECT definitePotholeCount FROM localtriptable")
    int getDefinitePotholeCount();

    @Query("SELECT startTime FROM localtriptable")
    String getStartTime();

    @Query("SELECT duration FROM localtriptable")
    long getDuration();

    @Query("SELECT distanceInKM FROM localtriptable")
    float getDistanceINKM();

    @Query("SELECT filesize FROM localtriptable")
    long getFileSize();

    @Query("SELECT user_id FROM localtriptable")
    String getUser_id();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrip(LocalTripEntity trip);

    @Query("DELETE FROM localtriptable")
    void deleteAll();

    @Query("SELECT * FROM localtriptable WHERE uploaded = 0")
    LiveData<List<LocalTripEntity>> getAllOfflineTrips();

    @Query("SELECT * FROM localtriptable ORDER BY (probablePotholeCount) DESC")
    LiveData<LocalTripEntity> getHighestPotholeTrip();
}
