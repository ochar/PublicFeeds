/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest.support;

import java.util.List;

/**
 *
 * @author io
 */
public class PageResponse<T> {
	
	private List<T> content;
	
	
	private int pageSize;
	
	private int currentPageNumber;
	
	private int totalPage;
	
	
	private long totalCount;
	
	
	private String currentUrl;
	
	private String prevUrl;
	
	private String nextUrl;
	

	public PageResponse() {
	}

	public PageResponse(List<T> content, 
			int pageSize, int currentPageNumber, int totalPage, 
			long totalCount, 
			String currentUrl, String prevUrl, String nextUrl) {
		this.content = content;
		this.pageSize = pageSize;
		this.currentPageNumber = currentPageNumber;
		this.totalPage = totalPage;
		this.totalCount = totalCount;
		this.currentUrl = currentUrl;
		this.prevUrl = prevUrl;
		this.nextUrl = nextUrl;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	public String getPrevUrl() {
		return prevUrl;
	}

	public void setPrevUrl(String prevUrl) {
		this.prevUrl = prevUrl;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	@Override
	public String toString() {
		return "PageResponse{" 
				+ "content=" + content 
				+ ", pageSize=" + pageSize 
				+ ", currentPageNumber=" + currentPageNumber 
				+ ", totalPage=" + totalPage 
				+ ", totalCount=" + totalCount 
				+ ", currentUrl=" + currentUrl 
				+ ", prevUrl=" + prevUrl 
				+ ", nextUrl=" + nextUrl + '}';
	}
	
}
