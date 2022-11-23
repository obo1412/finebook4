package com.gaimit.helper;

import org.springframework.beans.factory.annotation.Autowired;

import com.gaimit.mlm.model.BookHeld;
import com.gaimit.mlm.service.BookHeldService;

/**
 * 기본적인 공통 기능들을 묶어 놓은 클래스
 */
public class FreqFunction {
	// ----------- 싱글톤 객체 생성 시작 -----------
	private static FreqFunction current = null;

	public static FreqFunction getInstance() {
		if (current == null) {
			current = new FreqFunction();
		}
		return current;
	}

	public static void freeInstance() {
		current = null;
	}

	private FreqFunction() {
		super();
	}
	// ----------- 싱글톤 객체 생성 끝 -----------

	@Autowired
	BookHeldService bookHeldService;
	
	public String testFunction(BookHeld bookHeld) throws Exception {
		String result = null;
		bookHeld = bookHeldService.getBookHelditem(bookHeld);
		result = String.valueOf(bookHeld.getCopyCode());
		return result;
	}
	
}
