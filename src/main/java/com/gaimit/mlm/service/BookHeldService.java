package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.BookHeld;

/** 회원 관련 기능을 제공하기 위한 Service 계층 */
public interface BookHeldService {
		
	/**
	 * 책 목록조회(비밀정보를 제외한 모든 정보)
	 * @param Book - 책의 id 조회
	 * @throws Exception
	 */
	public List<BookHeld> getBookHeldList(BookHeld bookHeld) throws Exception;
	
	/**
	 * 엑셀 추출을 위한 도서목록
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> selectBookHeldListToExcel(BookHeld bookHeld) throws Exception;
	
	/**
	 * 엑셀추출, 년도 별로 등록된 도서 목록
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> selectNewBookHeldListToExcelByYear(BookHeld bookHeld) throws Exception;
	
	/**
	 * 엑셀추출, 년도 별로 폐기된 도서 목록
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> selectDiscardBookHeldListToExcelByYear(BookHeld bookHeld) throws Exception;
	
	/**
	 * 
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> getRegTodayBookHeldList(BookHeld bookHeld) throws Exception;
	
	/**
	 * 오늘 등록된 도서 전체수 구하기 페이지네이션을 위해서
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectRegTodayBookCountForPage(BookHeld bookHeld) throws Exception;
	/**
	 * 
	 * @param book id_book
	 * @return book 테이블의 컬럼 전체
	 * @throws Exception
	 */
	public BookHeld getBookHelditem(BookHeld bookHeld) throws Exception;
	
	/**
	 * 위 getBookHelditem과 동일하지만, null이어도 예외처리 하지 않음.
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public BookHeld selectBookHeldItemEvenNull(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 대출시, 숫자 기입만으로 도서 판별을 위한 쿼리 integer bookHeldId값 반환
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public Integer selectBookHeldItemByOnlyNumToSortingIndex(BookHeld bookHeld) throws Exception;
	
	/**
	 * 바코드 번호로 등록되어있는 도서가 있는지 체크
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectBookHeldCountByBarcode(BookHeld bookHeld) throws Exception;
	
	/**
	 * 
	 * @param bookHeld book 테이블 내에 isbn13 존재 여부 판단
	 * @throws Exception
	 */
	public int selectBookCount(BookHeld bookHeld) throws Exception;
	
	public BookHeld selectBookId(BookHeld bookHeld) throws Exception;
	
	public void insertBook(BookHeld bookHeld) throws Exception;
	
	
	public void insertBookHeld(BookHeld bookHeld) throws Exception;
	
	
	/**
	 * 복본기호를 체크하기 위하여
	 * book_id_book 인자를 이용하여 bookHeld안에 id가 또 있는지 검사
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectBookHeldCount(BookHeld bookHeld) throws Exception;
	
	/**
	 * bookTable 이 아닌 bookheld에서 isbn, 제목과 저자로 bookIdbook 검색
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectBookHeldBookIdBook(BookHeld bookHeld) throws Exception;
	
	/**
	 * lib 내에서 첫등록 체크
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectBookHeldFirstCount(BookHeld bookHeld) throws Exception;
	
	/**
	 * copy code 생성용
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectLastEmptyCopyCode(BookHeld bookHeld) throws Exception;
	
	/**
	 * copy code가 2를 기준으로 순차적으로 빈곳을 체크함, 따라서 처음값이
	 * 2 인지 확인할 수 있는 실행문이 필요함
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectFirstCopyCode(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서가 0번 복본이 사라졌을 때를 판단하기 위한 실행문
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectZeroCopyCodeCount(BookHeld bookHeld) throws Exception;
	
	/**
	 * local id barcode 마지막 구하기
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectEmptyLocalBarcode(BookHeld bookHeld) throws Exception;
	
	/**
	 * 바코드 첫번째 번호 1번이 아닌 경우, 1번을 만들어주고 1번이 있으면 사이번호를 위 함수로 찾는다
	 * @param bookHeld
	 * @return mysql에서 얻은 결과값이 null 수도 있다.
	 * 	그러나 int에는 null을 체크할 수 없으므로, Integer를 사용했다.
	 * @throws Exception
	 */
	public Integer selectFirstLocalBarcode(BookHeld bookHeld) throws Exception;
	
	/**
	 * 
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public String selectLastLocalBarcode(BookHeld bookHeld) throws Exception;
	
	/**
	 * 마지막 sortingIndex 구하기
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public Integer selectLastSortingIndex(BookHeld bookHeld) throws Exception;
	
	/**
	 * 바코드 중복체크를 위한 함수
	 * @param bookHeld
	 * @throws Exception
	 */
	public void selectDupCheckLocalBarcode(BookHeld bookHeld) throws Exception;
	
	/**
	 * 중복체크하는데 exception처리 던지는 것이 아닌, 중복숫자 리턴받기
	 * @param bookHeld
	 * @throws Exception
	 */
	public int selectDupCheckLocalBarcodeReturnNum(BookHeld bookHeld) throws Exception;
	
	/**
	 * 페이지를 위한 count
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectBookCountForPage(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 기록삭제 잘못올린 도서 같은 경우 삭제함
	 * @param bookHeld
	 * @throws Exception
	 */
	public void deleteBookHeldItem(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 폐기 반환값 없음
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateBookHeldDiscard(BookHeld bookHeld) throws Exception;
	
	/**
	 * 폐기된 도서 목록
	 * @param bookHeld 도서관 번호
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> getBookHeldDiscardList(BookHeld bookHeld) throws Exception;
	
	/**
	 * 폐기도서 카운트 페이지 목록
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectBookDiscardCountForPage(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 정보 수정
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateBookHeldItem(BookHeld bookHeld) throws Exception;
	
	/**
	 * 프린트 리스트
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> getPrintBookHeldList(BookHeld bookHeld) throws Exception;
	
	/**
	 * book id를 이용해서 프린트할 라벨 정보 하나씩 가져오기.
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public BookHeld selectPrintBookByBookId(BookHeld bookHeld) throws Exception;
	
	/**
	 * sortingIndex에 바코드 뒷번호만 넣어주기
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateSortingIndex(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서에 붙는 태그 수정
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateBookHeldTag(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 등록번호 중복 알림 주기
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> checkDupBarcodeBookHeld(BookHeld bookHeld) throws Exception;

	/**
	 * 도서 별치기호 일괄 수정을 위한 서비스
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateAddiCodeByBarcodeNum(BookHeld bookHeld) throws Exception;

	/**
	 * 등록시작시간 기준으로 몇권의 데이터가 이동했는지 확인 하기 위함
	 * @param bookHeld - libraryIdLib 도서관 번호, editDate 등록시간(check페이지 접속시간)
	 * @return
	 * @throws Exception
	 */
	public int selectCountImportExcelData(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 id를 이용하여 도서 상태(대출중, 대출가능) 변경하기
	 * @param bookHeld - libraryIdLib & id
	 * @throws Exception
	 */
	public void updateAvailableById(BookHeld bookHeld) throws Exception;
	
	/**
	 * 해당 도서관의 서가 항목을 그룹으로 묶어서 보이기.
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<String> selectBookShelfGroup(BookHeld bookHeld) throws Exception;
	
	
	/**
	 * 도서 바코드 자리수 수정 업데이트
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateBookBarcodeByBarcode(BookHeld bookHeld) throws Exception;
	
	
	/**
	 * 사용자 화면 user self에서 도서 검색
	 * 도서 개수 따로 지정하지 않음.
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> selectBookListUserSelf(BookHeld bookHeld) throws Exception;
	
	
	/**
	 * 도서 온전한 등록번호로 1권 정보 가져오기. 사용자 화면에서
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public BookHeld selectBookHeldItemUserSelf(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서관 내 보유중인 도서인지 판별 도서목록 반환 쿼리
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> selectBookListCheckDupNoBlank(BookHeld bookHeld) throws Exception;
	
	
	/**
	 * 바코드 번호로, 권차기호 일괄수정 시작번호 끝번호로 시작 1 끝 번호
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateVolumeCodeBatchByBarcode(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 isbn만으로 복본기호 체크, 제목과 저자는 신경안씀.
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public int selectDupCountByOnlyISBN(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서 별치기호 그룹별로 묶어서 리스트 가져오기.
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<String> selectAddiCodeGroup(BookHeld bookHeld) throws Exception;
	
	/**
	 * 바코드 범위 직접 입력 방식의 출력 리스트 
	 * @param bookHeld
	 * @return
	 * @throws Exception
	 */
	public List<BookHeld> selectPrintListByBarcodeRange(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서관 삭제를 위한 도서 정보 전부 삭제 library id lib 
	 * @param bookHeld
	 * @throws Exception
	 */
	public void deleteBookHeldAllByLibraryIdLib(BookHeld bookHeld) throws Exception;
	
	/**
	 * 도서등록번호로 RF ID 각각 수정하기
	 * @param bookHeld
	 * @throws Exception
	 */
	public void updateBookRfIdByBarcode(BookHeld bookHeld) throws Exception;
	
	
	
	
	
	
	
}
