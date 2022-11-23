package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.Library;

/** 회원 관련 기능을 제공하기 위한 Service 계층 */
public interface LibraryService {
		
	/**
	 * 도서관 목록 조회
	 * @param library - 파라미터 필요없음.
	 * @throws Exception
	 */
	public List<Library> selectLibraryList(Library library) throws Exception;
	
	/**
	 * 도서관 목록 수 조회
	 * @param library 도서관명과 지역명에 따라 조회가능
	 * @return
	 * @throws Exception
	 */
	public Integer selectLibraryCountForPage(Library library) throws Exception;
	
	/**
	 * string key와 id lib로 도서관 정보 호출하기
	 * @param library
	 * @return
	 * @throws Exception
	 */
	public Library selectLibItemByKeys(Library library) throws Exception;
	
	/**
	 * 도서관 만료일 업데이트
	 * @param library idLib 도서관 번호, expDate 만료일
	 * @throws Exception
	 */
	public void updateExpDateLib(Library library) throws Exception;
	
	/**
	 * 도서관 생성
	 * @param library nameLib, locLib, stringKeyLib, expDate
	 * @throws Exception
	 */
	public void insertNewLib(Library library) throws Exception;
	
	/**
	 * 도서관 내용 업데이트
	 * @param library idLib 도서관 번호, nameLib, locLib, stringKeyLib
	 * @throws Exception
	 */
	public void updateLibItem(Library library) throws Exception;
	
	/**
	 * 도서관 단일 조회 및 수정을 위한 셀렉트
	 * @param library idLib where 조건
	 * @return
	 * @throws Exception
	 */
	public Library selectLibraryItem(Library library) throws Exception;
	
	/**
	 * 도서관 단일 삭제
	 * @param library
	 * @throws Exception
	 */
	public void deleteLibItem(Library library) throws Exception;
	
	/**
	 * 바코드 등록번호 자리수 가져오기.
	 * @param library
	 * @return
	 * @throws Exception
	 */
	public Integer selectNodBarcodeByIdLib(Library library) throws Exception;
	
	/**
	 * 등록번호 자리수만 변경하기.
	 * @param library
	 * @throws Exception
	 */
	public void updateNodBarcodeByIdLib(Library library) throws Exception;
	
	/**
	 * 도서관 정산일 최신 날짜로 변경
	 * @param library
	 * @throws Exception
	 */
	public void updateStatementDateLatest(Library library) throws Exception;
	
	/**
	 * 도서관 정산일 날짜 원하는 날짜로 변경.
	 * @param library
	 * @throws Exception
	 */
	public void updateStatementDate(Library library) throws Exception;
	
	
	
	
	
	
	
}
