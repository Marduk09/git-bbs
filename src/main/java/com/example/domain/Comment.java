package com.example.domain;

/**
 * コメントを表すクラス.
 * 
 * @author takara.miyazaki
 *
 */
public class Comment {
	
	/** コメントID */
	private Integer id;
	/** 投稿者名 */
	private String name;
	/** コンテント */
	private String content;
	/** 記事ID */
	private Integer articleId;
	
	public Comment() {
		
	}
	public Comment(Integer id, String name, String content, Integer articId) {
		this.id = id;
		this.name = name;
		this.content = content;
		this.articleId = articId;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", name=" + name + ", content=" + content + ", articleId=" + articleId + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

}

