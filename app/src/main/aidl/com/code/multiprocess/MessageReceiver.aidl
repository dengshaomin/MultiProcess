// MessageReceiver.aidl
package com.code.multiprocess;

// Declare any non-default types here with import statements
import com.code.multiprocess.MessageModel;
interface MessageReceiver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onMessageReceived(in MessageModel msg);
}
