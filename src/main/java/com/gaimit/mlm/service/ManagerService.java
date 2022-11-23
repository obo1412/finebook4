package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.Manager;

/** 회원 관련 기능을 제공하기 위한 Service 계층 */
public interface ManagerService {
	/**
	 * 아이디 중복검사
	 * @param member - 아이디
	 * @throws Exception - 중복된 데이터인 경우 예외 발생함
	 */
	public void selectUserIdCount(Manager manager) throws Exception;
	
	/**
	 * 이메일 중복검사
	 * @param member - 이메일
	 * @throws Exception - 중복된 데이터인 경우 예외 발생함
	 */
	public void selectEmailCount(Manager manager) throws Exception;
	
	
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
	public void insertManager(Manager manager) throws Exception;
	
	/**
	 * 회원목록조회(비밀정보를 제외한 모든 정보)
	 * @param member - 회원 id 조회
	 * @throws Exception
	 */
	public List<Manager> getManagerList(Manager manager) throws Exception;
	
	/**
	 * 회원수 조회(아이디)
	 * @param member - 회원 id 개수 조회
	 * @throws Exception
	 */
	public int getManagerCount(Manager manager) throws Exception;
	
	/**
	 * 로그인
	 * @param member - 아이디 비밀번호
	 * @return 회원정보
	 * @throws Exception
	 */
	public Manager selectLoginInfo(Manager manager) throws Exception;
	
	/**
	 * 비밀번호 변경
	 * @param member - 이메일주소. 비밀번호
	 * @throws Exception
	 */
	public void updateManagerPasswordByEmail(Manager manager) throws Exception;
	
	/**
	 * 비밀번호 검사
	 * @param member
	 * @throws Exception
	 */
	public void selectManagerPasswordCount(Manager manager) throws Exception;
	
	/**
	 * 회원탈퇴
	 * @param member - 회원번호, 비밀번호
	 * @throws Exception
	 */
	public void deleteManager(Manager manager) throws Exception;
	
	/**
	 * 회원정보 수정
	 * @param member
	 * @throws Exception
	 */
	public void updateManager(Manager manager) throws Exception;
	
	/**
	 * 일련번호에 의한 회원정보 조회
	 * @param member
	 * @throws Exception
	 */
	public Manager selectManager(Manager manager) throws Exception;
	
	/**
	 * 언어 정보 수정 
	 * @param manager
	 * @throws Exception
	 */
	public void updateManagerLanguage(Manager manager) throws Exception;
	
	/**
	 * 도서관별 관리자 조회 admin_setting에 사용
	 * @param manager id_lib_mng 도서관 번호 사용
	 * @return
	 * @throws Exception
	 */
	public List<Manager> selectManagerListByLib(Manager manager) throws Exception;
	
	/**
	 * 아이디 중복된 아이디 없는지 체크!!!
	 * @param manager
	 * @return
	 * @throws Exception
	 */
	public int selectCountForUserIdDuplicateCheck(Manager manager) throws Exception;
	
	
	
	
	
	
	
	
}
