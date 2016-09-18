package com.ca.tongxunlu.service;


import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by haizeiym
 * on 2016/9/18
 */
public class NumLocation {
//    public static void getAddress(final Context context, final String phoneNum) {
//        final String path = "http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx";//发送xml文件过去的地址
//        new Thread() {//关于涉及网络的业务,一律异步执行
//            @Override
//            public void run() {
//                // TODO 自动生成的方法存根
//                super.run();
//                try {
//                    //首先读取xml文件内容,替换掉我需要的电话号码,转化为byte[]类型
//                    InputStream is = context.getAssets().open("query.xml");
//                    byte[] buffer = new byte[1024];
//                    ByteArrayOutputStream os = new ByteArrayOutputStream();
//                    if (is.read(buffer) != (-1)) {
//                        os.write(buffer);
//                    }
//                    String strData = os.toString();
//                    String data_ok = strData.replaceAll("\\$mobile", phoneNum);
//                    byte[] data = data_ok.getBytes();
//                    is.close();
//                    os.close();
//                    //建立连接,利用post方式将xml文件数据发送过去
//                    URL url = new URL(path);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setConnectTimeout(5000);
//                    connection.setRequestMethod("POST");
//                    connection.setDoOutput(true);
//                    connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//                    connection.setRequestProperty("Content-Length", String.valueOf(data.length));
//                    //发送xml数据
//                    OutputStream os2 = connection.getOutputStream();
//                    os2.write(data);
//
//                    //解析返回的数据,返回了的数据是xml格式的
//                    if (connection.getResponseCode() == 200) {
//                        InputStream in = connection.getInputStream();
//                        XmlPullParser parser = Xml.newPullParser();
//                        parser.setInput(in, "UTF-8");
//                        int event = parser.getEventType();
//                        while (event != XmlPullParser.END_DOCUMENT) {
//                            switch (event) {
//                                case XmlPullParser.START_TAG:
//                                    if ("getMobileCodeInfoResult".equals(parser.getName())) {
////                                        parser.next();
////                                        Message msg = handler.obtainMessage();
////                                        msg.obj = parser.getText();
////                                        msg.sendToTarget();
//                                        Log.e("haomadata", parser.getText() + "");
//
//                                    }
//                                    break;
//                            }
//                            event = parser.next();
//                        }
//                    }
//                    os2.close();
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
//        }.start();
//    }


    public static String getMobileAddress(Context context, String mobile) {

        try {
            InputStream inStream = context.getClassLoader().getResourceAsStream("query.xml");
            byte[] data = new StreamTool().readInputStream(inStream);
            String xml = new String(data);
            String soap = xml.replaceAll("\\$mobile", mobile);

            /**
             * 正则表达式$为特殊正则中的特殊符号须转义，即\$mobile
             * 而\为字符串中的特殊符号，所以用两个反斜杠，即"\\{1}quot;
             */
            String path = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";
            data = soap.getBytes();// 得到了xml的实体数据
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type",
                    "application/soap+xml; charset=utf-8");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream outStream = conn.getOutputStream();
            outStream.write(data);
            outStream.flush();
            outStream.close();
            if (conn.getResponseCode() == 200) {
                InputStream responseStream = conn.getInputStream();
                return parseXML(responseStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析返回xml数据
     *
     * @param responseStream
     * @return
     * @throws Exception
     */
    private static String parseXML(InputStream responseStream) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(responseStream, "UTF-8");
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:
                    if ("getMobileCodeInfoResult".equals(parser.getName())) {
                        return parser.nextText();
                    }
                    break;
            }
            event = parser.next();
        }
        return null;
    }

    static class StreamTool {
        /**
         * 从输入流读取数据
         *
         * @param inStream
         * @return
         * @throws Exception
         */
        public byte[] readInputStream(InputStream inStream) throws Exception {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            return outSteam.toByteArray();
        }
    }
}
