package lab;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tree {
	private TreeNode root;
	private int floor;

	public Tree() {

	}

	public Tree(String s) {
		this.root = new TreeNode(s);
		this.root.setFloor(0);
	}

	public TreeNode getRoot() {
		return this.root;
	}

	public int getFloor() {
		return this.floor;
	}

	/* 检查组分 */
	public boolean checkComponent(String s) throws Exception {
		boolean b = true;
		int tag = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(')
				tag++;
			else if (s.charAt(i) == ')')
				tag--;
		}
		if (tag != 0)
			b = false;
		/*检查连接词两边是否有空格，出现与下述不同的情况都是异常*/
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i)=='\\'){
				if(s.charAt(i-1)!=' ')
					b=false;
				else
					s=s.substring(0,i-1)+"*"+s.substring(i,s.length());
				if(s.substring(i,i+3).equals("\\or")||s.substring(i,i+3).equals("\\eq")){
					if(s.charAt(i+3)!=' ')
						b=false;
					else
						s=s.substring(0,i+3)+"*"+s.substring(i+4,s.length());
				}else if(s.substring(i,i+4).equals("\\and")||s.substring(i,i+4).equals("\\not")){
					if(s.charAt(i+4)!=' ')
						b=false;
					else
						s=s.substring(0,i+4)+"*"+s.substring(i+5,s.length());
				}else if(s.substring(i,i+6).equals("\\imply")){
					if(s.charAt(i+6)!=' ')
						b=false;
					else
						s=s.substring(0,i+6)+"*"+s.substring(i+7,s.length());
				}else
					throw new Exception();
			}
		}
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			al.add(s.charAt(i) + "");
		}
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).equals("(") || al.get(i).equals(")")) {
				al.remove(i);
				i--;
			}
		}
		s = "";
		for (int i = 0; i < al.size(); i++) {
			s = s + al.get(i);
		}
		String[] ss = s.split("\\*");
		for (int i = 0; i < ss.length; i++) {
			ss[i]=ss[i].trim();
			if (ss[i].equals(""))
				continue;
			if (ss[i].charAt(0) == '\\') {
				if (!ss[i].equals("\\and") & !ss[i].equals("\\or") & !ss[i].equals("\\not") & !ss[i].equals("\\imply")
						& !ss[i].equals("\\eq"))
					b = false;
			} else if (ss[i].charAt(0) - 'A' >= 0 && ss[i].charAt(0) - 'A' <= 25) {
				Pattern p0 = Pattern.compile("[A-Z]*_\\{[1-9][0-9]*\\}");
				Pattern p1 = Pattern.compile("[A-Z]*_\\{[0-9]\\}");
				Pattern p2 = Pattern.compile("[A-Z]*");
				Matcher m0 = p0.matcher(ss[i]);
				Matcher m1 = p1.matcher(ss[i]);
				Matcher m2 = p2.matcher(ss[i]);
				if (m0.matches() == false && m1.matches() == false && m2.matches() == false)
					b = false;
			} else
				b = false;
		}
		return b;
	}

	/* 因为checkComponent的作用就是检查组分，所以这里的叶节点只要不含有括号就是命题字母 */
	public boolean checkPropositionLetter(String s) {
		boolean b = true;
		if (s.contains("("))
			b = false;
		return b;
	}

	/* 返回任意一个没有归约的叶节点 */
	public TreeNode checkLeaf(TreeNode x) {
		TreeNode y = null;
		if (x != null) {
			if (x.getLeft() == null && x.getRight() == null) {
				if (checkPropositionLetter(x.getString()) == false) {
					y = x;
				}
			}
			if (checkLeaf(x.getLeft()) != null)
				y = checkLeaf(x.getLeft());
			if (checkLeaf(x.getRight()) != null)
				y = checkLeaf(x.getRight());
		}
		return y;
	}

	/* 树的构造方式 */
	public Tree formationTree() throws Exception {
		TreeNode tn0 = checkLeaf(root);
		if (tn0 == null)
			return this;
		else {
			String a = tn0.getString();
			/* 第一个连接词是not的命题 */
			if (a.substring(1, 5).equals("\\not")) {
				TreeNode tn1 = new TreeNode(a.substring(5, a.length() - 1));
				tn0.setLeft(tn1);
				tn1.setParent(tn0);
				return formationTree();
			} else {
				/* 第一个连接词不是not的命题 */
				int x = 1;
				TreeNode tn2;
				TreeNode tn3;
				/* 下一个要解析的组分分为两种情况，第一种，是一个命题 */
				if (a.charAt(1) == '(') {
					int y = 0;
					while (x < a.length()) {
						if (a.charAt(x) == '(')
							y++;
						else if (a.charAt(x) == ')')
							y--;
						if (y == 0)
							break;
						x++;
					}
					tn2 = new TreeNode(a.substring(1, x + 1));
					while (x < a.length()) {
						if ((a.charAt(x) - 'A' >= 0 && a.charAt(x) - 'A' < 26) || a.charAt(x) == '(')
							break;
						x++;
					}
					tn3 = new TreeNode(a.substring(x, a.length() - 1));
					tn0.setLeft(tn2);
					tn0.setRight(tn3);
					tn2.setParent(tn0);
					tn3.setParent(tn0);
					tn2.setFloor(tn0.getFloor() + 1);
					tn3.setFloor(tn0.getFloor() + 1);
					if (tn2.getFloor() > floor)
						floor = tn2.getFloor();
					if (tn3.getFloor() > floor)
						floor = tn3.getFloor();
				} else if (a.charAt(1) - 'A' >= 0 && a.charAt(1) - 'A' <= 25) {
					/* 第二种，是一个命题字母 */
					while (x < a.length()) {
						if (a.charAt(x) == '\\')
							break;
						x++;
					}
					tn2 = new TreeNode(a.substring(1, x));
					while (x < a.length()) {
						if ((a.charAt(x) - 'A' >= 0 && a.charAt(x) - 'A' < 26) || a.charAt(x) == '(')
							break;
						x++;
					}
					tn3 = new TreeNode(a.substring(x, a.length() - 1));
					tn0.setLeft(tn2);
					tn0.setRight(tn3);
					tn2.setParent(tn0);
					tn3.setParent(tn0);
					/* 节点层数 */
					tn2.setFloor(tn0.getFloor() + 1);
					tn3.setFloor(tn0.getFloor() + 1);
					/* 更新树的层数 */
					if (tn2.getFloor() > floor)
						floor = tn2.getFloor();
					if (tn3.getFloor() > floor)
						floor = tn3.getFloor();
				} else
					/* 其余情况都是异常 */
					throw new Exception();
				return formationTree();
			}
		}
	}
}
