package com.huaan.bd.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class HDFSOperation {
    public static void main(String[] args) throws IOException {
        //readFile();
        writeToFile();
        //appendToFile();
    }


    private static void writeToFile() throws IOException {

        String uri1 = "/user/mai/passwd.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri1), conf);
        String uri2 = "/user/mai/passwd2.txt";
        FileSystem fs2 = FileSystem.get(URI.create(uri2), conf);

        System.out.println("dfs.support.append : " + fs2.getConf().get("dfs.support.append"));

        InputStream in = fs.open(new Path(uri1));
        // 覆盖
        //OutputStream out = fs2.create(new Path(uri2));
        // 追加
        OutputStream out = fs2.append(new Path(uri2));
        // 覆盖uri2的内容
        IOUtils.copyBytes(in, out, 4096, true);
    }



    private static void readFile() throws IOException {
        //1：查看hdfs集群服务器/user/mai/passwd.txt的内容
        String uri = "hdfs://192.168.149.131:9000/user/mai/passwd.txt";
        //String uri = "/user/mai/passwd.txt";
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        InputStream in = null;
        try {
            in = fs.open(new Path(uri));
            IOUtils.copyBytes(in, System.out, 4096, false);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(in);
        }
    }


}
