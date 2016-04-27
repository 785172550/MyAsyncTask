package com.sean.myasync;

/**
 * Created by Administrator on 2016/4/25.
 */
public class NewsBean {

	private String title;
	private String content;
	private String icon_url;

	public NewsBean() {
	}

	public NewsBean(String title, String content, String icon_url) {
		this.title = title;
		this.content = content;
		this.icon_url = icon_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String geticon_url() {
		return icon_url;
	}

	public void seticon_url(String icon_url) {
		this.icon_url = icon_url;
	}
}
