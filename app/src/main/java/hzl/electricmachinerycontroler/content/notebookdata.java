package hzl.electricmachinerycontroler.content;

import cn.bmob.v3.BmobObject;

/**
 * Created by YLion on 2015/7/21.
 */
public class notebookdata extends BmobObject {
	public Integer id;
	public String type;
	public String content;
	public Integer version;

	public notebookdata() {
	}

	public notebookdata(String tableName, Integer id, String type, String content, Integer version) {

		this.id = id;
		this.type = type;
		this.content = content;
		this.version = version;
	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}

	public Integer getVersion() {
		return version;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
