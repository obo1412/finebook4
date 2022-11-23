package com.gaimit.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtHelper {

	//토큰 시크릿 키
	private final String secretKey = "blessinglibshop";

	//토큰 생성하기.
	public String createToken(int id_mng) {
		
		//만료 기간 설정
		Long expiredTime = 1000 * 60L * 60L * 24L * 7L;
//		Long expiredTime = 10L ;
		
		Date date = new Date();
		date.setTime(date.getTime()+expiredTime);
		
		//header 설정부분
		Map<String, Object> headers = new HashMap<>();
		headers.put("typ", "JWT");
		
		//페이로드 설정
		Map<String, Object> payloads = new HashMap<>();
		payloads.put("id", id_mng);
		
		//클레임설정
		Claims claims = Jwts.claims()
				.setSubject("auto_login")
				.setExpiration(date);
		
		claims.putAll(payloads);
		
		
		String jwt = Jwts.builder()
				.setHeader(headers)
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
				.compact();
		
		return jwt;
	}
	
	//값 읽어서 가져오기.
	public Map<String, Object> verifyJWT(String jwt) {
		Map<String, Object> resultMap = null;
		
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(secretKey.getBytes())
					.parseClaimsJws(jwt).getBody();
			resultMap = claims;
		} catch (ExpiredJwtException e) {
			return resultMap;
		} catch (Exception e) {
			return resultMap; 
		}
		
		return resultMap;
	}
}
