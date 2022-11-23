package com.gaimit.mlm.model;

public class Book extends Member{
	private int idBook;
	private String titleBook;
	private String writerBook;
	private String categoryBook;
	private String publisherBook;
	private String pubDateBook;
	private float priceBook;
	private String isbn10Book;
	private String isbn13Book;
	private String descriptionBook;
	private String bookOrNot;
	private int page;
	private String imageLink;
	private String classificationCode;
	private String authorCode;
	private String volumeCode;
	private int classCodeHead;
	private String classCodeColor;
	
	private int idCountry;
	private String nameCountry;
	
	public int getIdBook() {
		return idBook;
	}
	public void setIdBook(int idBook) {
		this.idBook = idBook;
	}
	public String getTitleBook() {
		return titleBook;
	}
	public void setTitleBook(String titleBook) {
		this.titleBook = titleBook;
	}
	public String getWriterBook() {
		return writerBook;
	}
	public void setWriterBook(String writerBook) {
		this.writerBook = writerBook;
	}
	public String getCategoryBook() {
		return categoryBook;
	}
	public void setCategoryBook(String categoryBook) {
		this.categoryBook = categoryBook;
	}
	public String getPublisherBook() {
		return publisherBook;
	}
	public void setPublisherBook(String publisherBook) {
		this.publisherBook = publisherBook;
	}
	public String getPubDateBook() {
		return pubDateBook;
	}
	public void setPubDateBook(String pubDateBook) {
		this.pubDateBook = pubDateBook;
	}
	
	public float getPriceBook() {
		return priceBook;
	}
	public void setPriceBook(float priceBook) {
		this.priceBook = priceBook;
	}
	public String getIsbn10Book() {
		return isbn10Book;
	}
	public void setIsbn10Book(String isbn10Book) {
		this.isbn10Book = isbn10Book;
	}
	public String getIsbn13Book() {
		return isbn13Book;
	}
	public void setIsbn13Book(String isbn13Book) {
		this.isbn13Book = isbn13Book;
	}
	public String getDescriptionBook() {
		return descriptionBook;
	}
	public void setDescriptionBook(String descriptionBook) {
		this.descriptionBook = descriptionBook;
	}
	public String getBookOrNot() {
		return bookOrNot;
	}
	public void setBookOrNot(String bookOrNot) {
		this.bookOrNot = bookOrNot;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public String getClassificationCode() {
		return classificationCode;
	}
	public void setClassificationCode(String classificationCode) {
		this.classificationCode = classificationCode;
	}
	public String getAuthorCode() {
		return authorCode;
	}
	public void setAuthorCode(String authorCode) {
		this.authorCode = authorCode;
	}
	public String getVolumeCode() {
		return volumeCode;
	}
	public void setVolumeCode(String volumeCode) {
		this.volumeCode = volumeCode;
	}
	public int getClassCodeHead() {
		return classCodeHead;
	}
	public void setClassCodeHead(int classCodeHead) {
		this.classCodeHead = classCodeHead;
	}
	public String getClassCodeColor() {
		return classCodeColor;
	}
	public void setClassCodeColor(String classCodeColor) {
		this.classCodeColor = classCodeColor;
	}
	public int getIdCountry() {
		return idCountry;
	}
	public void setIdCountry(int idCountry) {
		this.idCountry = idCountry;
	}
	public String getNameCountry() {
		return nameCountry;
	}
	public void setNameCountry(String nameCountry) {
		this.nameCountry = nameCountry;
	}
	@Override
	public String toString() {
		return "Book [idBook=" + idBook + ", titleBook=" + titleBook + ", writerBook=" + writerBook + ", categoryBook="
				+ categoryBook + ", publisherBook=" + publisherBook + ", pubDateBook=" + pubDateBook + ", priceBook="
				+ priceBook + ", isbn10Book=" + isbn10Book + ", isbn13Book=" + isbn13Book + ", descriptionBook="
				+ descriptionBook + ", bookOrNot=" + bookOrNot + ", page=" + page + ", imageLink=" + imageLink
				+ ", classificationCode=" + classificationCode + ", authorCode=" + authorCode + ", volumeCode="
				+ volumeCode + ", classCodeHead=" + classCodeHead + ", idCountry=" + idCountry + ", nameCountry="
				+ nameCountry + "]";
	}
	
}
