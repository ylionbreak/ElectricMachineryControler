package hzl.electricmachinerycontroler.content;

import cn.bmob.v3.BmobObject;

/**
 * Created by YLion on 2015/7/21.
 */
public class TuiContent extends BmobObject{
	String content;
	int num;
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
