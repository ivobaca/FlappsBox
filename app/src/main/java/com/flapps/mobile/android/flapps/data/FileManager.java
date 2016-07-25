package com.flapps.mobile.android.flapps.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class FileManager {

    Context context;

    public FileManager(Context context) {
        this.context = context;
    }

    protected boolean exists( String filename) {
        File file = context.getFileStreamPath(filename);
        return file.exists();
    }

    protected Object readObject(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream read= new ObjectInputStream (new FileInputStream( context.getFilesDir().toString()+ "/" + filename));
        Object obj = read.readObject();
        read.close();

        return obj;
    }

    protected void saveObject( Object obj, String filename ) throws IOException {

        ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream( context.getFilesDir().toString()+ "/" + filename));
        write.writeObject(obj);
        write.close();
    }

    protected static boolean delete(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        return file.delete();
    }
}
