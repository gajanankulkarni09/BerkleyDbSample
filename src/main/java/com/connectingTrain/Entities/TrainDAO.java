package com.connectingTrain.Entities;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class TrainDAO {
    @PrimaryKey
    private String trainId;
    private String trainName;

    public TrainDAO() {
    }

    public TrainDAO(String trainId, String trainName) {
        this.trainId = trainId;
        this.trainName = trainName;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

}
