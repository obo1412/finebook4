package com.gaimit.mlm.model;

public class Member extends MemberGrade{
	private int id;
	private int idLib;
	private String userId;
	private String userPw;
	private String name;
	private String phone;
	private String otherContact;
	private String birthdate;
	private String email;
	private String postcode;
	private String addr1;
	private String addr2;
	private String remarks;
	private String regDate;
	private String editDate;
	private String barcodeMbr;
	private String profileImg;
	private String rfUid;
	private int gradeId;
	private int status;
	
	
	private int limitStart;
	private int listCount;
	//member class 반 분류
	private Integer classId;
	private int idMbrClass;
	private String className;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdLib() {
		return idLib;
	}
	public void setIdLib(int idLib) {
		this.idLib = idLib;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOtherContact() {
		return otherContact;
	}
	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public String getBarcodeMbr() {
		return barcodeMbr;
	}
	public void setBarcodeMbr(String barcodeMbr) {
		this.barcodeMbr = barcodeMbr;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public String getRfUid() {
		return rfUid;
	}
	public void setRfUid(String rfUid) {
		this.rfUid = rfUid;
	}
	public int getGradeId() {
		return gradeId;
	}
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public int getIdMbrClass() {
		return idMbrClass;
	}
	public void setIdMbrClass(int idMbrClass) {
		this.idMbrClass = idMbrClass;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String toString() {
		return "Member [id=" + id + ", idLib=" + idLib + ", name=" + name + ", phone=" + phone + ", birthdate="
				+ birthdate + ", email=" + email + ", postcode=" + postcode + ", addr1=" + addr1 + ", addr2=" + addr2
				+ ", remarks=" + remarks + ", regDate=" + regDate + ", editDate=" + editDate + ", barcodeMbr="
				+ barcodeMbr + ", profileImg=" + profileImg + ", rfUid=" + rfUid + ", gradeId=" + gradeId + ", status="
				+ status + ", limitStart=" + limitStart + ", listCount=" + listCount + "]";
	}
}
