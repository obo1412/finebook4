package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.Member;

/** 회원 관련 기능을 제공하기 위한 Service 계층 */
public interface MemberService {
	/**
	 * 아이디 중복검사
	 * @param member - 아이디
	 * @throws Exception - 중복된 데이터인 경우 예외 발생함
	 */
	public void selectUserIdCount(Member member) throws Exception;
	
	/**
	 * 이메일 중복검사
	 * @param member - 이메일
	 * @throws Exception - 중복된 데이터인 경우 예외 발생함
	 */
	public void selectEmailCount(Member member) throws Exception;
	
	
	/**
	 * 마지막 등록 id 조회
	 * @param member - 최종 pk키 확인하기
	 * @throws Exception
	 */
	public int selectLastJoinedId() throws Exception;
	
	
	/**
	 * 회원가입(아이디,이메일 중복검사 후 가입처리)
	 * @param member - 일련번호, 가입일시,변경일시를 제외한 모든 정보
	 * @throws Exception
	 */
	public void insertMember(Member member) throws Exception;
	
	/**
	 * 회원목록조회(비밀정보를 제외한 모든 정보)
	 * @param member - 회원 id 조회
	 * @throws Exception
	 */
	public List<Member> getMemberList(Member member) throws Exception;
	
	/**
	 * 도서관별 회원목록조회(모든 정보)
	 * @param member - 도서관 id_lib 조회
	 * @throws Exception
	 */
	public List<Member> getMemberListByLib(Member member) throws Exception;
	
	/**
	 * 도서관별 회원목록 엑셀 변환에 쓰일 쿼리, 회원 전체
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public List<Member> selectMemberListToExcel(Member member) throws Exception;
	
	/**
	 * 전체 회원증 프린트를 위한
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public List<Member> selectMemberListAllForPrint(Member member) throws Exception;
	
	/**
	 * 
	 * @param member 이름과 도서관번호
	 * @return 이름과 도서관 번호가 일치하는 회원 목록
	 * @throws Exception
	 */
	public List<Member> getMemberListByLibAndName(Member member) throws Exception;
	
	/**
	 * 회원수 조회(아이디)
	 * @param member - 회원 id 개수 조회
	 * @throws Exception
	 */
	public int getMemberCount(Member member) throws Exception;
	
	/**
	 * 
	 * @param member 회원 이름과 도서관id
	 * @return
	 * @throws Exception
	 */
	public int getMemberCountByNameAndIdLib(Member member) throws Exception;
	
	/**
	 * 로그인
	 * @param member - 아이디 비밀번호
	 * @return 회원정보
	 * @throws Exception
	 */
	public Member selectLoginInfoMember(Member member) throws Exception;
	
	/**
	 * 비밀번호 변경
	 * @param member - 이메일주소. 비밀번호
	 * @throws Exception
	 */
	public void updateMemberPasswordByEmail(Member member) throws Exception;
	
	/**
	 * 비밀번호 검사
	 * @param member
	 * @throws Exception
	 */
	public void selectMemberPasswordCount(Member member) throws Exception;
	
	/**
	 * 회원 비활성화
	 * @param member - 회원번호, 비밀번호
	 * @throws Exception
	 */
	public void updateMemberInactive(Member member) throws Exception;
	
	/**
	 * 회원정보 수정
	 * @param member
	 * @throws Exception
	 */
	public void updateMember(Member member) throws Exception;
	
	/**
	 * 일련번호에 의한 회원정보 조회
	 * @param member
	 * @throws Exception
	 */
	public Member selectMember(Member member) throws Exception;
	
	/**
	 * 고객 등급 목록 조회
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public List<Member> selectGrade(Member member) throws Exception;
	
	/**
	 * idLib와 phone으로 전화번호 중복 검사
	 * @param member
	 * @throws Exception
	 */
	public int selectPhoneCount(Member member) throws Exception;
	
	/**
	 * idLib와 gradeId를 이용하여 등급 아이템 조회
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Member getGradeItem(Member member) throws Exception;
	
	/**
	 * gradeId와 idLib를 이용하여 등급 수정
	 * @param member
	 * @throws Exception
	 */
	public void updateGrade(Member member) throws Exception;
	
	/**
	 * grade 추가
	 * @param member
	 * @throws Exception
	 */
	public void insertGrade(Member member) throws Exception;
	
	/**
	 * grade 이름 중복검사
	 * @param member
	 * @throws Exception
	 */
	public void selectGradeNameCount(Member member) throws Exception;
	
	/**
	 * 페이지네이션을 위한 등급 수 검색
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public int selectGradeCountForPage(Member member) throws Exception;
	
	/**
	 * 등급정보 삭제 전, 참조걸린 해당 등급을 변경(제거)함.
	 * @param member
	 * @throws Exception
	 */
	public void updateMemberGradeStandardToDelete(Member member) throws Exception;
	
	/**
	 * 해당 등급을 삭제함.
	 * @param member
	 * @throws Exception
	 */
	public void deleteMemberGrade(Member member) throws Exception;
	
	/**
	 * 기준 등급 개수 조사
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public int selectGradeStandardCount(Member member) throws Exception;
	
	/**
	 * 기준등급은 하나만 존재해야하기 때문에
	 * 기존의 기준 등급을 일반등급으로 변경
	 * @param member
	 * @throws Exception
	 */
	public void updateMemberGradeStandardToNormal(Member member) throws Exception;
	
	/**
	 * 회원정보 가져오기를 위한, 등급ID값 가져오기
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Integer selectGradeIdByGradeNameForImportMember(Member member) throws Exception;
	
	
	/**
	 * 등록시작시간 기준으로 몇건의 회원 데이터가 이동했는지 확인 하기 위함
	 * @param dcdMember - idLib 도서관 번호, editDate 등록시간(check페이지 접속시간)
	 * @return
	 * @throws Exception
	 */
	public int selectCountMemberImportExcelData(Member member) throws Exception;
	
	/**
	 * 회원 분류 목록 가져오기 (반, 팀 등)
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public List<Member> selectMemberClassListByLib(Member member) throws Exception;
	
	/**
	 * 회원 분류 목록 개수 (페이징) 가져오기
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Integer selectMemberClassCountForPage(Member member) throws Exception;
	
	/**
	 * 회원 분류 추가! className 분류명과 idLib
	 * @param member
	 * @throws Exception
	 */
	public void insertClassMember(Member member) throws Exception;
	
	/**
	 * 회원 분류명 중복검사
	 * @param member
	 * @throws Exception
	 */
	public void selectClassNameCountForDup(Member member) throws Exception;
	
	/**
	 * 해당 분류를 삭제함
	 * @param member
	 * @throws Exception
	 */
	public void deleteClassMemberItem(Member member) throws Exception;
	
	/**
	 * 회원분류 삭제하기 전에, 해당 회원분류를 가진 회원의 회원분류 null로 바꾸기
	 * @param member
	 * @throws Exception
	 */
	public void updateMemberClassIdToNull(Member member) throws Exception;
	
	/**
	 * 회원 분류 item 조회
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Member selectClassMemberItem(Member member) throws Exception;
	
	/**
	 * 회원 분류 정보 수정 현재는 분류명만 수정 다른 정보는 없음.
	 * @param member
	 * @throws Exception
	 */
	public void updateClassMemberItem(Member member) throws Exception;
	
	/**
	 * 사용자 화면에서 회원 목록 가져오기.
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public List<Member> selectMemberListUserSelf(Member member) throws Exception;
	
	/**
	 * 사용자 화면에서, 회원이름 또는 회원번호 온전히 맞을 경우.
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public List<Member> selectMemberItemUserSelf(Member member) throws Exception;
	
	/**
	 * 회원 등록번호로, 회원정보 조회 1개만
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public Member selectMemberByBarcodeMbr(Member member) throws Exception;
	
	/**
	 * 도서관 삭제를 위한, 회원정보, 회원분류, 회원등급 삭제
	 * @param member
	 * @throws Exception
	 */
	public void deleteMemberAllDatas(Member member) throws Exception;
	
	
	
	
	
	
	
	
	
	
	
}
