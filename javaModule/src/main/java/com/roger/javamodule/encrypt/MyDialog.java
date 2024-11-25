//package com.roger.javamodule.encrypt;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.math.BigInteger;
//import java.nio.charset.StandardCharsets;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//import sun.misc.BASE64Encoder;
//
///**
// * @Author Roger
// * @Date 2024/2/1 16:33
// * @Description
// */
//public class MyDialog extends JFrame {
//
//    public static void main(String[] args) {
//        new MyDialog("生成字符串");
////        System.out.println("1706775624000-axzk = " + getEncrypt("1706775624","axzk"));
//    }
//
//    JLabel jlabel1, jLabel2, jLabel3;
//    JTextField jtf1, jtf2, jtf3;
//    JButton jb1, jb2;
//    JPanel pane;
//
//    /**
//     * 构造函数
//     *
//     * @param title
//     */
//    public MyDialog(String title) {
//        super(title);
//        this.getContentPane().setLayout(null);
//        this.setBounds(500, 500, 460, 460);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        pane = new JPanel();
//        pane.setBounds(10, 10, 400, 300);
//        pane.setLayout(null);
//        jlabel1 = new JLabel("过期时间(s):");
//        jlabel1.setBounds(20, 40, 100, 30);
//        jtf1 = new JTextField();
//        jtf1.setBounds(130, 40, 260, 30);
//        pane.add(jlabel1);
//        pane.add(jtf1);
//
//        jLabel2 = new JLabel("公司名称(字母):");
//        jLabel2.setBounds(20, 80, 100, 30);
//        jtf2 = new JTextField();
//        jtf2.setBounds(130, 80, 260, 30);
//        pane.add(jLabel2);
//        pane.add(jtf2);
//
//        jb1 = new JButton("加密");
//        jb1.setBounds(130, 150, 70, 30);
//        pane.add(jb1);
//
//        jb2 = new JButton("取消");
//        jb2.setBounds(220, 150, 70, 30);
//        pane.add(jb2);
//
//
//        jLabel3 = new JLabel("结果:");
//        jLabel3.setBounds(20, 180, 100, 30);
//        jtf3 = new JTextField();
//        jtf3.setBounds(20, 220, 370, 30);
//        pane.add(jLabel3);
//        pane.add(jtf3);
//
//        this.add(pane);
//
//
//        /**
//         * 处理文本内容
//         * true 包含中文字符  false 不包含中文字符
//         */
//        jb1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (jtf1.getText().trim().length() != 10) {
//                    //10位，s的时间戳。13位，毫秒
//                    JOptionPane.showMessageDialog(null, "输入时间戳（秒）错误！！！");
//                }
//                if (jtf2.getText().trim().length() == 0) {
//                    JOptionPane.showMessageDialog(null, "输入名称错误！！！");
//                }
//                //先转36进制
//                jtf3.setText(getEncrypt(jtf1.getText().trim(), jtf2.getText().trim()));
//            }
//
//        });
//        this.setVisible(true);
//    }
//
//    private static String getEncrypt(String text1, String text2) {
//        String time = new BigInteger(text1, 10).toString(36);
//        System.out.println("time = " + time);
//        String name = new BigInteger(text2, 36).toString(26);
//        System.out.println("name = " + name);
//        String result = new StringBuffer(time + "-" + name).reverse().toString();
//        System.out.println("result = " + result);
//        try {
//            String base64 = new String(new BASE64Encoder().encode(result.getBytes(StandardCharsets.UTF_8)));
//            return base64;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return "";
//        }
//    }
//
//}
