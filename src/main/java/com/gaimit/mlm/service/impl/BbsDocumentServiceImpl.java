package com.gaimit.mlm.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaimit.mlm.model.BbsDocument;
import com.gaimit.mlm.service.BbsDocumentService;

//--> import org.springframework.stereotype.Service; 
@Service
public class BbsDocumentServiceImpl implements BbsDocumentService {

	/** 처리 결과를 기록할 Log4J 객체 생성 */
	// --> import org.slf4j.Logger;
	// --> import org.slf4j.LoggerFactory;
	private static Logger logger = LoggerFactory.getLogger(BbsDocumentServiceImpl.class);

	/** MyBatis */
	// --> import org.springframework.beans.factory.annotation.Autowired;
	// --> import org.apache.ibatis.session.SqlSession
	@Autowired
	SqlSession sqlSession;

	@Override
	public void insertDocument(BbsDocument document) throws Exception {
		try {
			int result = sqlSession.insert("BbsDocumentMapper.insertDocument", document);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("저장된 게시물이 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 정보 등록에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public BbsDocument selectDocument(BbsDocument document) throws Exception {
		BbsDocument result = null;

		try {
			result = sqlSession.selectOne("BbsDocumentMapper.selectDocument", document);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 게시물이 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public BbsDocument selectPrevDocument(BbsDocument document) throws Exception {
		BbsDocument result = null;

		try {
			// 이전글이 없는 경우도 있으므로, 리턴값이 null인 경우 예외를 발생하지 않는다.
			result = sqlSession.selectOne("BbsDocumentMapper.selectPrevDocument", document);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("이전글 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public BbsDocument selectNextDocument(BbsDocument document) throws Exception {
		BbsDocument result = null;

		try {
			// 다음글이 없는 경우도 있으므로, 리턴값이 null인 경우 예외를 발생하지 않는다.
			result = sqlSession.selectOne("BbsDocumentMapper.selectNextDocument", document);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("다음글 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public void updateDocumentHit(BbsDocument document) throws Exception {
		try {
			int result = sqlSession.update("BbsDocumentMapper.updateDocumentHit", document);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("존재하지 않는 게시물에 대한 요청입니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("조회수 갱신에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public List<BbsDocument> selectDocumentList(BbsDocument document) throws Exception {

		List<BbsDocument> result = null;

		try {
			result = sqlSession.selectList("BbsDocumentMapper.selectDocumentList", document);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 글 목록이 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("글 목록 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public int selectDocumentCount(BbsDocument document) throws Exception {
		int result = 0;

		try {
			// 게시물 수가 0건인 경우도 있으므로, 
			// 결과값이 0인 경우에 대한 예외를 발생시키지 않는다.
			result = sqlSession.selectOne("BbsDocumentMapper.selectDocumentCount", document);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 수 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public int selectDocumentCountByManagerId(BbsDocument document) throws Exception {
		int result = 0;

		try {
			// 자신의 게시물이 아닌 경우도 있으므로,
			// 결과값이 0인 경우에 대한 예외를 발생시키지 않는다.
			result = sqlSession.selectOne(
					"BbsDocumentMapper.selectDocumentCountByManagerId", document);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 수 조회에 실패했습니다.");
		}

		return result;
	}
	

	@Override
	public int selectDocumentCountByPw(BbsDocument document) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("BbsDocumentMapper.selectDocumentCountByPw", document);
			// 비밀번호가 일치하는 데이터의 수가 0이라면 비밀번호가 잘못된 것으로 간주한다.
			if (result < 1) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("비밀번호를 확인하세요.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 수 조회에 실패했습니다.");
		}

		return result;
	}
	
	@Override
	public void deleteDocument(BbsDocument document) throws Exception {
		try {
			int result = sqlSession.delete("BbsDocumentMapper.deleteDocument", document);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("존재하지 않는 게시물에 대한 요청입니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 삭제에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void updateDocument(BbsDocument document) throws Exception {
		try {
			int result = sqlSession.update("BbsDocumentMapper.updateDocument", document);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("존재하지 않는 게시물에 대한 요청입니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 수정에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}
	
	@Override
	public void updateDocumentManagerOut(BbsDocument document) throws Exception {
		try {
			// 게시글을 작성한 적이 없는 회원도 있을 수 있기 때문에,
			// NullPointerException을 발생시키지 않는다.
			sqlSession.update("BbsDocumentMapper.updateDocumentMemberOut", document);
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("참조관계 해제에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public List<BbsDocument> selectCheckRequestBook(BbsDocument document) throws Exception {
		List<BbsDocument> result = null;

		try {
			result = sqlSession.selectList("BbsDocumentMapper.selectCheckRequestBook", document);

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("글 목록 조회(selectCheckRequestBook)에 실패했습니다.");
		}

		return result;
	}

	@Override
	public void updateDocumentSubjectOnly(BbsDocument document) throws Exception {
		try {
			int result = sqlSession.update("BbsDocumentMapper.updateDocumentSubjectOnly", document);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("존재하지 않는 게시물(updateDocumentSubjectOnly)에 대한 요청입니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시물 수정(updateDocumentSubjectOnly)에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void deleteDocumentByManagerId(BbsDocument document) throws Exception {
		try {
			sqlSession.delete("BbsDocumentMapper.deleteDocumentByManagerId", document);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("게시글 삭제에 실패했습니다(deleteDocumentByManagerId).");
		}
	}

}
