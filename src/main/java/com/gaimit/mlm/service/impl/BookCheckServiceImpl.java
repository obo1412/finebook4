package com.gaimit.mlm.service.impl;

import java.util.List;

/*import java.util.List;*/

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaimit.mlm.model.BookCheckModel;
import com.gaimit.mlm.service.BookCheckService;

//--> import org.springframework.stereotype.Service; 
@Service
public class BookCheckServiceImpl implements BookCheckService {
	
	/** 처리 결과를 기록할 Log4J 객체 생성 */
	// --> import org.slf4j.Logger;
	// --> import org.slf4j.LoggerFactory;
	private static Logger logger = LoggerFactory.getLogger(BookCheckServiceImpl.class);

	/** MyBatis */
	// --> import org.springframework.beans.factory.annotation.Autowired;
	// --> import org.apache.ibatis.session.SqlSession
	@Autowired
	SqlSession sqlSession;

	@Override
	public void insertBcs(BookCheckModel bookCheck) throws Exception {
		// 대여중인 도서인지 중복검사
		//getBorrowCountByBookCode(borrow);

		// 데이터 저장처리 = 가입
		// not null로 설정된 값이 설정되지 않았다면 예외 발생됨.
		try {
			int result = sqlSession.insert("BookCheckMapper.insertBcs", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("저장된 장서점검 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("장서점검 등록(insertBcs)에 실패했습니다.");
		}
		
	}

	@Override
	public List<BookCheckModel> selectBcsList(BookCheckModel bookCheck) throws Exception {
		List<BookCheckModel> result = null;
		try {
			result = sqlSession.selectList("BookCheckMapper.selectBcsList", bookCheck);
		} catch (Exception e) {
			throw new Exception("장서점검 데이터 조회(selectBcsList)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectWholeCount(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectWholeCount", bookCheck);
		} catch (Exception e) {
			throw new Exception("전체도서수 조회(selectWholeCount)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectBrwedCount(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectBrwedCount", bookCheck);
		} catch (Exception e) {
			throw new Exception("대출중 도서수 조회(selectBrwedCount)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void insertBcl(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.insert("BookCheckMapper.insertBcl", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("입력된 장서점검 도서(insertBcl) 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("장서점검 도서 체크(insertBcl)에 실패했습니다.");
		}
	}

	@Override
	public List<BookCheckModel> selectBclList(BookCheckModel bookCheck) throws Exception {
		List<BookCheckModel> result = null;
		try {
			result = sqlSession.selectList("BookCheckMapper.selectBclList", bookCheck);
		} catch (Exception e) {
			throw new Exception("장서점검 도서 목록 조회(selectBclList)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public List<BookCheckModel> selectBookHeldList(BookCheckModel bookCheck) throws Exception {
		List<BookCheckModel> result = null;
		try {
			result = sqlSession.selectList("BookCheckMapper.selectBookHeldList", bookCheck);
		} catch (Exception e) {
			throw new Exception("도서 전체 목록 조회(bookCheck - selectBookHeldList)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public BookCheckModel selectBclItemByInputBarcode(BookCheckModel bookCheck) throws Exception {
		BookCheckModel result = null;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectBclItemByInputBarcode", bookCheck);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 단일 도서 점검(selectBclItemByInputBarcode) 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("단일도서점검(selectBclItemByInputBarcode) 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void updateCheckedCount(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.update("BookCheckMapper.updateCheckedCount", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("존재하지 않는 점검현황(updateCheckedCount)에 대한 요청입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("점검수(updateCheckedCount) 갱신에 실패했습니다.");
		}
	}

	@Override
	public int existBookCheckCount(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.existBookCheckCount", bookCheck);
		} catch (Exception e) {
			throw new Exception("장서점검 보유도서 조회(existBookCheckCount)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectIdBookHeldByInputBarcode(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectIdBookHeldByInputBarcode", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("존재하지 않는 등록번호(selectIdBookHeldByInputBarcode)에 대한 요청입니다.");
		} catch (Exception e) {
			throw new Exception("도서 고유번호 조회(selectIdBookHeldByInputBarcode)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectRedupCountInBcl(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectRedupCountInBcl", bookCheck);
		} catch (Exception e) {
			throw new Exception("중복 점검 도서 조회(selectRedupCountInBcl)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectBrwedCheck(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectBrwedCheck", bookCheck);
		} catch (Exception e) {
			throw new Exception("대출중인 점검 도서 조회(selectBrwedCheck)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public BookCheckModel selectCurrentBookCheckStatus(BookCheckModel bookCheck) throws Exception {
		BookCheckModel result = null;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectCurrentBookCheckStatus", bookCheck);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 도서점검 상태(selectCurrentBookCheckStatus) 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("도서점검 상태(selectCurrentBookCheckStatus) 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void deleteBclByBcs(BookCheckModel bookCheck) throws Exception {
		try {
			// 첨부파일이 없는 경우도 있으므로, 결과가 0인 경우 예외를 발생하지 않는다.
			sqlSession.delete("BookCheckMapper.deleteBclByBcs", bookCheck);
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("해당 점검의 BCL(deleteBclByBcs) 정보 삭제에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void deleteBcs(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.delete("BookCheckMapper.deleteBcs", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("삭제된 장서점검(deleteBcs) 정보가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("장서점검(deleteBcs) 정보 삭제에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}
	
	@Override
	public void updateRedupCount(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.update("BookCheckMapper.updateRedupCount", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("존재하지 않는 점검현황(updateRedupCount)에 대한 요청입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("중복 점검수(updateRedupCount) 갱신에 실패했습니다.");
		}
	}

	@Override
	public void updateConfirmCount(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.update("BookCheckMapper.updateConfirmCount", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("존재하지 않는 점검현황(updateConfirmCount)에 대한 요청입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("확인 점검수(updateConfirmCount) 갱신에 실패했습니다.");
		}
	}
	
	@Override
	public void updateRentedCheckedCount(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.update("BookCheckMapper.updateRentedCheckedCount", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("존재하지 않는 점검현황(updateRentedCheckedCount)에 대한 요청입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("발견된 대출중 도서수(updateRentedCheckedCount) 갱신에 실패했습니다.");
		}
	}

	
	@Override
	public void updateUnregCount(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.update("BookCheckMapper.updateUnregCount", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("존재하지 않는 점검현황(updateUnregCount)에 대한 요청입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("발견된 미등록 도서수(updateUnregCount) 갱신에 실패했습니다.");
		}
	}
	
	@Override
	public void updateUncheckedCountMinus(BookCheckModel bookCheck) throws Exception {
		try {
			int result = sqlSession.update("BookCheckMapper.updateUncheckedCountMinus", bookCheck);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("존재하지 않는 점검현황(updateUncheckedCountMinus)에 대한 요청입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("미점검 도서수(updateUncheckedCountMinus) 갱신에 실패했습니다.");
		}
	}

	@Override
	public List<BookCheckModel> selectUncheckedBookInBcs(BookCheckModel bookCheck) throws Exception {
		List<BookCheckModel> result = null;
		try {
			result = sqlSession.selectList("BookCheckMapper.selectUncheckedBookInBcs", bookCheck);
		} catch (Exception e) {
			throw new Exception("미점검 도서 목록 조회(selectUncheckedBookInBcs)에 실패했습니다.");
		}
		return result;
	}
	
	@Override
	public int selectUncheckedBookCountInBcs(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectUncheckedBookCountInBcs", bookCheck);
		} catch (Exception e) {
			throw new Exception("미점검 도서수 조회(selectUncheckedBookCountInBcs)에 실패했습니다.");
		}
		return result;
	}
	
	@Override
	public List<BookCheckModel> selectUncheckedBookExceptRentedBook(BookCheckModel bookCheck) throws Exception {
		List<BookCheckModel> result = null;
		try {
			result = sqlSession.selectList("BookCheckMapper.selectUncheckedBookExceptRentedBook", bookCheck);
		} catch (Exception e) {
			throw new Exception("미점검 도서 목록 조회(selectUncheckedBookExceptRentedBook)에 실패했습니다.");
		}
		return result;
	}
	
	@Override
	public List<BookCheckModel> selectWholeBrwedBookList(BookCheckModel bookCheck) throws Exception {
		List<BookCheckModel> result = null;
		try {
			result = sqlSession.selectList("BookCheckMapper.selectWholeBrwedBookList", bookCheck);
		} catch (Exception e) {
			throw new Exception("전체 대출중 도서 목록 조회(selectWholeBrwedBookList)에 실패했습니다.");
		}
		return result;
	}


	@Override
	public List<BookCheckModel> selectNormalBookList(BookCheckModel bookCheck) throws Exception {
		List<BookCheckModel> result = null;
		try {
			result = sqlSession.selectList("BookCheckMapper.selectNormalBookList", bookCheck);
		} catch (Exception e) {
			throw new Exception("장서점검 도서 목록 조회(selectNormalBookList)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public String selectBarcodeByIdBookHeld(BookCheckModel bookCheck) throws Exception {
		String result = null;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectBarcodeByIdBookHeld", bookCheck);
		} catch (Exception e) {
			throw new Exception("도서등록번호 가져오기가(selectBarcodeByIdBookHeld) 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectRentedBookCount(BookCheckModel bookCheck) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectRentedBookCount", bookCheck);
		} catch (Exception e) {
			throw new Exception("발견된 대출중 도서수 조회(selectRentedBookCount)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectBookCheckRecordByBookId(int bookHeldId) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("BookCheckMapper.selectBookCheckRecordByBookId", bookHeldId);
		} catch (Exception e) {
			throw new Exception("해당 도서의 장서점검기록 조회(selectBookCheckRecordByBookId)에 실패했습니다.");
		}
		return result;
	}


	
	

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	

}