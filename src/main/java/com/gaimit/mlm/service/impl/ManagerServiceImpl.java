package com.gaimit.mlm.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaimit.mlm.model.Manager;
import com.gaimit.mlm.service.ManagerService;

//--> import org.springframework.stereotype.Service; 
@Service
public class ManagerServiceImpl implements ManagerService {
	
	/** 처리 결과를 기록할 Log4J 객체 생성 */
	// --> import org.slf4j.Logger;
	// --> import org.slf4j.LoggerFactory;
	private static Logger logger = LoggerFactory.getLogger(ManagerServiceImpl.class);

	/** MyBatis */
	// --> import org.springframework.beans.factory.annotation.Autowired;
	// --> import org.apache.ibatis.session.SqlSession
	@Autowired
	SqlSession sqlSession;

	@Override
	public void selectUserIdCount(Manager manager) throws Exception {
		try {
			int result = sqlSession.selectOne("ManagerMapper.selectUserIdCount", manager);

			// 중복된 데이터가 존재한다면?
			if (result > 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("이미 사용중인 아이디 입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("아이디 중복검사에 실패했습니다.");
		}
	}

	@Override
	public void selectEmailCount(Manager manager) throws Exception {
		try {
			int result = sqlSession.selectOne("ManagerMapper.selectEmailCount", manager);

			// 중복된 데이터가 존재한다면?
			if (result > 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("이미 사용중인 이메일 입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("이메일 중복검사에 실패했습니다.");
		}
	}
	
	@Override
	public int selectLastJoinedId() throws Exception {
		int result;
		try {
			result = sqlSession.selectOne("ManagerMapper.selectLastJoinedId");

			// 중복된 데이터가 존재한다면?
			/*if (result > 0) {
				throw new NullPointerException();
			}*/
		} catch (NullPointerException e) {
			throw new Exception("이미 가입된 번호 입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("아이디 키를 가져오는데 실패했습니다.");
		}
		return result;
	}
	
	@Override
	public void insertManager(Manager manager) throws Exception {
		// 아이디 중복검사 및 이메일 중복검사 호출
		/*selectUserIdCount(manager);
		selectEmailCount(manager);*/

		// 데이터 저장처리 = 가입
		// not null로 설정된 값이 설정되지 않았다면 예외 발생됨.
		try {
			int result = sqlSession.insert("ManagerMapper.insertManager", manager);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("저장된 회원정보가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원가입에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}
	
	@Override
	public List<Manager> getManagerList(Manager member) throws Exception {
		List<Manager> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberList", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 데이터가 없습니다.");
		} catch (Exception e) {
			throw new Exception("데이터 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int getManagerCount(Manager manager) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("ManagerMapper.selectManagerCount", manager);
		} catch (Exception e) {
			throw new Exception("데이터 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public Manager selectLoginInfo(Manager manager) throws Exception {
		Manager result = null;

		try {
			result = sqlSession.selectOne("ManagerMapper.selectLoginInfo", manager);

			// 조회된 데이터가 없다는 것은 WHERE절 조건에 맞는 데이터가 없다는 것.
			// --> WHERE절은 아이디와 비밀번호가 일치하는 항목을 지정하므로,
			// 조회된 데이터가 없다는 것은 아이디나 비밀번호가 잘못되었음을 의미한다.
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("아이디나 비밀번호가 잘못되었습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("로그인에 실패했습니다.");
		}

		return result;
	}

	@Override
	public void updateManagerPasswordByEmail(Manager manager) throws Exception {
		try {
			int result = sqlSession.update("ManagerMapper.updateManagerPasswordByEmail", manager);
			// 수정된 행의 수가 없다는 것은 WHERE절 조건이 맞지 않기 때문이다.
			// 즉, 입력한 이메일과 일치하는 데이터가 없다는 의미
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("가입된 이메일이 아닙니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("비밀번호 변경에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}
	
	public void selectManagerPasswordCount(Manager manager) throws Exception {
		try {
			int result = sqlSession.selectOne("ManagerMapper.selectManagerPasswordCount", manager);

			// 회원번호와 비밀번호가 일치하는 데이터가 0이라면, 비밀번호가 잘못된 상태
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("잘못된 비밀번호 입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("비밀번호 검사에 실패했습니다.");
		}
	}

	@Override
	public void deleteManager(Manager manager) throws Exception {
		try {
			int result = sqlSession.delete("ManagerMapper.deleteManager", manager);
			// 삭제된 데이터가 없다는 것은 WHERE절의 조건값이 맞지 않다는 의미.
			// 이 경우, 첫 번째 WHERE조건에서 사용되는 id값에 대한 회원을 찾을 수 없다는 의미
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("이미 삭제된 관리자(deleteManager) 입니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("관리자 삭제(deleteManager)에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void updateManager(Manager manager) throws Exception {
		try {
			int result = sqlSession.update("ManagerMapper.updateManager", manager);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("변경된 회원정보가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원정보 수정에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public Manager selectManager(Manager manager) throws Exception {
		Manager result = null;
		
		try {
			result = sqlSession.selectOne("ManagerMapper.selectManager", manager);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원정보 조회에 실패했습니다.");
		}
		
		return result;
	}

	@Override
	public void updateManagerLanguage(Manager manager) throws Exception {
		try {
			int result = sqlSession.update("ManagerMapper.updateManagerLanguage", manager);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("변경된 회원별 언어 정보가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원정보 수정(언어설정)에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public List<Manager> selectManagerListByLib(Manager manager) throws Exception {
		List<Manager> result = null;
		try {
			result = sqlSession.selectList("ManagerMapper.selectManagerListByLib", manager);
		} catch (Exception e) {
			throw new Exception("해당 도서관 관리자목록 조회(selectManagerListByLib)에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectCountForUserIdDuplicateCheck(Manager manager) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("ManagerMapper.selectCountForUserIdDuplicateCheck", manager);
			// 중복된 데이터가 존재한다면?
			if (result > 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("이미 존재하는 관리자 아이디입니다.(selectCountForUserIdDuplicateCheck)");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("관리자 아이디 중복 조회(selectCountForUserIdDuplicateCheck)에 실패했습니다.");
		}
		return result;
	}

}
