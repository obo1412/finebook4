package com.gaimit.mlm.model;

public class BbsDocument {
	private int id;
	private String category;
	private String writerName;
	private String writerPw;
	private String email;
	private String subject;
	private String content;
	private int hit;
	private String regDate;
	private String editDate;
	private String ipAddress;
	private int managerId;
	private int idLibMng;
	
	private String teamDoc;
	private String personDoc;
	
	private String reqBookTitle;
	private String reqBookAuthor;
	private String reqBookIsbn;

	// 페이지 구현을 위해서 추가된 값
	private int limitStart;
	private int listCount;

	// 갤러리 구현을 위해서 추가된 값
	private boolean gallery;
	private String imagePath;

	/** getter, setter, toString() 추가 - 코드 제시는 생략합니다. */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	public String getWriterPw() {
		return writerPw;
	}

	public void setWriterPw(String writerPw) {
		this.writerPw = writerPw;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getEditDate() {
		return editDate;
	}

	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public int getIdLibMng() {
		return idLibMng;
	}

	public void setIdLibMng(int idLibMng) {
		this.idLibMng = idLibMng;
	}

	public String getTeamDoc() {
		return teamDoc;
	}

	public void setTeamDoc(String teamDoc) {
		this.teamDoc = teamDoc;
	}

	public String getPersonDoc() {
		return personDoc;
	}

	public void setPersonDoc(String personDoc) {
		this.personDoc = personDoc;
	}

	public String getReqBookTitle() {
		return reqBookTitle;
	}

	public void setReqBookTitle(String reqBookTitle) {
		this.reqBookTitle = reqBookTitle;
	}

	public String getReqBookAuthor() {
		return reqBookAuthor;
	}

	public void setReqBookAuthor(String reqBookAuthor) {
		this.reqBookAuthor = reqBookAuthor;
	}

	public String getReqBookIsbn() {
		return reqBookIsbn;
	}

	public void setReqBookIsbn(String reqBookIsbn) {
		this.reqBookIsbn = reqBookIsbn;
	}

	public int getLimitStart() {
		return limitStart;
	}

	public void setLimitStart(int limitStart) {
		this.limitStart = limitStart;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}

	public boolean isGallery() {
		return gallery;
	}

	public void setGallery(boolean gallery) {
		this.gallery = gallery;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return "BbsDocument [id=" + id + ", category=" + category + ", writerName=" + writerName + ", writerPw="
				+ writerPw + ", email=" + email + ", subject=" + subject + ", content=" + content + ", hit=" + hit
				+ ", regDate=" + regDate + ", editDate=" + editDate + ", ipAddress=" + ipAddress + ", managerId="
				+ managerId + ", limitStart=" + limitStart + ", listCount=" + listCount + ", gallery=" + gallery
				+ ", imagePath=" + imagePath + "]";
	}

}
