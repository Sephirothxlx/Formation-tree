package lab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {
	/* 存储命题的string数组 */
	private String[] s;
	/* 命题个数，用于数组的循环 */
	private int i = 0;
	private JMenuBar menubar = new JMenuBar();
	private JMenu readFile = new JMenu("读入");
	private JMenuItem menuItem = new JMenuItem("读入txt");
	private Panel panel = new Panel();
	private JScrollPane scrollPane = new JScrollPane(panel);
	private JButton button = new JButton("绘制");
	/* 树是static的，因为panel类的paint需要 */
	public static Tree t = new Tree();
	/* signal信号，用于代表是否可以执行画图，即该命题是否可以被构造成树 */
	public static int signal = 0;

	public MainFrame() {
		this.setLayout(null);
		menubar.add(readFile);
		readFile.add(menuItem);
		button.setBounds(0, 0, 50, 20);
		menuItem.addActionListener(new l0());
		button.addActionListener(new l1());
		scrollPane.setBounds(10, 25, 980, 620);
		this.add(button);
		this.add(scrollPane);
		this.setJMenuBar(menubar);
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setTitle("formation tree");
		frame.setSize(1000, 700);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class l0 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.showDialog(new JLabel(), "选择");
			File file = jfc.getSelectedFile();
			try {
				BufferedReader bf0 = new BufferedReader(new FileReader(file));
				BufferedReader bf1 = new BufferedReader(new FileReader(file));
				int i = 0;
				while (bf0.readLine() != null)
					i++;
				s = new String[i];
				String a;
				i = 0;
				while ((a = bf1.readLine()) != null) {
					s[i] = a;
					i++;
				}
				bf0.close();
				bf1.close();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "请读入文件", "消息", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public class l1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (s == null)
				JOptionPane.showMessageDialog(null, "请读入文件", "消息", JOptionPane.INFORMATION_MESSAGE);
			else {
				if (i >= s.length) {
					JOptionPane.showMessageDialog(null, "后面没有了", "消息", JOptionPane.INFORMATION_MESSAGE);
					s = null;
					i = 0;
				} else {
					String x = s[i];
					/* 初步检查组分是否正确，不考虑组分的组合方式 */
					try {
						if (!t.checkComponent(x)) {
							signal = 1;
							JOptionPane.showMessageDialog(null, s[i]+":命题格式不正确。", "消息", JOptionPane.INFORMATION_MESSAGE);
						} else {
							String[] xx = x.split(" ");
							x = "";
							/* 去空格操作 */
							for (int l = 0; l < xx.length; l++)
								x = x + xx[l];
							t = new Tree(x);
							/* 构造树 */
							t = t.formationTree();
							signal = 0;
						}
					} catch (Exception e1) {
						signal = 1;
						JOptionPane.showMessageDialog(null, s[i]+":命题格式不正确。", "消息", JOptionPane.INFORMATION_MESSAGE);
					}
					repaint();
					i++;
				}
			}
		}

	}
}
