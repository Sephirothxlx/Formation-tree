package lab;

public class TreeNode {
	private TreeNode parent;
	private TreeNode left;
	private TreeNode right;
	private String s;
	private int floor;

	public TreeNode() {

	}

	public TreeNode(String s) {
		this.s = s;
	}

	public TreeNode getParent() { // 得到父节点
		return this.parent;
	}

	public void setParent(TreeNode parent) { // 设置父节点
		this.parent = parent;
	}

	public TreeNode getLeft() { // 得到左孩子
		return this.left;
	}

	public void setLeft(TreeNode left) { // 设置左孩子
		this.left = left;
	}

	public TreeNode getRight() { // 得到右孩子
		return this.right;
	}

	public void setRight(TreeNode right) { // 设置右孩子
		this.right = right;
	}

	public String getString() { // 得到string
		return this.s;
	}

	public void setWord(String s) { // 设置string
		this.s = s;
	}

	public int getFloor() { // 得到层数
		return this.floor;
	}

	public void setFloor(int floor) { // 设置层数
		this.floor = floor;
	}

}
