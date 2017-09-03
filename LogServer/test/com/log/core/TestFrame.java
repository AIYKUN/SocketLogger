package com.log.core;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TestFrame extends JFrame {
	private JTextField textField1;
	private JPasswordField passwordField;
	private JLabel label0;
	private JLabel label1;
	private JLabel label2;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JCheckBox checkBox1;
	private JCheckBox checkBox2;
	private JCheckBox checkBox3;
	private JButton button1;
	private JLabel imageLabel;
	/**
	 * 多选列表
	 */
	private JList jList;

	/**
	 * 下拉列表
	 */
	private JComboBox comboBox1;

	/**
	 * 多行文本
	 */
	private JTextArea textArea1;
	/**
	 * 创建图片对象
	 */
	private ImageIcon image1 = new ImageIcon("07020850090639166076.gif");

	/**
	 * 初始化窗口
	 */
	public TestFrame() {
		// 设置容器为空布局，绝对定位
		this.setLayout(null);
		// 创建图片对象
		imageLabel = new JLabel(image1);
		imageLabel.setBounds(0, 0, 170, 135);
		// 创建字体对象
		Font font = new Font("宋体", Font.BOLD, 30);
		// 创建颜色对象
		Color color = new Color(128, 200, 128);

		// 设置按钮
		radioButton1 = new JRadioButton("男");
		radioButton1.setBounds(250, 190, 50, 20);
		radioButton2 = new JRadioButton("女");
		radioButton2.setBounds(400, 190, 50, 20);

		checkBox1 = new JCheckBox("Java");
		checkBox1.setBounds(210, 220, 70, 20);
		checkBox2 = new JCheckBox("C++");
		checkBox2.setBounds(330, 220, 70, 20);
		checkBox3 = new JCheckBox("C#");
		checkBox3.setBounds(450, 220, 70, 20);

		textArea1 = new JTextArea();
		// 有滚动条的容器，用来装多行文本框
		JScrollPane sp1 = new JScrollPane(textArea1);
		sp1.setBounds(230, 260, 250, 80);

		String[] str = new String[] { "四川", "江苏", "云南", "安徽" };
		comboBox1 = new JComboBox(str);
		comboBox1.setBounds(380, 350, 70, 50);

		jList = new JList(str);
		// 用滚动条的容器来装多选列表
		JScrollPane sp2 = new JScrollPane(jList);
		sp2.setBounds(240, 350, 70, 50);

		// 将单选按钮放在按钮组中，实现单选效果
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioButton1);
		bg.add(radioButton2);

		button1 = new JButton("确定");
		button1.setBounds(305, 420, 80, 20);
		// 登陆界面标签
		label0 = new JLabel("登陆界面");
		label0.setBounds(300, 50, 150, 50);
		label0.setFont(font);
		label0.setForeground(color);
		// 用户名标签
		label1 = new JLabel("用户名：");
		label1.setBounds(210, 110, 100, 20);
		// 密码标签
		label2 = new JLabel("密码：");
		label2.setBounds(210, 160, 100, 20);
		// 创建组件
		textField1 = new JTextField();
		textField1.setBounds(310, 110, 200, 20);
		// 密码框
		passwordField = new JPasswordField();
		passwordField.setBounds(310, 160, 200, 20);
		// 将组件加入到容器中
		this.add(textField1);
		this.add(passwordField);
		this.add(label0);
		this.add(label1);
		this.add(label2);
		this.add(radioButton1);
		this.add(radioButton2);
		this.add(checkBox1);
		this.add(checkBox2);
		this.add(checkBox3);
		this.add(sp1);
		this.add(comboBox1);
		this.add(sp2);
		this.add(button1);
		this.add(imageLabel);
		// 设置标题
		this.setTitle("登陆");
		// 设置窗口的关闭策略
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置窗口大小
		this.setSize(750, 500);
		// 设置窗口居中，放在窗口大小后面，null表示桌面
		this.setLocationRelativeTo(null);
		// 将窗口设置为显示,要写在最后一句
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new TestFrame();

	}
}
