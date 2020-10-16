package com.blazeautomation.connected_ls_sample.localdatabase;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM photomodel")
    List<PhotoModel> getAllHubs();

    @Insert
    void insertHubs(PhotoModel task);

    @Delete
    void deleteHubs(PhotoModel task);

    @Update
    void updateHubs(PhotoModel task);

    @Query("DELETE FROM photomodel")
     void deleteAudio();

}
