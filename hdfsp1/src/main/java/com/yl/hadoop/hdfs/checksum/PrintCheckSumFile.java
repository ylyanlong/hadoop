package com.yl.hadoop.hdfs.checksum;

import org.apache.hadoop.hdfs.server.datanode.BlockMetadataHeader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PrintCheckSumFile {
    private static final String checksumfile = "D:\\downLoad\\CRT\\blk_1073868789_127972.meta";

    public static void main(String[] args){
        DataInputStream input = null;
        BlockMetadataHeader header = null;

        try {
            input = new DataInputStream(new FileInputStream(checksumfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            header = BlockMetadataHeader.readHeader(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // header.getHeaderSize();


        System.out.println("version:" + header.getVersion());
        System.out.println("checksum:" + header.getChecksum());
    }
}
