package com.connectingTrain;

import com.connectingTrain.Entities.TrainDAO;
import com.connectingTrain.database.BerkleyDb;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

import java.net.URISyntaxException;

public class Application {
    static BerkleyDb dbEnv = null;

    public static void main(String[] args) throws URISyntaxException {
        StoreConfig storeConfig = new StoreConfig();
        EntityStore store = null;
        try {
            dbEnv = new BerkleyDb();
            dbEnv.setUp();
            storeConfig.setAllowCreate(false);
            store = new EntityStore(dbEnv.getEnv(), "connectingTrains",storeConfig);
            //storeConfig.setTransactional(true);
           PrimaryIndex<String,TrainDAO> index = store.getPrimaryIndex(String.class, TrainDAO.class);
           //index.put(new Train("8","first Train"));
           //store.sync();
           //Train train = index.get("1");
           System.out.println(index.count());
        }
        catch (DatabaseException dbe){
            dbe.printStackTrace();
        }
        finally {
            store.close();
            dbEnv.close();
        }
    }


}
