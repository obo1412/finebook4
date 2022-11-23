package com.gaimit.mlm.model;

public class Manager extends Library {
	private int idMng;
	private int idLibMng;
	private String userIdMng;
	private String userPwMng;
	private String nameMng;
	private String emailMng;
	private int langMng;

	public String getUserPwMng() {
		return userPwMng;
	}

	public void setUserPwMng(String userPwMng) {
		this.userPwMng = userPwMng;
	}

	public int getIdLibMng() {
		return idLibMng;
	}

	public void setIdLibMng(int idLibMng) {
		this.idLibMng = idLibMng;
	}

	public String getUserIdMng() {
		return userIdMng;
	}

	public void setUserIdMng(String userIdMng) {
		this.userIdMng = userIdMng;
	}

	public String getNameMng() {
		return nameMng;
	}

	public void setNameMng(String nameMng) {
		this.nameMng = nameMng;
	}

	public int getIdMng() {
		return idMng;
	}

	public void setIdMng(int idMng) {
		this.idMng = idMng;
	}

	public String getEmailMng() {
		return emailMng;
	}

	public void setEmailMng(String emailMng) {
		this.emailMng = emailMng;
	}

	public int getLangMng() {
		return langMng;
	}

	public void setLangMng(int langMng) {
		this.langMng = langMng;
	}

	@Override
	public String toString() {
		return "Manager [idMng=" + idMng + ", idLibMng=" + idLibMng + ", userIdMng=" + userIdMng + ", userPwMng="
				+ userPwMng + ", nameMng=" + nameMng + ", emailMng=" + emailMng + ", langMng=" + langMng + "]";
	}

}
