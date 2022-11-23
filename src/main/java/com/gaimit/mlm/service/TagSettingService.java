package com.gaimit.mlm.service;

import java.util.List;

import com.gaimit.mlm.model.TagSetting;

/** 회원 관련 기능을 제공하기 위한 Service 계층 */
public interface TagSettingService {
	/**
	 * 아이디 중복검사
	 * @param member - 아이디
	 * @throws Exception - 중복된 데이터인 경우 예외 발생함
	 */
	public TagSetting selectRollTagPositionValue(TagSetting tag) throws Exception;
	
	
	public void updateTagPosition(TagSetting tag) throws Exception;
	
	
	public void updatePrintingEaAndSheetCount(TagSetting tag) throws Exception;
	
	
	public void updatePrintingSheetCountUp(TagSetting tag) throws Exception;
	
	
	public int selectCurrentSheetCount(TagSetting tag) throws Exception;
	
	/**
	 * 해당 도서관 id에 태그table정보가 있는지 확인
	 * @param tag
	 * @return
	 * @throws Exception
	 */
	public int selectTagInfoCountByLib(TagSetting tag) throws Exception;
	
	/**
	 * 기본 태그 값 넣어주기
	 */
	public void insertDefaultTagSetting(TagSetting tag) throws Exception;
	
	/**
	 * 개인설정 색상 관리용 넣어주기
	 * @param tag
	 * @throws Exception
	 */
	public void insertCustomColor(TagSetting tag) throws Exception;
	
	/*
	 *기본 설정 색상 가져오기 
	 */
	public List<TagSetting> selectTagColorsBy0(TagSetting tag) throws Exception;
	
	/**
	 * 관리용 개인색상 불러오기
	 * @param tag
	 * @return
	 * @throws Exception
	 */
	public TagSetting selectCustomColor(TagSetting tag) throws Exception;
	
	/**
	 * 태그 색상 설정 변경
	 * @param tag
	 * @throws Exception
	 */
	public void updateTagColor(TagSetting tag) throws Exception;
	
	/**
	 * 도서관 삭제를 위한 태그세팅 목록 삭제
	 * @param tag
	 * @throws Exception
	 */
	public void deleteTagSettingByIdLib(TagSetting tag) throws Exception;
}
