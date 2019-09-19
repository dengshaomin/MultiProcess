package com.code.multiprocess;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.code.multiprocess.IMyAidlInterface.Stub;

public class MainActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();

    IMyAidlInterface mIMyAidlInterface;

    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            L.e(TAG + ":binderDied");
            if (mIMyAidlInterface != null && mIMyAidlInterface.asBinder() != null) {
                mIMyAidlInterface.asBinder().unlinkToDeath(this, 0);
                mIMyAidlInterface = null;
            }
            startService();
        }
    };

    private boolean isServiceConnectioned;

    private MessageReceiver mMessageReceiver = new MessageReceiver.Stub() {
        @Override
        public void onMessageReceived(MessageModel msg) throws RemoteException {
            L.e(TAG + ":" + msg.toString());
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.e(TAG + ":onServiceConnected");
            mIMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                mIMyAidlInterface.asBinder().linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isServiceConnectioned = true;
            regesitListener();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.e(TAG + ":onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.jump).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, RemoteActivity0.class), 0);
            }
        });
        findViewById(R.id.send).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(MainActivity.this, RemoteActivity0.class), 0);
                if (mIMyAidlInterface != null) {
                    try {
                        mIMyAidlInterface.sendTime(System.currentTimeMillis());
                        MessageModel messageModel = new MessageModel(0, System.currentTimeMillis());
                        MessageModel messageModel1 = mIMyAidlInterface.sendObject(messageModel);
                        L.e(MainActivity.class.getSimpleName() + ":" + messageModel.toString());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        findViewById(R.id.connect).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionService();
            }
        });

        findViewById(R.id.disconnect).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disConnectionService();
            }
        });

        findViewById(R.id.start).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });

        findViewById(R.id.regeist).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                regesitListener();
            }
        });
        findViewById(R.id.unregeist).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unRegesitListener();
            }
        });

        findViewById(R.id.kill).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                unRegesitListener();
                if (mIMyAidlInterface != null) {
                    try {
                        mIMyAidlInterface.stopCurrentModel();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void connectionService() {
        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private void disConnectionService() {
        if (isServiceConnectioned) {
            unbindService(mServiceConnection);
            isServiceConnectioned = false;
        }
    }

    private void startService() {
        startService(new Intent(this, RemoteService.class));
    }

    private void stopService() {
        stopService(new Intent(this, RemoteService.class));
    }

    private void regesitListener() {
        if (mIMyAidlInterface != null) {
            try {
                mIMyAidlInterface.regeistListener(mMessageReceiver);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void unRegesitListener() {
        if (mIMyAidlInterface != null && mIMyAidlInterface.asBinder() != null && mIMyAidlInterface.asBinder().isBinderAlive()) {
            try {
                mIMyAidlInterface.unRegeistListener(mMessageReceiver);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disConnectionService();
        unRegesitListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
