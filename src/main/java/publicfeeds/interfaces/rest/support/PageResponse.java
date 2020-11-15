/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.rest.support;

import java.util.List;

/**
 * Wrapper of a list of objects meant to be a paged response of rest controller.
 * This wrapper represents a page in a larger set of objects. Along with 
 * actual list of objects it also contains information of page size, number, 
 * total available pages, and url to previous and next page.
 *
 * @param <T> type of the contained in this wrapper response
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
	
	/**
	 * Creates new page response wrapping a list of objects.
	 * 
	 * @param content list of objects contained in current page
	 * @param pageSize total items in one page
	 * @param currentPageNumber number of the current page
	 * @param totalPage total available in the whole collection
	 * @param totalCount total items in the collection
	 * @param currentUrl url to current page
	 * @param prevUrl url to previous page
	 * @param nextUrl url to next page
	 */
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
	
	/**
	 * Returns object contents of this page.
	 *
	 * @return object contents of this page.
	 */
	public List<T> getContent() {
		return content;
	}
	
	/**
	 * Sets the object contents of this page.
	 *
	 * @param content the contents to be set.
	 */
	public void setContent(List<T> content) {
		this.content = content;
	}
	
	/**
	 * Returns size of this page.
	 *
	 * @return size of this page.
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * Sets the size of this page.
	 *
	 * @param pageSize the size to set to.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * Returns current page number of this response.
	 *
	 * @return current page number of this response.
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	
	/**
	 * Sets the current page number of this response.
	 *
	 * @param currentPageNumber the page number to be set.
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}
	
	/**
	 * Returns total page available of the whole collection.
	 *
	 * @return total page available of the whole collection.
	 */
	public int getTotalPage() {
		return totalPage;
	}
	
	/**
	 * Sets the total page available of the whole collection.
	 *
	 * @param totalPage the total page to be set.
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	/**
	 * Returns total item counts of the whole collection.
	 *
	 * @return total item counts of the whole collection.
	 */
	public long getTotalCount() {
		return totalCount;
	}
	
	/**
	 * Sets the total item counts of the whole collection.
	 *
	 * @param totalCount the total counts to be set.
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	/**
	 * Returns url to the current page.
	 *
	 * @return url to the curren page.
	 */
	public String getCurrentUrl() {
		return currentUrl;
	}
	
	/**
	 * Sets the url to the current page.
	 *
	 * @param currentUrl the url to be set.
	 */
	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}
	
	/**
	 * Returns url to the previous page.
	 *
	 * @return url to the previous page.
	 */
	public String getPrevUrl() {
		return prevUrl;
	}
	
	/**
	 * Sets the url to the previous page.
	 *
	 * @param prevUrl the url to be set.
	 */
	public void setPrevUrl(String prevUrl) {
		this.prevUrl = prevUrl;
	}
	
	/**
	 * Returns url to the next page.
	 *
	 * @return url to the next page.
	 */
	public String getNextUrl() {
		return nextUrl;
	}
	
	/**
	 * Sets the url to the next page.
	 *
	 * @param nextUrl the url to be set.
	 */
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
