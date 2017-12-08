package com.zhuye.zhengmeng.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hpc on 2017/11/25.
 */

public class GetLocalMp3Utils {
    // 获取当前目录下所有的mp4文件
    public static ArrayList<String> GetMp3FileName(String fileAbsolutePath) {
        ArrayList<String> Mp3File = new ArrayList<String>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        if (subFile!=null){
            for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
                // 判断是否为文件夹
                if (!subFile[iFileLength].isDirectory()) {
                    String filename = subFile[iFileLength].getName();
                    // 判断是否为MP4结尾
                    if (filename.trim().toLowerCase().endsWith(".mp3")) {
                        Mp3File.add(filename);
                    }
                }
            }
        }

        return Mp3File;
    }

}
