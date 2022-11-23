package com.gaimit.mlm.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaimit.mlm.model.TagSetting;
import com.gaimit.mlm.service.TagSettingService;

//--> import org.springframework.stereotype.Service; 
@Service
public class TagSettingServiceImpl implements TagSettingService {

	/** 처리 결과를 기록할 Log4J 객체 생성 */
	// --> import org.slf4j.Logger;
	// --> import org.slf4j.LoggerFactory;
	private static Logger logger = LoggerFactory.getLogger(TagSettingServiceImpl.class);

	/** MyBatis */
	// --> import org.springframework.beans.factory.annotation.Autowired;
	// --> import org.apache.ibatis.session.SqlSession
	@Autowired
	SqlSession sqlSession;

	@Override
	public TagSetting selectRollTagPositionValue(TagSetting tag) throws Exception {
		TagSetting result = null;

		try {
			result = sqlSession.selectOne("TagSettingMapper.selectRollTagPositionValue", tag);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 롤태그 위치가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("롤태그 위치 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public void updateTagPosition(TagSetting tag) throws Exception {
		try {
			int result = sqlSession.update("TagSettingMapper.updateTagPosition", tag);
			// 수정된 행의 수가 없다는 것은 WHERE절 조건이 맞지 않기 때문이다.
			// 즉, 입력한 이메일과 일치하는 데이터가 없다는 의미
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("수정된 태그 위치 정보가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("태그 위치 수정에 실패하였습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void updatePrintingEaAndSheetCount(TagSetting tag) throws Exception {
		try {
			sqlSession.update("TagSettingMapper.updatePrintingEaAndSheetCount", tag);
			// 수정된 행의 수가 없다는 것은 WHERE절 조건이 맞지 않기 때문이다.
			// 즉, 입력한 이메일과 일치하는 데이터가 없다는 의미
			/*if (result == 0) {
				throw new NullPointerException();
			}*/
		} /*catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("수정된 태그 위치 정보가 없습니다.");
		}*/ catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("태그 인쇄 카운팅 업데이트에 실패하였습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void updatePrintingSheetCountUp(TagSetting tag) throws Exception {
		try {
			int result = sqlSession.update("TagSettingMapper.updatePrintingSheetCountUp", tag);
			// 수정된 행의 수가 없다는 것은 WHERE절 조건이 맞지 않기 때문이다.
			// 즉, 입력한 이메일과 일치하는 데이터가 없다는 의미
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("업데이트된 자동 Sheet counting 값이 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("자동 Sheet counting 업데이트에 실패하였습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public int selectCurrentSheetCount(TagSetting tag) throws Exception {
		int result = 0;

		try {
			result = sqlSession.selectOne("TagSettingMapper.selectCurrentSheetCount", tag);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 현재 인쇄시트 값이 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("현재 인쇄시트 값 조회에 실패했습니다.");
		}

		return result;
	}

	@Override
	public int selectTagInfoCountByLib(TagSetting tag) throws Exception {
		int result = 0;
		try {
			result = sqlSession.selectOne("TagSettingMapper.selectTagInfoCountByLib", tag);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("selectTagInfoCountByLib 정보 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void insertDefaultTagSetting(TagSetting tag) throws Exception {
		insertCustomColor(tag);
		try {
			int result = sqlSession.insert("TagSettingMapper.insertDefaultTagSetting", tag);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("저장된 태그 세팅 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("태그세팅(DefaultTagSetting) 정보저장에 실패했습니다.");
		}
	}
	
	@Override
	public void insertCustomColor(TagSetting tag) throws Exception {
		try {
			int result = sqlSession.insert("TagSettingMapper.insertCustomColor", tag);
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("저장된 관리색상(insertCustomColor) 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("관리색상(insertCustomColor) 정보저장에 실패했습니다.");
		}
	}

	@Override
	public List<TagSetting> selectTagColorsBy0(TagSetting tag) throws Exception {
		List<TagSetting> result = null;
		try {
			result = sqlSession.selectList("TagSettingMapper.selectTagColorsBy0", tag);
			if (result == null) {
				throw new NullPointerException();
			}
			//개인설정용 색상 삽입
			result.add(selectCustomColor(tag));
		} catch (NullPointerException e) {
			throw new Exception("조회된 색상 양식(selectTagColorsBy0)이 없습니다.");
		} catch (Exception e) {
			throw new Exception("색상 양식 조회(selectTagColorsBy0)에 실패했습니다.");
		}
		return result;
	}

	//이놈은 selectTagColorsBy0 이 안에서만 사용.
	@Override
	public TagSetting selectCustomColor(TagSetting tag) throws Exception {
		TagSetting result = null;
		try {
			result = sqlSession.selectOne("TagSettingMapper.selectCustomColor", tag);
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			throw new Exception("조회된 관리색상(selectCustomColor) 정보가 없습니다.");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("관리색상(selectCustomColor) 조회에 실패했습니다.");
		}
		return result;
	}

	@Override
	public void updateTagColor(TagSetting tag) throws Exception {
		try {
			int result = sqlSession.update("TagSettingMapper.updateTagColor", tag);
			// 수정된 행의 수가 없다는 것은 수정이 되지 않았다는 말이다.
			if (result == 0) {
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			// sqlSession.rollback();
			throw new Exception("수정된 태그 색상(updateTagColor) 정보가 없습니다.");
		} catch (Exception e) {
			// sqlSession.rollback();
			logger.error(e.getLocalizedMessage());
			throw new Exception("태그 색상 수정(updateTagColor)에 실패하였습니다.");
		} finally {
			// sqlSession.commit();
		}
	}

	@Override
	public void deleteTagSettingByIdLib(TagSetting tag) throws Exception {
		try {
			sqlSession.delete("TagSettingMapper.deleteTagSettingByIdLib", tag);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new Exception("태그세팅 삭제에 실패했습니다(deleteTagSettingByIdLib).");
		}
	}

	
}
