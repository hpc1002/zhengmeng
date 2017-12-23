package com.zhuye.zhengmeng.KTV.socket;

import android.content.Context;

import java.io.Serializable;

/**
 * ┏┓　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * iBoosJie.　 ┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * 创建人：贾杰
 * 创建日期： 2017/6/6
 * 说明：连接参数(构建者模式)
 * 修改：
 */
public class ConnectionConfig implements Serializable{


    private Context context;

    private String ip;

    private int port;

    private int readBufferSize;

    private long connectionTime;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public void setReadBufferSize(int readBufferSize) {
        this.readBufferSize = readBufferSize;
    }

    public long getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(long connectionTime) {
        this.connectionTime = connectionTime;
    }

    public static class Builder{

        private Context context;

        private String ip = "59.110.223.3";

        private int port = 8282;

        private int readBufferSize = 512;

        private long connectionTimeout = 10000;

        public Builder(Context context){

            this.context = context;

        }

        public Builder setIp(String ip){
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port){
            this.port = port;
            return  this;
        }

        public Builder setReadBuilder(int size){
            this.readBufferSize = size;
            return this;
        }

        public Builder setConnectionTimeout(int time){
            this.connectionTimeout = time;
            return this;
        }


        private void applyConfig(ConnectionConfig config){
            config.context = this.context;
            config.ip = this.ip;
            config.port = this.port;
            config.readBufferSize = this.readBufferSize;
            config.connectionTime = this.connectionTimeout;

        }

        public ConnectionConfig builder(){

            ConnectionConfig config = new ConnectionConfig();
            applyConfig(config);

            return config;
        }



    }
}
