package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.BbsComment;

/** 덧글 관련 기능을 제공하기 위한 Service 계층 */
public interface BbsCommentService {
	/**
	 * 덧글을 저장한다.
	 * @param comment - 덧글 데이터
	 * @throws Exception
	 */
	public void insertComment(BbsComment comment) throws Exception;

	/**
	 * 하나의 덧글을 읽어들인다.
	 * @param comment - 읽어들일 게시물 일련번호가 저장된 Beans
	 * @return BbsComment - 읽어들인 게시물 내용
	 * @throws Exception
	 */
	public BbsComment selectComment(BbsComment comment) throws Exception;

	/**
	 * 하나의 게시물에 속한 모든 덧글 목록을 조회한다.
	 * @param comment - 읽어들일 게시물 일련번호가 저장된 Beans
	 * @return List - 읽어들인 덧글 목록
	 * @throws Exception
	 */
	// --> import java.util.List;
	public List<BbsComment> selectCommentList(BbsComment comment) throws Exception;
	
	/**
	 * 자신의 덧글인지 검사한다.
	 * @param Comment
	 * @return int
	 * @throws Exception
	 */
	public int selectCommentCountByManagerId(BbsComment comment) throws Exception;

	/**
	 * 비밀번호를 검사한다.
	 * @param Comment
	 * @return int
	 * @throws Exception
	 */
	public int selectCommentCountByPw(BbsComment comment) throws Exception;

	/**
	 * 덧글을 삭제한다.
	 * @param Comment
	 * @throws Exception
	 */
	public void deleteComment(BbsComment comment) throws Exception;
	
	/**
	 * 게시물을 수정한다.
	 * @param comment - 덧글 데이터
	 * @throws Exception
	 */
	public void updateComment(BbsComment comment) throws Exception;

	/**
	 * 특정 게시물에 속한 모든 덧글을 삭제한다.
	 * @param Comment
	 * @throws Exception
	 */
	public void deleteCommentAll(BbsComment comment) throws Exception;
	
	
	/**
	 * 회원과 덧글간의 참조관계를 해제한다.
	 * @param document - 게시물 데이터
	 * @throws Exception
	 */
	public void updateCommentManagerOut(BbsComment comment) throws Exception;
	
	
	/**
	 * 덧글 삭제 매니저 id와 관련된거 모두
	 * @param comment
	 * @throws Exception
	 */
	public void deleteCommentByManagerId(BbsComment comment) throws Exception;
	
	
	
	
	
	
	
	
	
}
