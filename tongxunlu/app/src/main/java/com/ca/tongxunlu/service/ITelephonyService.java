package com.ca.tongxunlu.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telephony.NeighboringCellInfo;

import com.android.internal.telephony.ITelephony;

import java.util.List;

/**
 * Created by Administrator on 2016/6/18.
 */
public class ITelephonyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ITelephony.Stub() {


            @Override
            public void dial(String number) throws RemoteException {

            }

            @Override
            public void call(String number) throws RemoteException {

            }

            @Override
            public boolean showCallScreen() throws RemoteException {
                return false;
            }

            @Override
            public boolean showCallScreenWithDialpad(boolean showDialpad) throws RemoteException {
                return false;
            }

            @Override
            public boolean endCall() throws RemoteException {
                return false;
            }

            @Override
            public void answerRingingCall() throws RemoteException {

            }

            @Override
            public void silenceRinger() throws RemoteException {

            }

            @Override
            public boolean isOffhook() throws RemoteException {
                return false;
            }

            @Override
            public boolean isRinging() throws RemoteException {
                return false;
            }

            @Override
            public boolean isIdle() throws RemoteException {
                return false;
            }

            @Override
            public boolean isRadioOn() throws RemoteException {
                return false;
            }

            @Override
            public boolean isSimPinEnabled() throws RemoteException {
                return false;
            }

            @Override
            public void cancelMissedCallsNotification() throws RemoteException {

            }

            @Override
            public boolean supplyPin(String pin) throws RemoteException {
                return false;
            }

            @Override
            public boolean handlePinMmi(String dialString) throws RemoteException {
                return false;
            }

            @Override
            public void toggleRadioOnOff() throws RemoteException {

            }

            @Override
            public boolean setRadio(boolean turnOn) throws RemoteException {
                return false;
            }

            @Override
            public void updateServiceLocation() throws RemoteException {

            }

            @Override
            public void enableLocationUpdates() throws RemoteException {

            }

            @Override
            public void disableLocationUpdates() throws RemoteException {

            }

            @Override
            public int enableApnType(String type) throws RemoteException {
                return 0;
            }

            @Override
            public int disableApnType(String type) throws RemoteException {
                return 0;
            }

            @Override
            public boolean enableDataConnectivity() throws RemoteException {
                return false;
            }

            @Override
            public boolean disableDataConnectivity() throws RemoteException {
                return false;
            }

            @Override
            public boolean isDataConnectivityPossible() throws RemoteException {
                return false;
            }

            @Override
            public Bundle getCellLocation() throws RemoteException {
                return null;
            }

            @Override
            public List<NeighboringCellInfo> getNeighboringCellInfo() throws RemoteException {
                return null;
            }

            @Override
            public int getCallState() throws RemoteException {
                return 0;
            }

            @Override
            public int getDataActivity() throws RemoteException {
                return 0;
            }

            @Override
            public int getDataState() throws RemoteException {
                return 0;
            }
        };
    }

}
