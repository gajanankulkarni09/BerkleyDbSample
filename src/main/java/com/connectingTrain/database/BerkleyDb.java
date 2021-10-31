package com.connectingTrain.database;

import com.connectingTrain.Application;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentMutableConfig;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class BerkleyDb {
     Environment dbEnivironement = null;
     EnvironmentConfig environmentConfig = null;

    public  void setUp() throws URISyntaxException {
        //URL resource = BerkleyDb.class.getResource("/dbEnv");
        //String fileName = Paths.get(resource.toURI()).toString();
        try {
            environmentConfig = new EnvironmentConfig();
            EnvironmentMutableConfig environmentMutableConfig = new EnvironmentMutableConfig();
            environmentMutableConfig.setCacheSize(1000000000);
            environmentConfig.setAllowCreate(false);
            dbEnivironement = new Environment(new File("/home/gajanan/data/projects/java/dbEnv"), environmentConfig);
            dbEnivironement.setMutableConfig(environmentMutableConfig);
            environmentMutableConfig.setTxnWriteNoSyncVoid(false);
        }
        catch (DatabaseException dbe){
dbe.printStackTrace();
        }
    }

    public Environment getEnv() {
        return dbEnivironement;
    }

    public void close(){
        try {
            if (dbEnivironement != null)
                dbEnivironement.close();
        }
        catch (DatabaseException dbe){
            System.err.println("Error closing environment" +
                    dbe.toString());
        }
    }
}
