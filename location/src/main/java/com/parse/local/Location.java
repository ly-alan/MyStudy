package com.parse.local;


import com.parse.local.ui.FileSelectorDialog;

import javax.swing.SwingUtilities;

public class Location {

    public static void main(String[] args) {
        // 创建并显示GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileSelectorDialog().setVisible(true);
            }
        });
    }


    // 测试使用
//    public static void main(String[] args) {
//
////        String targetBranch = "中国建设银行阿克苏塔中路支行";
////        String targetBranch = "中国招商银行深纺支行";
////        String targetBranch = "中国农业银行重庆江津白沙支行";
//        String targetBranch = "中国招商银行深纺大厦支行";
//        AmapPoi result = BankBranchLocator.searchBankBranch(targetBranch);
//
//        if (result != null) {
//            System.out.println("查询成功:" + targetBranch);
//            System.out.println("支行名称: " + result.name);
//            System.out.println("详细地址: " + result.getFullAddress());
//        } else {
//            System.out.println("未找到该支行信息");
//        }
//    }

}