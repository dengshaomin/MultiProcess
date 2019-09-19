// IMyAidlInterface.aidl
package com.code.multiprocess;
// Declare any non-default types here with import statements
import com.code.multiprocess.MessageModel;
import com.code.multiprocess.MessageReceiver;
interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void sendTime( long along);

    MessageModel sendObject( inout MessageModel messageModel);

    void regeistListener(MessageReceiver messageReceiver);
    void unRegeistListener(MessageReceiver messageReceiver);
    void stopCurrentModel();
}
