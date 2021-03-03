package n9e.hr.model;

public class Node {
	private long id;
	private long pid;
	private String name;
	private String path;
	private long leaf;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getLeaf() {
		return leaf;
	}

	public void setLeaf(long leaf) {
		this.leaf = leaf;
	}

}
