package com.gaimit.mlm.model;

//도서관이 보유하고 있는 책 정보

public class BookHeld extends Book{
	private int id;
	private int libraryIdLib;
	private Integer bookIdBook;
	private String title;
	private String writer;
	private String publisher;
	private String pubDate;
	private String pubDateYear;
	private String price;
	private String isbn10;
	private String isbn13;
	private String category;
	private String bookShelf;
	private String description;
	private String regDate;
	private String editDate;
	private String localIdBarcode;
	private int sortingIndex;
	private int purchasedOrDonated;
	private int available;
	private String additionalCode;
	private String classificationCode;
	private String authorCode;
	private String volumeCode;
	private int copyCode;
	private int barcodeInitCount;
	private int newBarcodeForDupCheck;
	private String tag;
	private String rfId;
	private String bookOrNot;
	private int page;
	private String bookSize;
	private String imageLink;
	
	private int idCountry;
	private String nameCountry;
	
	private int classCodeHead;
	private String classCodeColor;
	private String classCodeSection;
	
	//도서 대출 여부를 확인하기 위한 변수
	//model에만 존재
	private String brwStatus;
	
	//바코드 수정을 위해 변경바코드 임시빈즈
	private String modiBarcode;
	
	// 분류기호별 정리표를 위한 변수
	private String classCodeRange;
	private int count;
	
	
	public String getClassCodeRange() {
		return classCodeRange;
	}
	public void setClassCodeRange(String classCodeRange) {
		this.classCodeRange = classCodeRange;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getModiBarcode() {
		return modiBarcode;
	}
	public void setModiBarcode(String modiBarcode) {
		this.modiBarcode = modiBarcode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLibraryIdLib() {
		return libraryIdLib;
	}
	public void setLibraryIdLib(int libraryIdLib) {
		this.libraryIdLib = libraryIdLib;
	}
	public Integer getBookIdBook() {
		return bookIdBook;
	}
	public void setBookIdBook(Integer bookIdBook) {
		this.bookIdBook = bookIdBook;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getPubDateYear() {
		return pubDateYear;
	}
	public void setPubDateYear(String pubDateYear) {
		this.pubDateYear = pubDateYear;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getIsbn10() {
		return isbn10;
	}
	public void setIsbn10(String isbn10) {
		this.isbn10 = isbn10;
	}
	public String getIsbn13() {
		return isbn13;
	}
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBookShelf() {
		return bookShelf;
	}
	public void setBookShelf(String bookShelf) {
		this.bookShelf = bookShelf;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getLocalIdBarcode() {
		return localIdBarcode;
	}
	public void setLocalIdBarcode(String localIdBarcode) {
		this.localIdBarcode = localIdBarcode;
	}
	public int getSortingIndex() {
		return sortingIndex;
	}
	public void setSortingIndex(int sortingIndex) {
		this.sortingIndex = sortingIndex;
	}
	public int getPurchasedOrDonated() {
		return purchasedOrDonated;
	}
	public void setPurchasedOrDonated(int purchasedOrDonated) {
		this.purchasedOrDonated = purchasedOrDonated;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public String getAdditionalCode() {
		return additionalCode;
	}
	public void setAdditionalCode(String additionalCode) {
		this.additionalCode = additionalCode;
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
	public int getCopyCode() {
		return copyCode;
	}
	public void setCopyCode(int copyCode) {
		this.copyCode = copyCode;
	}
	public int getBarcodeInitCount() {
		return barcodeInitCount;
	}
	public void setBarcodeInitCount(int barcodeInitCount) {
		this.barcodeInitCount = barcodeInitCount;
	}
	public int getNewBarcodeForDupCheck() {
		return newBarcodeForDupCheck;
	}
	public void setNewBarcodeForDupCheck(int newBarcodeForDupCheck) {
		this.newBarcodeForDupCheck = newBarcodeForDupCheck;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getRfId() {
		return rfId;
	}
	public void setRfId(String rfId) {
		this.rfId = rfId;
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
	public String getBookSize() {
		return bookSize;
	}
	public void setBookSize(String bookSize) {
		this.bookSize = bookSize;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
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
	public String getClassCodeSection() {
		return classCodeSection;
	}
	public void setClassCodeSection(String classCodeSection) {
		this.classCodeSection = classCodeSection;
	}
	public String getBrwStatus() {
		return brwStatus;
	}
	public void setBrwStatus(String brwStatus) {
		this.brwStatus = brwStatus;
	}
	
	
}
