package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;

@Controller
@Transactional
@RequestMapping("/bord")
public class ArticleController {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}
	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}
	
	@RequestMapping(value = "/like-count", method = RequestMethod.POST)
	public Map<String ,Integer> likeCount(){
		Map<String, Integer> likeMap = new HashMap<>();
		return likeMap;
	}
	/**
	 * 記事一覧を表示
	 * @param model リクエストスコープ
	 * @return 記事一覧画面
	 */
	@RequestMapping("")
	public String index(Model model) {
		
		List<Article> articleList = articleRepository.findAll();
		/*初級
		for(Article article : articleList) {
			article.setCommentList(commentRepository.findByArticleId(article.getId()));
		}
		*/
		model.addAttribute("articleList", articleList);
		
		return "bulletin-bord";
	}
	
	/**
	 * 記事を投稿.
	 * @param form 記事のフォーム
	 * @param model リクエストスコープ
	 * @return 記事一覧画面へのリダイレクト
	 */
	@RequestMapping("/insert-article")
	public String insertArticle(@Validated ArticleForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return index(model);
		}
		
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		articleRepository.insert(article);
		
		return "redirect:/bord";
	}
	
	/**
	 * コメント投稿.
	 * @param form コメント投稿用フォーム
	 * @return 記事一覧画面へのリダイレクト
	 */
	@RequestMapping("/insert-comment")
	public String insertComment(@Validated CommentForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return index(model);
		}
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		comment.setArticleId(Integer.parseInt(form.getArticleId()));
		commentRepository.insert(comment);
		
		return "redirect:/bord";
	}
	
	/**
	 * 記事を削除.
	 * @param articleId 削除する記事ID
	 * @return 記事一覧画面へのリダイレクト
	 */
	@RequestMapping("/delete-article")
	public String deleteArticle(String articleId) {
		//commentRepository.deleteByArticleId(Integer.parseInt(articleId));
		articleRepository.deleteById(Integer.parseInt(articleId));
		
		return "redirect:/bord";
	}
	

}
