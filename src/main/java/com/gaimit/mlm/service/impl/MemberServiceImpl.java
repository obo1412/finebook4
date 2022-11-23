package com.gaimit.mlm.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaimit.mlm.model.Member;
import com.gaimit.mlm.service.MemberService;

//--> import org.springframework.stereotype.Service; 
@Service
public class MemberServiceImpl implements MemberService {

	/** 처리 결과를 기록할 Log4J 객체 생성 */
	// --> import org.slf4j.Logger;
	// --> import org.slf4j.LoggerFactory;
	private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

	/** MyBatis */
	// --> import org.springframework.beans.factory.annotation.Autowired;
	// --> import org.apache.ibatis.session.SqlSession
	@Autowired
	SqlSession sqlSession;

	@Override
	public void selectUserIdCount(Member member) throws Exception {
		try {
			int result = sqlSession.selectOne("MemberMapper.selectUserIdCount", member);

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
	public void selectEmailCount(Member member) throws Exception {
		try {
			int result = sqlSession.selectOne("MemberMapper.selectEmailCount", member);

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
			result = sqlSession.selectOne("MemberMapper.selectLastJoinedId");

			// 중복된 데이터가 존재한다면?
			/*
			 * if (result > 0) { throw new NullPointerException(); }
			 */
		} catch (NullPointerException e) {
			throw new Exception("존재하는 초기 회원번호가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("아이디 키를 가져오는데 실패했습니다.");
		}
		return result;
	}

	@Override
	public void insertMember(Member member) throws Exception {
		// 아이디 중복검사 및 이메일 중복검사 호출
		/*
		 * selectUserIdCount(member); selectEmailCount(member);
		 */

		// 데이터 저장처리 = 가입
		// not null로 설정된 값이 설정되지 않았다면 예외 발생됨.
		try {
			int result = sqlSession.insert("MemberMapper.insertMember", member);
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
	public List<Member> getMemberList(Member member) throws Exception {
		List<Member> result = null;
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
	public List<Member> getMemberListByLib(Member member) throws Exception {
		List<Member> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberListByLib", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 회원 이름이 없습니다.");
		} catch (Exception e) {
			throw new Exception("회원이름 조회에 실패했습니다.");
		}
		return result;
	}
	
	@Override
	public List<Member> selectMemberListToExcel(Member member) throws Exception {
		List<Member> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberListToExcel", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("회원 정보가 존재하지 않습니다.(selectMemberListToExcel)");
		} catch (Exception e) {
			throw new Exception("회원 정보 조회(selectMemberListToExcel)에 실패했습니다.");
		}
		return result;
	}
	
	@Override
	public List<Member> selectMemberListAllForPrint(Member member) throws Exception {
		List<Member> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberListAllForPrint", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("회원증 인쇄 목록이 없습니다.(selectMemberListAllForPrint)");
		} catch (Exception e) {
			throw new Exception("회원증 인쇄 조회에 실패했습니다.(selectMemberListAllForPrint)");
		}
		return result;
	}

	@Override
	public List<Member> getMemberListByLibAndName(Member member) throws Exception {
		List<Member> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberListByLibAndName", member);
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
	public int getMemberCount(Member member) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("MemberMapper.selectMemberCount", member);
		} catch (Exception e) {
			throw new Exception("데이터 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int getMemberCountByNameAndIdLib(Member member) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("MemberMapper.selectMemberCountByNameAndIdLib", member);
		} catch (Exception e) {
			throw new Exception("데이터 조회에 실패했습니다.");
		}
		return result;
	}

	
	@Override
	public Member selectLoginInfoMember(Member member) throws Exception {
		Member result = null;
		try {
			result = sqlSession.selectOne("MemberMapper.selectLoginInfoMember", member);
			// 조회된 데이터가 없다는 것은 WHERE절 조건에 맞는 데이터가 없다는 것.
			// --> WHERE절은 아이디와 비밀번호가 일치하는 항목을 지정하므로,
			// 조회된 데이터가 없다는 것은 아이디나 비밀번호가 잘못되었음을 의미한다.
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("회원 아이디나 비밀번호가 잘못되었습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 로그인에 실패했습니다.");
		}
		return result;
	}

	

	@Override
	public void updateMemberPasswordByEmail(Member member) throws Exception {
		try {
			int result = sqlSession.update("MemberMapper.updateMemberPasswordByEmail", member);
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

	public void selectMemberPasswordCount(Member member) throws Exception {
		try {
			int result = sqlSession.selectOne("MemberMapper.selectMemberPasswordCount", member);

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
	public void updateMemberInactive(Member member) throws Exception {
		try {
			int result = sqlSession.update("MemberMapper.updateMemberInactive", member);
			// 삭제된 데이터가 없다는 것은 WHERE절의 조건값이 맞지 않다는 의미.
			// 이 경우, 첫 번째 WHERE조건에서 사용되는 id값에 대한 회원을 찾을 수 없다는 의미
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("이미 비화성화된 회원 입니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원정보 비활성화에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void updateMember(Member member) throws Exception {
		try {
			int result = sqlSession.update("MemberMapper.updateMember", member);
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
	public Member selectMember(Member member) throws Exception {
		Member result = null;

		try {
			result = sqlSession.selectOne("MemberMapper.selectMember", member);
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
	public List<Member> selectGrade(Member member) throws Exception {
		List<Member> result = null;

		try {
			result = sqlSession.selectList("MemberMapper.selectGrade", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 등급 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 등급 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public int selectPhoneCount(Member member) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("MemberMapper.selectPhoneCount", member);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("전화번호 중복검사에 실패했습니다.");
		}
		/*
		 * 아래처럼 추가하면, 그냥 검증시에 바로 메시지 뜨게 할 수 있음.
		 *  catch (NullPointerException e) {
				throw new Exception("이미 가입된 전화번호 입니다.");
			}
		 */
		return result;
	}

	@Override
	public Member getGradeItem(Member member) throws Exception {
		Member result = null;
		try {
			result = sqlSession.selectOne("MemberMapper.getGradeItem", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 등급이 없습니다.");
		} catch (Exception e) {
			throw new Exception("등급 데이터 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void updateGrade(Member member) throws Exception {
		try {
			int result =sqlSession.update("MemberMapper.updateGrade", member);
			if(result == 0) {
				throw new NullPointerException();
			}
		} catch(NullPointerException e) {
			throw new Exception("변경된 등급정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("등급정보 수정에 실패했습니다.");
		} finally {
			
		}
		
	}

	@Override
	public void insertGrade(Member member) throws Exception {
		try {
			int result = sqlSession.insert("MemberMapper.insertGrade", member);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("저장된 등급정보가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("등급 추가에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void selectGradeNameCount(Member member) throws Exception {
		try {
			int result = sqlSession.selectOne("MemberMapper.selectGradeNameCount", member);
			//중복된 데이터가 존재한다면?
			if(result > 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("이미 존재하는 등급이름입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("등급 이름 중복검사에 실패했습니다.");
		}
		
	}

	@Override
	public int selectGradeCountForPage(Member member) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("MemberMapper.selectGradeCountForPage", member);
		} catch (Exception e) {
			throw new Exception("등급 개수 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void updateMemberGradeStandardToDelete(Member member) throws Exception {
		try {
			int result =sqlSession.update("MemberMapper.updateMemberGradeStandardToDelete", member);
			if(result == 0) {
				logger.debug("해당 등급을 사용중인 회원이 없으므로 변경된 내용 없습니다.");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("기준 등급으로 정보 수정에 실패했습니다.");
		}
	}

	@Override
	public void deleteMemberGrade(Member member) throws Exception {
		try {
			int result = sqlSession.delete("MemberMapper.deleteMemberGrade", member);
			// 삭제된 데이터가 없다는 것은 WHERE절의 조건값이 맞지 않다는 의미.
			// 이 경우, 첫 번째 WHERE조건에서 사용되는 id값에 대한 회원을 찾을 수 없다는 의미
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("기준 등급은 삭제할 수 없습니다.(혹은 이미 삭제된 회원 등급입니다.)");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 등급 삭제에 실패했습니다1.");
		}
		
	}

	@Override
	public int selectGradeStandardCount(Member member) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("MemberMapper.selectGradeStandardCount", member);
		} catch (Exception e) {
			throw new Exception("기준 등급 개수 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void updateMemberGradeStandardToNormal(Member member) throws Exception {
		try {
			int result =sqlSession.update("MemberMapper.updateMemberGradeStandardToNormal", member);
			if(result == 0) {
				logger.debug("기준 등급 -> 일반 등급 변경된 내용이 없습니다.");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("기준등급 -> 일반등급 정보 수정에 실패했습니다.");
		}
	}

	@Override
	public Integer selectGradeIdByGradeNameForImportMember(Member member) throws Exception {
		Integer result = null;
		try {
			result = sqlSession.selectOne("MemberMapper.selectGradeIdByGradeNameForImportMember", member);
		} catch (Exception e) {
			throw new Exception("등급 개수 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public int selectCountMemberImportExcelData(Member member) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("MemberMapper.selectCountMemberImportExcelData", member);
		} catch (Exception e) {
			throw new Exception("등록중인 회원수 조회에 실패했습니다.(selectCountMemberImportExcelData)");
		}
		return result;
	}

	@Override
	public List<Member> selectMemberClassListByLib(Member member) throws Exception {
		List<Member> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberClassListByLib", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 분류 목록 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 분류 목록 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public Integer selectMemberClassCountForPage(Member member) throws Exception {
		Integer result = null;
		try {
			result = sqlSession.selectOne("MemberMapper.selectMemberClassCountForPage", member);
		} catch (Exception e) {
			throw new Exception("회원 분류수(selectMemberClassCountForPage) 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void insertClassMember(Member member) throws Exception {
		try {
			int result = sqlSession.insert("MemberMapper.insertClassMember", member);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("저장된 분류정보(insertClassMember)가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("분류 추가(insertClassMember)에 실패했습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void selectClassNameCountForDup(Member member) throws Exception {
		try {
			int result = sqlSession.selectOne("MemberMapper.selectClassNameCountForDup", member);
			//중복된 데이터가 존재한다면?
			if(result > 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("이미 존재하는 회원 분류명입니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 분류명 중복검사에 실패했습니다.");
		}
	}

	@Override
	public void deleteClassMemberItem(Member member) throws Exception {
		try {
			int result = sqlSession.delete("MemberMapper.deleteClassMemberItem", member);
			// 삭제된 데이터가 없다는 것은 WHERE절의 조건값이 맞지 않다는 의미.
			// 이 경우, 첫 번째 WHERE조건에서 사용되는 id값에 대한 회원을 찾을 수 없다는 의미
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("이미 삭제된 회원 분류입니다.(deleteClassMemberItem)");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 분류 삭제에 실패했습니다.(deleteClassMemberItem)");
		}
	}
	
	@Override
	public void updateMemberClassIdToNull(Member member) throws Exception {
		try {
			//int result = 
			sqlSession.update("MemberMapper.updateMemberClassIdToNull", member);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 분류 정보 수정에 실패했습니다.(updateMemberClassIdToNull)");
		} finally {
			
		}
	}

	@Override
	public Member selectClassMemberItem(Member member) throws Exception {
		Member result = null;
		try {
			result = sqlSession.selectOne("MemberMapper.selectClassMemberItem", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 분류 정보가 없습니다.(selectClassMemberItem)");
		} catch (Exception e) {
			throw new Exception("회원 분류 데이터 조회에 실패했습니다.(selectClassMemberItem)");
		}
		return result;
	}

	@Override
	public void updateClassMemberItem(Member member) throws Exception {
		try {
			int result =sqlSession.update("MemberMapper.updateClassMemberItem", member);
			if(result == 0) {
				throw new NullPointerException();
			}
		} catch(NullPointerException e) {
			throw new Exception("변경된 회원 분류 정보가 없습니다.(updateClassMemberItem)");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 분류 정보 수정에 실패했습니다.(updateClassMemberItem)");
		} finally {
			
		}
	}

	@Override
	public List<Member> selectMemberListUserSelf(Member member) throws Exception {
		List<Member> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberListUserSelf", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 데이터가 없습니다.(selectMemberListUserSelf)");
		} catch (Exception e) {
			throw new Exception("데이터 조회에 실패했습니다.(selectMemberListUserSelf)");
		}
		return result;
	}

	@Override
	public List<Member> selectMemberItemUserSelf(Member member) throws Exception {
		List<Member> result = null;
		try {
			result = sqlSession.selectList("MemberMapper.selectMemberItemUserSelf", member);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 데이터가 없습니다.(selectMemberItemUserSelf)");
		} catch (Exception e) {
			throw new Exception("데이터 조회에 실패했습니다.(selectMemberItemUserSelf)");
		}
		return result;
	}

	@Override
	public Member selectMemberByBarcodeMbr(Member member) throws Exception {
		Member result = null;
		try {
			result = sqlSession.selectOne("MemberMapper.selectMemberByBarcodeMbr", member);
			// 조회된 데이터가 없다는 것은 WHERE절 조건에 맞는 데이터가 없다는 것.
			// --> WHERE절은 아이디와 비밀번호가 일치하는 항목을 지정하므로,
			// 조회된 데이터가 없다는 것은 아이디나 비밀번호가 잘못되었음을 의미한다.
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("해당 등록번호의 회원이 존재하지 않습니다.(selectMemberByBarcodeMbr)");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원 정보조회에 실패했습니다.(selectMemberByBarcodeMbr)");
		}
		return result;
	}

	@Override
	public void deleteMemberAllDatas(Member member) throws Exception {
		try {
			sqlSession.delete("MemberMapper.deleteMemberByIdLib", member);
			sqlSession.delete("MemberMapper.deleteMemberClassByIdLib", member);
			sqlSession.delete("MemberMapper.deleteMemberGradeByIdLib", member);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("회원전체 삭제에 실패했습니다(deleteMemberAllDatas).");
		}
	}

	



	
	
	
	
	
	
	
	

	
}
