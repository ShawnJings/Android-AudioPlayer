package com.timeapp.shawn.voiceplay.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SDCardUtils {

	public static boolean isMounted() {
		return Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState());
	}
	
    /**
     * 获取扩展SD卡存储目录
     * 
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     * 
     * @return
     */
    public static String getExternalSdCardPath(Context ctx) {
 
        if (isMounted()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }
 
        String path = null;
 
        File sdCardFile = null;
 
        String[] paths = getStorageList(ctx);
 
        for (String devMount : paths) {
            File file = new File(devMount);
 
            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();
 
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);
 
                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }
 
        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }
 
        return null;
    }
    
    @SuppressLint("InlinedApi")
	public static String[] getStorageList(Context ctx) {
    	StorageManager sm = (StorageManager) ctx.getSystemService(Activity.STORAGE_SERVICE);
    	try {
    		
    		Method mMethodGetPaths = sm.getClass().getMethod("getVolumePaths");
    		String[] paths = null;
        	paths = (String[]) mMethodGetPaths.invoke(sm);

        	return paths;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return null;
    }
    	
}
