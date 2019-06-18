package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;
import com.example.domain.Comment;

@Repository
public class ArticleRepository {
    private static final ResultSetExtractor<List<Article>> ARTICLE_RESULT_SET_EXTRACTOR = (rs) -> {
        Article currentArticle = null;
        ArrayList<Article> articleList = new ArrayList<>();

        while (rs.next()) {
            int articleId = rs.getInt("article_id");
            if (currentArticle == null || currentArticle.getId() != articleId) {
                currentArticle = new Article();
                currentArticle.setId(articleId);
                currentArticle.setName(rs.getString("article_name"));
                currentArticle.setContent(rs.getString("article_content"));
                currentArticle.setCommentList(new ArrayList<>());
                articleList.add(currentArticle);
            }
            if (rs.getObject("comment_id") == null) {
                continue;
            }

            Comment comment = new Comment(rs.getInt("comment_id"), rs.getString("comment_name"),
                    rs.getString("comment_content"), articleId);
            currentArticle.getCommentList().add(comment);
        }
        return articleList;
    };

    @Autowired
    private NamedParameterJdbcTemplate template;

    /**
     * 記事全件をコメントも含めて取得する.
     * 
     * @return 記事全件のリスト
     */
    public List<Article> findAll() {
        String sql = "SELECT a.id AS article_id, a.name AS article_name, a.content AS article_content,"
                + "          c.id AS comment_id, c.name AS comment_name, c.content AS comment_content"
                + " FROM articles a"
                + " LEFT OUTER JOIN comments c"
                + " ON a.id = c.article_id"
                + " ORDER BY a.id DESC, c.id DESC";
        return template.query(sql, ARTICLE_RESULT_SET_EXTRACTOR);
    }

    /**
     * 新しい記事をDBに登録する.
     * 
     * @param article 新しい記事
     */
    public void insert(Article article) {
        String sql = "INSERT INTO articles (name, content) VALUES (:name, :content);";
        SqlParameterSource params = new BeanPropertySqlParameterSource(article);
        template.update(sql, params);
    }

    /**
     * 指定したIDの記事を削除する.
     * 
     * @param id ID
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM articles WHERE id = :id;";
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        template.update(sql, params);
    }
}
