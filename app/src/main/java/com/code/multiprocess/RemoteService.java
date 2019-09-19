package com.code.multiprocess;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.code.multiprocess.IMyAidlInterface.Stub;

public class RemoteService extends Service {

    Stub mStub;

    Timer mTimer;

    TimerTask mTimerTask;

    private RemoteCallbackList<MessageReceiver> mMessageReceiverList = new RemoteCallbackList<>();

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        L.e(this.getClass().getSimpleName() + ":onBind");
        if (mStub == null) {
            mStub = new Stub() {
                @Override
                public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
                }

                @Override
                public void sendTime(long along) throws RemoteException {
                    L.e(this.getClass().getSimpleName() + ":" + along);
                }

                @Override
                public MessageModel sendObject(MessageModel messageModel) throws RemoteException {
                    L.e(this.getClass().getSimpleName() + ":" + messageModel.toString());
                    messageModel.setIndex(messageModel.getIndex() + 1);
                    return messageModel;
                }

                @Override
                public void regeistListener(MessageReceiver messageReceiver) throws RemoteException {
                    L.e(this.getClass().getSimpleName() + ":" + messageReceiver);
                    if (messageReceiver == null) {
                        return;
                    }
                    mMessageReceiverList.register(messageReceiver);
                }

                @Override
                public void unRegeistListener(MessageReceiver messageReceiver) throws RemoteException {
                    L.e(this.getClass().getSimpleName() + ":" + messageReceiver);
                    if (mMessageReceiverList != null) {
                        mMessageReceiverList.unregister(messageReceiver);
                    }
                }

                @Override
                public void stopCurrentModel() throws RemoteException {
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer = null;
                    }
                    if (mTimerTask != null) {
                        mTimerTask.cancel();
                        mTimerTask = null;
                    }
                    stopSelf();
                }
            };
        }
        if (mTimer == null) {
            mTimer = new Timer();
            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (mMessageReceiverList) {
                            if (mMessageReceiverList != null) {
                                int count = mMessageReceiverList.beginBroadcast();
                                for (int i = 0; i < count; i++) {
                                    MessageReceiver messageReceiver = mMessageReceiverList.getBroadcastItem(i);
                                    if (messageReceiver != null) {
                                        try {
                                            messageReceiver.onMessageReceived(new MessageModel(0, System.currentTimeMillis()));
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                mMessageReceiverList.finishBroadcast();
                            }
                        }
                    }
                };
            }
            mTimer.schedule(mTimerTask, 0, 1000);
        }
        return mStub;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.e(this.getClass().getSimpleName() + ":onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.e(this.getClass().getSimpleName() + ":onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(this.getClass().getSimpleName() + ":onDestroy");
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        L.e(this.getClass().getSimpleName() + ":unbindService");
    }
}
