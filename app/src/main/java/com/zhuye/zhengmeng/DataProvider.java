package com.zhuye.zhengmeng;


import com.zhuye.zhengmeng.home.bean.BannerDto;

import java.util.ArrayList;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class DataProvider {


//
//    public static List<Material> getMaterialList(int page) {
//        ArrayList<Material> arr = new ArrayList<>();
//        if (page == 12) return arr;
//
//        arr.add(new Material("微积分", "http://i2.hdslb.com/52_52/user/61175/6117592/myface.jpg", "侯鹏成", "中国教育出版社"));
//        arr.add(new Material("线性代数", "http://i1.hdslb.com/52_52/user/6738/673856/myface.jpg", "侯鹏成", "北京大学出版社"));
//        arr.add(new Material("高等数学", "http://i1.hdslb.com/account/face/1467772/e1afaf4a/myface.png", "侯鹏成", "中国教育出版社"));
//        arr.add(new Material("工程制图", "http://i0.hdslb.com/52_52/user/18494/1849483/myface.jpg", "侯鹏成", "中国教育出版社"));
//        arr.add(new Material("通信原理", "http://i0.hdslb.com/52_52/account/face/4613528/303f4f5a/myface.png", "侯鹏成", "中国教育出版社"));
//        arr.add(new Material("汇编语言", "http://i0.hdslb.com/52_52/account/face/611203/76c02248/myface.png", "侯鹏成", "中国教育出版社"));
//        arr.add(new Material("数据结构", "http://i2.hdslb.com/52_52/user/46230/4623018/myface.jpg", "侯鹏成", "北京大学出版社"));
//        arr.add(new Material("多媒体通信", "http://i2.hdslb.com/52_52/user/66723/6672394/myface.jpg", "侯鹏成", "中国教育出版社"));
//        arr.add(new Material("电子电路", "http://i1.hdslb.com/user/3039/303946/myface.jpg", "侯鹏成", "北京大学出版社"));
//        arr.add(new Material("大学英语", "http://i2.hdslb.com/account/face/9034989/aabbc52a/myface.png", "侯鹏成", "中国教育出版社"));
//        arr.add(new Material("算法导论", "http://i0.hdslb.com/account/face/1557783/8733bd7b/myface.png", "侯鹏成", "北京大学出版社"));
//        arr.add(new Material("大学计算机基础", "http://i2.hdslb.com/user/3716/371679/myface.jpg", "侯鹏成", "北京大学出版社"));
//        arr.add(new Material("C语言程序算法", "http://i1.hdslb.com/account/face/9045165/4b11d894/myface.png", "侯鹏成", "中国教育出版社"));
//        return arr;
//    }
//
//    public static List<Numbers> getNumberList(int page) {
//        ArrayList<Numbers> arr = new ArrayList<>();
//        if (page == 12) return arr;
//
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/52_52/user/61175/6117592/myface.jpg", "123444", "叫我华哥", "10%", "1", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/52_52/user/6738/673856/myface.jpg", "123444", "侯鹏成", "10%", "2", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/account/face/1467772/e1afaf4a/myface.png", "123444", "可乐成", "10%", "3", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/52_52/user/18494/1849483/myface.jpg", "123444", "天之骄子", "10%", "4", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/52_52/account/face/4613528/303f4f5a/myface.png", "123444", "放羊娃", "10%", "5", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/52_52/account/face/611203/76c02248/myface.png", "123444", "张三丰", "10%", "6", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/52_52/user/46230/4623018/myface.jpg", "123444", "张无忌", "10%", "7", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/52_52/user/66723/6672394/myface.jpg", "123444", "张翻过", "10%", "8", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/user/3039/303946/myface.jpg", "123444", "袁崇焕", "10%", "1", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/account/face/9034989/aabbc52a/myface.png", "123444", "毛文龙", "10%", "9", "中国教育出版社"));
//        arr.add(new Numbers("29经验值", "http://i0.hdslb.com/account/face/1557783/8733bd7b/myface.png", "123444", "张承宪", "10%", "10", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i2.hdslb.com/user/3716/371679/myface.jpg", "123444", "司马坑", "10%", "11", "北京大学出版社"));
//        arr.add(new Numbers("29经验值", "http://i1.hdslb.com/account/face/9045165/4b11d894/myface.png", "123444", "李世明", "10%", "12", "中国教育出版社"));
//        return arr;
//    }
//
//    public static List<Notification> getNotifiList() {
//        ArrayList<Notification> arr = new ArrayList<>();
//        arr.add(new Notification("2017.02.23 9:46", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:47", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:48", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:50", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:55", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        arr.add(new Notification("2017.02.23 9:59", "【环球网报道 】据俄新社2月22日消息， 俄罗斯外交部发言人扎哈罗娃表示，俄外交部官网开设了一个栏目，专门用来揭露那些关于俄罗斯的不实消息。"));
//        return arr;
//    }
//
//    public static List<Ad> getAdList() {
//        ArrayList<Ad> arr = new ArrayList<>();
//        arr.add(new Ad("http://i2.hdslb.com/u_user/e97a1632329cac1fa6ab3362e7233a08.jpg", "http://www.bilibili.com/topic/v2/1004.html"));
//        arr.add(new Ad("http://i1.hdslb.com/u_user/80fcc32d0b5d3565377847bd9dd06dc3.jpg", "http://www.bilibili.com/topic/1003.html"));
//        arr.add(new Ad("http://i2.hdslb.com/u_user/f19f0e44328a4190a48acf503c737c50.png", "http://yoo.bilibili.com/html/activity/cq2015/index.html"));
//        arr.add(new Ad("http://i1.hdslb.com/u_user/7ee1aeadc8257f43fa6b806717c9c398.png", "http://www.bilibili.com/html/activity-acsociety.html"));
//        return arr;
//    }
//
//    public static List<Object> getPersonWithAds(int page) {
//        ArrayList<Object> arrAll = new ArrayList<>();
//        List<Ad> arrAd = getAdList();
//        int index = 0;
//        for (Person person : getPersonList(page)) {
//            arrAll.add(person);
//            //按比例混合广告
//            if (Math.random() < 0.2) {
//                arrAll.add(arrAd.get(index % arrAd.size()));
//                index++;
//            }
//        }
//
//        return arrAll;
//    }

    static final BannerDto[] VIRTUAL_PICTURE = {
            new BannerDto("忘情水", "http://o84n5syhk.bkt.clouddn.com/57154327_p0.png", 1, 123),
            new BannerDto("冰雨", "http://o84n5syhk.bkt.clouddn.com/57180221_p0.jpg", 2, 124),
            new BannerDto("算什么男人", "http://o84n5syhk.bkt.clouddn.com/57174070_p0.jpg", 3, 125),
            new BannerDto("至少还有你", "http://o84n5syhk.bkt.clouddn.com/57166531_p0.jpg", 4, 126),
            new BannerDto("为了谁", "http://o84n5syhk.bkt.clouddn.com/57151022_p0.jpg", 5, 127),
            new BannerDto("同桌的你", "http://o84n5syhk.bkt.clouddn.com/57172236_p0.jpg", 6, 127),
    };

    public static ArrayList<BannerDto> getPictures() {
        ArrayList<BannerDto> arrayList = new ArrayList<>();
        for (int i = 0; i < VIRTUAL_PICTURE.length; i++) {
            arrayList.add(VIRTUAL_PICTURE[i]);
        }
        return arrayList;
    }

    static final int[] NarrowImage = {
            R.mipmap.ktv_room_1,
            R.mipmap.ktv_room_2,
            R.mipmap.ktv_room_3,
            R.mipmap.ktv_room_4,
    };

    public static ArrayList<Integer> getNarrowImage() {
        ArrayList<Integer> arrayList = new ArrayList<>();
//        if (page == 4) return arrayList;

        for (int i = 0; i < NarrowImage.length; i++) {
            arrayList.add(NarrowImage[i]);
        }
        return arrayList;
    }
    static final int[] BankImage = {
            R.mipmap.bank_back_1,
            R.mipmap.bank_back_2,
            R.mipmap.bank_back_3,
            R.mipmap.bank_back_4,
    };

    public static ArrayList<Integer> getBankBackImage() {
        ArrayList<Integer> arrayList = new ArrayList<>();
//        if (page == 4) return arrayList;

        for (int i = 0; i < BankImage.length; i++) {
            arrayList.add(BankImage[i]);
        }
        return arrayList;
    }
}
