package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.Book;

/** 회원 관련 기능을 제공하기 위한 Service 계층 */
public interface BookService {
		
	/**
	 * 책 목록조회(비밀정보를 제외한 모든 정보)
	 * @param Book - 책의 id 조회
	 * @throws Exception
	 */
	public List<Book> getBookList(Book book) throws Exception;
	
	/**
	 * 
	 * @param book id_book
	 * @return book 테이블의 컬럼 전체
	 * @throws Exception
	 */
	public Book getBookitem(Book book) throws Exception;
	
	
	//이하 국가 관련
	public void insertCountry(Book book) throws Exception;
	
	
	public List<Book> selectCountryList(Book book) throws Exception;
	
	public List<Book> selectCountryListOnly(Book book) throws Exception;
	
	
	public int selectCountryCount(Book book) throws Exception;
	
	
	public int selectCountryNameCheck(Book book) throws Exception;
}
