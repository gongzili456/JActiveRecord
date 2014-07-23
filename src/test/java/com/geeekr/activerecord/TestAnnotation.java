package com.geeekr.activerecord;

import java.io.Serializable;
import java.util.Date;

import org.junit.Test;

public class TestAnnotation {

	@Test
	public void fanshe() {
		Post p = new Post();
		p.setTitle("title");

		Serializable a = 0;
		System.out.println(a.toString());
	}

	@Test
	public void delete() {
		Post p = new Post();
		p.setId(1);
		p.delete();
	}

	@Test
	public void update() {
		Post p = new Post();
		p.setId(2);
		p.setTitle("title");
		p.setMarkdown("markdown");
		p.setCreatedAt(new Date());
		p.setCreatedBy(1);
		p.setPage(false);
		p.setSlug("slug");
		p.setStatus("publish");

		p.setHtml("<a>aaaa</a>");
		p.update();
	}

	@Test
	public void insert() {
		Post p = new Post();
		p.setTitle("title");
		p.setMarkdown("markdown");
		p.setHtml("html");
		p.setCreatedAt(new Date());
		p.setCreatedBy(1);
		p.setPage(false);
		p.setSlug("slug");
		p.setStatus("publish");

		p.insert();
	}

	@Test
	public void query() {
		Post p = Post.query(Post.class, 2);
		System.out.println(p);
	}
}
