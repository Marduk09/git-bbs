package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Comment;

/**
 * commentsデータベースを操作するレポジトリ.
 * 
 * @author takara.miyazaki
 *
 */
@Repository
public class CommentRepository {
	
	private static final RowMapper<Comment> COMMENT_ROW_MAPPER = (rs, i) -> {
		Comment comment = new Comment();
		comment.setId(rs.getInt("id"));
		comment.setName(rs.getString("name"));
		comment.setContent(rs.getString("content"));
		comment.setArticleId(rs.getInt("article_id"));
		return comment;
	};
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	/**
	 * 指定された記事IDのコメントをすべて取得する.
	 * 
	 * @param articleId 記事ID
	 * @return コメントのリスト
	 */
	public List<Comment> findByArticleId(int articleId){
		String sql = "SELECT id, name, content, article_id FROM comments "
					+ "WHERE article_id =:articleId ORDER BY id DESC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);
		
		List<Comment> commentList = template.query(sql, param, COMMENT_ROW_MAPPER);
		return commentList;
	}
	
	/**
	 * コメントをDBに追加する.
	 * 
	 * @param comment 追加するコメント
	 */
	public void insert(Comment comment) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(comment);
		
		String sql = "INSERT INTO comments (name, content, article_id) "
					+ "VALUES(:name, :content, :articleId)";
		template.update(sql, param);
	}
	
	/**
	 * 指定された記事IDのコメントを削除する
	 * 
	 * @param articleId 記事ID
	 */
	public void deleteByArticleId(int articleId) {
		String sql = "DELETE FROM comments WHERE article_id = :articleId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);
		template.update(sql, param);
	}

}

