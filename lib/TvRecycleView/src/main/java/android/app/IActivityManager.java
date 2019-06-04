package android.app;

import android.content.res.Configuration;
import android.os.RemoteException;

/**
 * Created by elegant on 16/3/6.
 */
public abstract interface IActivityManager {
    public abstract Configuration getConfiguration() throws RemoteException;

    public abstract void updateConfiguration(Configuration paramConfiguration)
            throws RemoteException;
}