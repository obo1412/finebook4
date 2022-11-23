package com.gaimit.mlm.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaimit.mlm.model.Library;
import com.gaimit.mlm.service.LibraryService;

//--> import org.springframework.stereotype.Service; 
@Service
public class LibraryServiceImpl implements LibraryService {
	
	/** 처리 결과를 기록할 Log4J 객체 생성 */
	// --> import org.slf4j.Logger;
	// --> import org.slf4j.LoggerFactory;
	/*private static Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);*/

	/** MyBatis */
	// --> import org.springframework.beans.factory.annotation.Autowired;
	// --> import org.apache.ibatis.session.SqlSession
	@Autowired
	SqlSession sqlSession;


	
	
	@Override
	public List<Library> selectLibraryList(Library library) throws Exception {
		List<Library> result = null;
		try {
			result = sqlSession.selectList("LibraryMapper.selectLibraryList", library);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 도서관 목록이 없습니다.");
		} catch (Exception e) {
			throw new Exception("도서관 목록 조회에 실패했습니다.");
		}
		return result;
	}


	@Override
	public Integer selectLibraryCountForPage(Library library) throws Exception {
		Integer result = 0;
		try {
			result = sqlSession.selectOne("LibraryMapper.selectLibraryCountForPage", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("등록된 도서관이 없습니다.");
		} catch (Exception e) {
			throw new Exception("도서관 수 조회에 실패했습니다.");
		}
		return result;
	}



	@Override
	public Library selectLibItemByKeys(Library library) throws Exception {
		Library result = null;
		try {
			result = sqlSession.selectOne("LibraryMapper.selectLibItemByKeys", library);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 도서 목록 데이터가 없습니다.");
		} catch (Exception e) {
			throw new Exception("도서 목록 데이터 조회에 실패했습니다.");
		}
		return result;
	}


	@Override
	public void updateExpDateLib(Library library) throws Exception {
		try {
			int result = sqlSession.update("LibraryMapper.updateExpDateLib", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("업데이트된 만료일 값이 없습니다.(updateExpDateLib)");
		} catch (Exception e) {
			throw new Exception("만료일 업데이트(updateExpDateLib)에 실패했습니다.");
		}
	}


	@Override
	public void insertNewLib(Library library) throws Exception {
		try {
			int result = sqlSession.insert("LibraryMapper.insertNewLib", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("새로 생성된 도서관 정보가 없습니다.(insertNewLib)");
		} catch (Exception e) {
			throw new Exception("도서관 생성에 실패했습니다.(insertNewLib)");
		} finally {
		}
	}


	@Override
	public void updateLibItem(Library library) throws Exception {
		try {
			int result = sqlSession.update("LibraryMapper.updateLibItem", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("업데이트된 도서관 정보가 없습니다.(updateLibItem)");
		} catch (Exception e) {
			throw new Exception("도서관 정보 업데이트(updateLibItem)에 실패했습니다.");
		}
	}


	@Override
	public Library selectLibraryItem(Library library) throws Exception {
		Library result = null;
		try {
			result = sqlSession.selectOne("LibraryMapper.selectLibraryItem", library);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 도서관 데이터가 없습니다.(selectLibraryItem)");
		} catch (Exception e) {
			throw new Exception("도서관 데이터 조회에 실패했습니다.(selectLibraryItem)");
		}
		return result;
	}


	@Override
	public void deleteLibItem(Library library) throws Exception {
		try {
			int result = sqlSession.delete("LibraryMapper.deleteLibItem", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("이미 삭제된 도서관입니다.(deleteLibItem)");
		} catch (Exception e) {
			throw new Exception("도서관 삭제(deleteLibItem)에 실패했습니다.");
		}
	}


	@Override
	public Integer selectNodBarcodeByIdLib(Library library) throws Exception {
		Integer result = 0;
		try {
			result = sqlSession.selectOne("LibraryMapper.selectNodBarcodeByIdLib", library);
		} catch (Exception e) {
			throw new Exception("도서관의 등록번호 자리수 조회(selectNodBarcodeByIdLib)에 실패했습니다.");
		}
		return result;
	}


	@Override
	public void updateNodBarcodeByIdLib(Library library) throws Exception {
		try {
			int result = sqlSession.update("LibraryMapper.updateNodBarcodeByIdLib", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("업데이트된 등록번호 자리수가 없습니다.(updateNodBarcodeByIdLib)");
		} catch (Exception e) {
			throw new Exception("등록번호 자리수 업데이트(updateNodBarcodeByIdLib)에 실패했습니다.");
		}
	}


	@Override
	public void updateStatementDateLatest(Library library) throws Exception {
		try {
			int result = sqlSession.update("LibraryMapper.updateStatementDateLatest", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("업데이트된 정산일 값이 없습니다.(updateStatementDateLatest)");
		} catch (Exception e) {
			throw new Exception("정산일 업데이트(updateStatementDateLatest)에 실패했습니다.");
		}
	}


	@Override
	public void updateStatementDate(Library library) throws Exception {
		try {
			int result = sqlSession.update("LibraryMapper.updateStatementDate", library);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("업데이트된 정산일 값이 없습니다.(updateStatementDate)");
		} catch (Exception e) {
			throw new Exception("정산일 업데이트(updateStatementDate)에 실패했습니다.");
		}
	}


}
