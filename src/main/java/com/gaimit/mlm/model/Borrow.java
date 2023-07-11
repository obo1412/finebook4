package com.gaimit.mlm.model;

public class Borrow extends BookHeld {
	private int idBrw;
	private int idLibBrw;
	private int bookHeldId;
	private int idMemberBrw;
	private String startDateBrw;
	private String endDateBrw;
	private String dueDateBrw;
	private String restrictDateBrw;
	private int CountIdBrw;
	private int brwNow;
	//오늘 대출/반납 판에, 선택된 날짜를 받는 변수
	private String pickDateBrw;
	// 다독자, 다대출에 사용할 카운트
	private int count;
	
	
	public int getIdBrw() {
		return idBrw;
	}
	public void setIdBrw(int idBrw) {
		this.idBrw = idBrw;
	}
	public int getIdLibBrw() {
		return idLibBrw;
	}
	public void setIdLibBrw(int idLibBrw) {
		this.idLibBrw = idLibBrw;
	}
	public int getBookHeldId() {
		return bookHeldId;
	}
	public void setBookHeldId(int bookHeldId) {
		this.bookHeldId = bookHeldId;
	}
	public int getIdMemberBrw() {
		return idMemberBrw;
	}
	public void setIdMemberBrw(int idMemberBrw) {
		this.idMemberBrw = idMemberBrw;
	}
	public String getStartDateBrw() {
		return startDateBrw;
	}
	public void setStartDateBrw(String startDateBrw) {
		this.startDateBrw = startDateBrw;
	}
	public String getEndDateBrw() {
		return endDateBrw;
	}
	public void setEndDateBrw(String endDateBrw) {
		this.endDateBrw = endDateBrw;
	}
	public String getDueDateBrw() {
		return dueDateBrw;
	}
	public void setDueDateBrw(String dueDateBrw) {
		this.dueDateBrw = dueDateBrw;
	}
	public String getRestrictDateBrw() {
		return restrictDateBrw;
	}
	public void setRestrictDateBrw(String restrictDateBrw) {
		this.restrictDateBrw = restrictDateBrw;
	}
	public int getCountIdBrw() {
		return CountIdBrw;
	}
	public void setCountIdBrw(int countIdBrw) {
		CountIdBrw = countIdBrw;
	}
	public int getBrwNow() {
		return brwNow;
	}
	public void setBrwNow(int brwNow) {
		this.brwNow = brwNow;
	}
	public String getPickDateBrw() {
		return pickDateBrw;
	}
	public void setPickDateBrw(String pickDateBrw) {
		this.pickDateBrw = pickDateBrw;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "Borrow [idBrw=" + idBrw + ", idLibBrw=" + idLibBrw + ", bookHeldId=" + bookHeldId + ", idMemberBrw="
				+ idMemberBrw + ", startDateBrw=" + startDateBrw + ", endDateBrw=" + endDateBrw + ", dueDateBrw="
				+ dueDateBrw + ", restrictDateBrw=" + restrictDateBrw + "]";
	}
	
}
