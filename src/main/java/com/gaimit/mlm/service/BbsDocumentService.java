package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.BbsDocument;

/**
 * 게시물 관련 기능을 제공하기 위한 Service 계층
 */
public interface BbsDocumentService {
	/**
	 * 게시물을 저장한다.
	 * 
	 * @param document
	 *            - 게시물 데이터
	 * @throws Exception
	 */
	public void insertDocument(BbsDocument document) throws Exception;

	/**
	 * 하나의 게시물을 읽어들인다.
	 * 
	 * @param document
	 *            - 읽어들일 게시물 일련번호가 저장된 Beans
	 * @return BbsDocument - 읽어들인 게시물 내용
	 * @throws Exception
	 */
	public BbsDocument selectDocument(BbsDocument document) throws Exception;

	/**
	 * 현재글을 기준으로 이전글을 읽어들인다.
	 * 
	 * @param document
	 *            - 현재글에 대한 게시물 번호가 저장된 Beans
	 * @return BbsDocument - 읽어들인 게시물 내용 (없을 경우 null)
	 * @throws Exception
	 */
	public BbsDocument selectPrevDocument(BbsDocument document) throws Exception;

	/**
	 * 현재글을 기준으로 다음글을 읽어들인다.
	 * 
	 * @param document
	 *            - 현재글에 대한 게시물 번호가 저장된 Beans
	 * @return BbsDocument - 읽어들인 게시물 내용 (없을 경우 null)
	 * @throws Exception
	 */
	public BbsDocument selectNextDocument(BbsDocument document) throws Exception;

	/**
	 * 조회수를 1 증가시킨다.
	 * 
	 * @param document
	 *            - 현재글에 대한 게시물 번호가 저장된 Beans
	 * @throws Exception
	 */
	public void updateDocumentHit(BbsDocument document) throws Exception;

	/**
	 * 게시글 목록 조회
	 * 
	 * @param document
	 *            - 카테고리 정보가 저장된 Beans
	 * @return List - 게시물 목록
	 * @throws Exception
	 */
	public List<BbsDocument> selectDocumentList(BbsDocument document) throws Exception;

	/**
	 * 전체 게시물 수 조회
	 * 
	 * @param document
	 * @return int
	 * @throws Exception
	 */
	public int selectDocumentCount(BbsDocument document) throws Exception;

	/**
	 * 자신의 게시물인지 검사한다.
	 * 
	 * @param document
	 * @return int
	 * @throws Exception
	 */
	public int selectDocumentCountByManagerId(BbsDocument document) throws Exception;

	/**
	 * 비밀번호를 검사한다.
	 * 
	 * @param document
	 * @return int
	 * @throws Exception
	 */
	public int selectDocumentCountByPw(BbsDocument document) throws Exception;

	/**
	 * 게시물을 삭제한다.
	 * 
	 * @param document
	 * @throws Exception
	 */
	public void deleteDocument(BbsDocument document) throws Exception;
	
	/**
	 * 도서관 삭제를 위한 manager id의 게시글 전체 삭제
	 * @param document
	 * @throws Exception
	 */
	public void deleteDocumentByManagerId(BbsDocument document) throws Exception;
	
	/**
	 * 게시물을 수정한다.
	 * @param document - 게시물 데이터
	 * @throws Exception
	 */
	public void updateDocument(BbsDocument document) throws Exception;
	
	/**
	 * 회원과 게시물의 참조관계를 해제한다.
	 * @param document - 게시물 데이터
	 * @throws Exception
	 */
	public void updateDocumentManagerOut(BbsDocument document) throws Exception;
	
	
	/**
	 * 게시글에 이미 요청한 동일 도서가 있는지 확인하기.
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public List<BbsDocument> selectCheckRequestBook(BbsDocument document) throws Exception;
	
	/**
	 * 게시글 제목만 수정하기 [요청], [진행중], [완료] 처리 수정하기위함
	 * @param document
	 * @throws Exception
	 */
	public void updateDocumentSubjectOnly(BbsDocument document) throws Exception;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
