package lab;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Panel extends JPanel {
	public Panel() {
		this.setLayout(null);
	}

	/* 这个方法使用递归的方式画每一个节点 */
	public void paintNode(Graphics g, TreeNode tn, int x0, int y0, int x1, int y1) {
		if (tn != null) {
			/* 本身的节点可能没有父节点，于是直接画不连线 */
			if (tn.getParent() == null) {
				g.drawString(tn.getString(), x1 - (tn.getString().length() * 7) / 2, y1);
			} else {
				/* 有父节点，画出string还要连线 */
				g.drawString(tn.getString(), x1 - (tn.getString().length() * 7) / 2, y1);
				g.drawLine(x0, y0, x1, y1 - 15);
			}
			/* 不是\not的情况，左右两个儿子都要递归 */
			if (tn.getLeft() != null && tn.getRight() != null) {
				paintNode(g, tn.getLeft(), x1, y1, x1 - tn.getLeft().getString().length() * 7 / 2 - 20, y1 + 50);
				paintNode(g, tn.getRight(), x1, y1, x1 + tn.getRight().getString().length() * 7 / 2 + 20, y1 + 50);
			}
			/* 只递归左儿子 */
			else if (tn.getLeft() != null && tn.getRight() == null)
				paintNode(g, tn.getLeft(), x1, y1, x1, y1 + 50);
		}
	}

	/* 重载paint，在其中根据树的大小设置panel的大小 */
	public void paint(Graphics g) {
		super.paint(g);
		this.setPreferredSize(new Dimension(getW()[0] + getW()[1], getH()));
		this.revalidate();
		/* 信号0时，画 */
		/* 信号1时，不画 */
		if (MainFrame.signal == 0)
			/* 根节点的中点是x=左宽的位置 */
			paintNode(g, MainFrame.t.getRoot(), getW()[0], 50, getW()[0], 50);
	}

	/* 得到panel宽度，从根节点的中点处分为左右两个宽度，所以返回一个数组 */
	public int[] getW() {
		int[] xx = { 0, 0 };
		/* 第一次打开面板时执行的代码 */
		if (MainFrame.t.getRoot() == null)
			return xx;
		/* 调用repaint时执行的代码 */
		TreeNode tn = MainFrame.t.getRoot();
		int x = tn.getString().length() * 7;
		int leftX = -x / 2, rightX = x / 2;
		int leftCenter = 0, rightCenter = 0, temp = 0;
		/* 左宽的获得方式时，找到最左边的叶节点的x，并与根节点x比较 */
		while ((tn = tn.getLeft()) != null) {
			temp = leftCenter - tn.getString().length() * 7 / 2 - 20;
			if (temp - tn.getString().length() * 7 / 2 < leftX)
				leftX = temp - tn.getString().length() * 7 / 2;
			leftCenter = temp;
		}
		tn = MainFrame.t.getRoot();
		/* 右宽获得方式类同左宽 */
		while ((tn = tn.getRight()) != null) {
			temp = rightCenter + tn.getString().length() * 7 / 2 + 20;
			if (temp + tn.getString().length() * 7 / 2 > rightX)
				rightX = temp + tn.getString().length() * 7 / 2;
			rightCenter = temp;
		}
		xx[0] = -leftX;
		xx[1] = rightX;
		return xx;
	}

	/* 得到高度，这个根据树的层数获得 */
	public int getH() {
		return MainFrame.t.getFloor() * 50 + 50;
	}
}
