package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.BookCheckModel;

/** 회원 관련 기능을 제공하기 위한 Service 계층 */
public interface BookCheckService {
	
	/**
	 * 
	 * @param bookCheck  장서점검 빈즈 장서점검 status 생성
	 * @throws Exception
	 */
	public void insertBcs(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 
	 * @param borrow
	 * @return 대출중인 도서 목록 반환
	 * @throws Exception
	 */
	public List<BookCheckModel> selectBcsList(BookCheckModel bookCheck) throws Exception;
	 
	/**
	 * 해당도서관의 전체도서수
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectWholeCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 대출중인 도서수 확인
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectBrwedCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 실제 장서점검된 리스트에 도서 목록에 올리기
	 * @param bookCheck
	 * @throws Exception
	 */
	public void insertBcl(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 해당 장서점검의 도서 체크 리스트
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public List<BookCheckModel> selectBclList(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 보유도서 전체 목록
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public List<BookCheckModel> selectBookHeldList(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 방금 입력한 바코드 번호로, 등록된 데이터 가져오기
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public BookCheckModel selectBclItemByInputBarcode(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 체크한 도서 카운트 +1
	 * @param bookCheck
	 * @throws Exception
	 */
	public void updateCheckedCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 입력한 inputBarcode의 도서 정보가 존재하는지 체크
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int existBookCheckCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * id_book_held의 도서명, 저자명 등 데이터를 얻기 위함.
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectIdBookHeldByInputBarcode(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 중복 도서가 있는지 id_book_held로 count 체크
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectRedupCountInBcl(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 대출중인 도서인지 count 체크
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectBrwedCheck(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 현재 진행중인 도서점검 상태 가져오기
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public BookCheckModel selectCurrentBookCheckStatus(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * bookcheckList 삭제 bcs 삭제를 위하여
	 * @param bookCheck
	 * @throws Exception
	 */
	public void deleteBclByBcs(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * bcs 삭제
	 * @param bookCheck
	 * @throws Exception
	 */
	public void deleteBcs(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 중복도서 카운트 +1
	 * @param bookCheck
	 * @throws Exception
	 */
	public void updateRedupCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 점검시 정상 확인된 도서 수만 카운트 +1
	 * @param bookCheck
	 * @throws Exception
	 */
	public void updateConfirmCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 점검시 발견된 대출중 도서 카운트 매번 값을 받아서 업데이트
	 * +1 해주려면, 이게 처음찍힌 대출도서인지, 기존에 찍힌 도서라서 
	 * 카운트가 필요없는지 판별해야해서. 그냥 매번 값을 받아오는게 빠름.
	 * @param bookCheck
	 * @throws Exception
	 */
	public void updateRentedCheckedCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 점검시 미등록 도서 카운트 +1
	 * @param bookCheck
	 * @throws Exception
	 */
	public void updateUnregCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 미점검 도서 카운트 -1 (빼기 1!!!!!!!!!!)
	 * @param bookCheck
	 * @throws Exception
	 */
	public void updateUncheckedCountMinus(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 점검 체크되지 않은 도서 목록 등록번호, 제목, 저자
	 * 이 기능은, 대출중 도서를 포함!
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public List<BookCheckModel> selectUncheckedBookInBcs(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 점검 체크되지 않은 도서 수 구하기, bcs 생성시 사용.
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectUncheckedBookCountInBcs(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 점검 체크되지 않은 도서 목록
	 * - 대출중 도서를 빼고 나머지 목록!! 
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public List<BookCheckModel> selectUncheckedBookExceptRentedBook(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 도서관의 현재 전체 대출 도서 목록.
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public List<BookCheckModel> selectWholeBrwedBookList(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 점검 정상상태 도서 목록, 점검결과 '확인'인 도서들
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public List<BookCheckModel> selectNormalBookList(BookCheckModel bookCheck) throws Exception;
	
	
	/**
	 * idBookHeld를 이용하여, 해당 도서의 정확한 바코드 번호 가져오기.
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public String selectBarcodeByIdBookHeld(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 발견된 대출중 도서수 가져오기.
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectRentedBookCount(BookCheckModel bookCheck) throws Exception;
	
	/**
	 * 도서 삭제시, 장서점검 기록이 있는지 확인하기.
	 * @param bookCheck
	 * @return
	 * @throws Exception
	 */
	public int selectBookCheckRecordByBookId(int bookHeldId) throws Exception;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
