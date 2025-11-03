package com.parse.local.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class FileSelectorDialog extends JFrame {

    private JTextField filePathField;
    private JTextField inputField1;
    private JTextField inputField2;
    private JTextField inputField3;
    private JButton selectButton;
    private JButton confirmButton;
    private JLabel statusLabel;
    private File selectedFile;

    public FileSelectorDialog() {
        initializeUI();
    }

    private void initializeUI() {
        // 设置窗口属性
        setTitle("解析excel中银行地址");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null); // 窗口居中

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // 底部状态面板
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        // 添加事件监听器
        setupEventListeners();

        // 设置主面板
        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // 文件选择部分
        JLabel fileLabel = new JLabel("选择Excel文件:");
        filePathField = new JTextField();
        filePathField.setEditable(false);
        selectButton = new JButton("浏览...");

        // 输入框部分
        JLabel label1 = new JLabel("解析所在信息列（A/B/C）:");
        inputField1 = new JTextField();

        JLabel label2 = new JLabel("支行名填入列(D/E/F):");
        inputField2 = new JTextField();

        JLabel label3 = new JLabel("所在省&市填入(D/E/F):");
        inputField3 = new JTextField();

        // 确认按钮
        confirmButton = new JButton("开始处理");

        // 添加到面板
        panel.add(fileLabel);
        panel.add(createFileSelectionPanel());

        panel.add(label1);
        panel.add(inputField1);

        panel.add(label2);
        panel.add(inputField2);

        panel.add(label3);
        panel.add(inputField3);

        // 空标签占位
        panel.add(new JLabel());
        panel.add(confirmButton);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("操作状态"));

        statusLabel = new JLabel("就绪");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(statusLabel, BorderLayout.CENTER);

//        // 使用JTextArea代替JLabel，支持多行文本
//        statusLabel = new JTextArea(3, 1); // 3行，20列
//        statusLabel.setEditable(false); // 设置为只读
//        statusLabel.setLineWrap(true); // 自动换行
//        statusLabel.setWrapStyleWord(true); // 按单词换行
//        statusLabel.setBackground(panel.getBackground()); // 与面板背景一致
//
//        // 设置边框和边距
//        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//
//        panel.setPreferredSize(new Dimension(0, 50)); // 固定高度100像素

        return panel;
    }

    private JPanel createFileSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(filePathField, BorderLayout.CENTER);
        panel.add(selectButton, BorderLayout.EAST);
        return panel;
    }

    private void setupEventListeners() {
        // 文件选择按钮事件
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectExcelFile();
            }
        });

        // 确认按钮事件
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmProcessing();
            }
        });
    }

    private void selectExcelFile() {
        JFileChooser fileChooser = new JFileChooser();

        // 设置文件过滤器，只显示Excel文件
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
            }

            @Override
            public String getDescription() {
                return "Excel文件 (*.xls, *.xlsx)";
            }
        });

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            updateStatus("已选择文件: " + selectedFile.getName());
        }
    }

    private void confirmProcessing() {
        // 验证文件是否选择
        if (selectedFile == null) {
            showWarning("请先选择Excel文件！");
            return;
        }

        // 获取输入框内容
        String input1 = inputField1.getText().trim();
        String blankName = inputField2.getText().trim();
        String province = inputField3.getText().trim();

        // 验证必填字段（示例）
        if (input1.isEmpty()) {
            showWarning("请输入待解析列！");
            return;
        }

        // 显示确认对话框
        String message = buildConfirmationMessage(input1, blankName, province);

        int confirmResult = JOptionPane.showConfirmDialog(
                this,
                message,
                "请确认信息",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmResult == JOptionPane.YES_OPTION) {
            // 用户点击了"是"，在后台线程中执行处理逻辑
            startBackgroundProcessing(input1, blankName, province);
        } else {
            // 用户点击了"否"或关闭对话框
            updateStatus("用户取消操作");
        }
    }

    private void startBackgroundProcessing(final String input1, final String blankName, final String province) {
        // 禁用按钮，防止重复操作
        setControlsEnabled(false);
        updateStatus("开始处理数据...");

        // 使用SwingWorker在后台执行耗时操作
        final SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {

                ParseExcelBlank.processExcelFile(selectedFile, input1, blankName, province, new ParseExcelBlank.StatusCallback() {
                    @Override
                    public void updateStatus(String status) {
                        System.out.println(status);
                        publish(status);
                    }

                    @Override
                    public void updateError(String status) {
                        System.out.println("处理失败: " + status);
                        publish("处理失败: " + status);
                    }
                });
                return true;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // 更新状态文字
                if (!chunks.isEmpty()) {
                    String status = chunks.get(chunks.size() - 1);
                    statusLabel.setText(status);
                }
            }

            @Override
            protected void done() {
//                try {
//                    Boolean success = get();
//                    if (success) {
//                        showSuccess("数据处理完成！\n文件: " + selectedFile.getName());
//                    } else {
//                        showError("处理过程中出现错误");
//                    }
//                } catch (InterruptedException | ExecutionException e) {
//                    showError("处理异常: " + e.getMessage());
//                } finally {
                // 恢复界面状态
                setControlsEnabled(true);
//                }
            }
        };

        worker.execute();
    }

    private void setControlsEnabled(boolean enabled) {
        selectButton.setEnabled(enabled);
        confirmButton.setEnabled(enabled);
        inputField1.setEnabled(enabled);
        inputField2.setEnabled(enabled);
        inputField3.setEnabled(enabled);

        if (enabled) {
            confirmButton.setText("开始处理");
        } else {
            confirmButton.setText("处理中...");
        }
    }

    private void updateStatus(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText(message);
                System.out.println("状态: " + message);
            }
        });
    }

    private String buildConfirmationMessage(String input1, String blankName, String province) {
        StringBuilder message = new StringBuilder();
        message.append("请确认以下信息：\n\n");
        message.append("Excel文件: ").append(selectedFile.getName()).append("\n");
        message.append("解析列: ").append(input1).append("\n");
        message.append("银行名保存到: ").append(blankName).append("\n");
        message.append("所在省保存到: ").append(province).append("\n");
        message.append("处理过程可能需要一些时间，是否继续？");

        return message.toString();
    }

    // 工具方法：显示各种消息对话框
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "警告", JOptionPane.WARNING_MESSAGE);
        updateStatus("警告: " + message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
        updateStatus("错误: " + message);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "成功", JOptionPane.INFORMATION_MESSAGE);
        updateStatus("成功: " + message);
    }

}