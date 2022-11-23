package com.gaimit.mlm.model;

public class BookCheckModel extends Borrow{
	
	//book_check_status table
	private int idBcs;
	private int idLib;
	private String checkDate;
	//전체 도서수
	private int wholeCount;
	//점검 회수
	private int checkedCount;
	//정상 확인된 도서
	private int confirmCount;
	//발견된 중복점검 도서
	private int redupCount;
	//도서관 내 전체 대출중 도서수
	private int brwedCount;
	//발견된 대출중 도서수
	private int rentedCheckedCount;
	//발견된 미등록 도서수
	private int unregCount;
	//아직 미점검 도서수
	private int uncheckedCount;
	
	//book_check_list table
	private int idBcl;
	private String inputBarcode;
	private Integer idBookHeld;
	private String checkResult;
	private String checkIp;
	
	public int getIdBcs() {
		return idBcs;
	}
	public void setIdBcs(int idBcs) {
		this.idBcs = idBcs;
	}
	public int getIdLib() {
		return idLib;
	}
	public void setIdLib(int idLib) {
		this.idLib = idLib;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public int getWholeCount() {
		return wholeCount;
	}
	public void setWholeCount(int wholeCount) {
		this.wholeCount = wholeCount;
	}
	public int getCheckedCount() {
		return checkedCount;
	}
	public void setCheckedCount(int checkedCount) {
		this.checkedCount = checkedCount;
	}
	public int getConfirmCount() {
		return confirmCount;
	}
	public void setConfirmCount(int confirmCount) {
		this.confirmCount = confirmCount;
	}
	public int getRedupCount() {
		return redupCount;
	}
	public void setRedupCount(int redupCount) {
		this.redupCount = redupCount;
	}
	public int getBrwedCount() {
		return brwedCount;
	}
	public void setBrwedCount(int brwedCount) {
		this.brwedCount = brwedCount;
	}
	public int getRentedCheckedCount() {
		return rentedCheckedCount;
	}
	public void setRentedCheckedCount(int rentedCheckedCount) {
		this.rentedCheckedCount = rentedCheckedCount;
	}
	public int getUnregCount() {
		return unregCount;
	}
	public void setUnregCount(int unregCount) {
		this.unregCount = unregCount;
	}
	public int getUncheckedCount() {
		return uncheckedCount;
	}
	public void setUncheckedCount(int uncheckedCount) {
		this.uncheckedCount = uncheckedCount;
	}
	public int getIdBcl() {
		return idBcl;
	}
	public void setIdBcl(int idBcl) {
		this.idBcl = idBcl;
	}
	public String getInputBarcode() {
		return inputBarcode;
	}
	public void setInputBarcode(String inputBarcode) {
		this.inputBarcode = inputBarcode;
	}
	public Integer getIdBookHeld() {
		return idBookHeld;
	}
	public void setIdBookHeld(Integer idBookHeld) {
		this.idBookHeld = idBookHeld;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getCheckIp() {
		return checkIp;
	}
	public void setCheckIp(String checkIp) {
		this.checkIp = checkIp;
	}
	
	
}
