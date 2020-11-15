/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.support;

import java.util.Iterator;
import java.util.Objects;

/**
 * Wrapper object representing a page in a collection of objects.
 * 
 * @param <T> type of object contained in the page
 * @author io
 */
public class Page<T> implements Iterable<T> {
	
	private final long totalSize;
	
	/**
	 * Currently selected page number. Page number is one based
	 */
	private final int pageNumber;
	private final int pageSize;
	
	/**
	 * Underlying collection of this page
	 */
	private final Iterable<T> backingCollection;
	
	
	/**
	 * Creates new page object given an underlying collection.
	 * 
	 * @param pageSize number of items in a page, must be more than 0
	 * @param pageNumber currently selected page number, must be more than 0
	 * @param totalSize total item count of the underlying collection, must not 
	 * be negative
	 * @param backingCollection underlying collection of the page, must not be 
	 * null
	 * @throws IllegalArgumentException if selected page number is more than 
	 * available
	 */
	public Page(int pageSize,
				int pageNumber, 
				long totalSize, 
				Iterable<T> backingCollection) {
		
		if (pageSize < 1)
			throw new IllegalArgumentException("pageSize can not be less than 1");
		if (pageNumber < minPageNumber)
			throw new IllegalArgumentException("pageNumber can not be less than "+minPageNumber);
		
		if (totalSize < 0)
			throw new IllegalArgumentException("totalSize can not be less than 0");
	
		if (totalSize == 0) {
			this.pageSize = 0;
			this.pageNumber = 0;

			this.totalSize = 0;
			
			Objects.requireNonNull(backingCollection, "backingCollection must not be null");
			this.backingCollection = backingCollection;
			return;
		}
			
		
		if (pageNumber > calcTotalPage(pageSize, totalSize)) {
			throw new IllegalArgumentException("pageNumber is more than available number of pages");
		}
		
		Objects.requireNonNull(backingCollection, "backingCollection must not be null");
		
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;
		
		this.totalSize = totalSize;
		
		this.backingCollection = backingCollection;
	}
	
	
	/**
	 * Returns total item count of the underlying collection.
	 * 
	 * @return total item count of the underlying collection
	 */
	public long getTotalSize() {
		return totalSize;
	}
	
	/**
	 * Return currently selected page number.
	 * 
	 * @return currently selected page number
	 */
	public int getPageNumber() {
		return pageNumber;
	}
	
	/**
	 * Returns number of items in a page.
	 * 
	 * @return number of items in a page
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * Returns total available page number.
	 * 
	 * @return total available page number
	 */
	public int getTotalPage() {
		return calcTotalPage(pageSize, totalSize);
	}
	
	/**
	 * Returns total available page number given page size and collection size.
	 *
	 * @param pageSize number of items in a page, must be more than 0
	 * @param totalSize total item count of the underlying collection, must not 
	 * be negative
	 * @return total available page number
	 */
	public static int calcTotalPage(int pageSize, long totalSize) {
		if (pageSize < 1) {
			throw new IllegalArgumentException("pageSize can not be less than 1");
		}
		if (totalSize < 0) {
			throw new IllegalArgumentException("totalSize can not be less than 0");
		}
		if (totalSize == 0) {
			return 0;
		}
		return (int) ((totalSize + (long) pageSize - 1) /  (long) pageSize); // must be positive
	}
	
	/**
	 * Returns the first item position in the current page.
	 * Item position is zero based.
	 * 
	 * @return the first item position in the current page, zero based
	 */
	public int getStartPosition() {
		return calcStartPosition(pageSize, pageNumber, totalSize);
	}
	
	/**
	 * Returns the first item position in the given page and collection size.
	 * Item position is zero based.
	 *
	 * @param pageSize number of items in a page, must be more than 0
	 * @param pageNumber currently selected page number, must be more than 0
	 * @param totalSize total item count of the underlying collection, must not 
	 * be negative
	 * @return the first item position in the page, zero based
	 * @throws IllegalArgumentException if selected page number is more than 
	 * available
	 */
	public static int calcStartPosition(int pageSize, int pageNumber, long totalSize) {
		if (pageSize < 1) {
			throw new IllegalArgumentException("pageSize can not be less than 1");
		}
		if (pageNumber < 1) {
			throw new IllegalArgumentException("pageNumber can not be less than 1");
		}
		if (totalSize < 0) {
			throw new IllegalArgumentException("totalSize can not be less than 0");
		}
		if (totalSize == 0) {
			return 0;
		}
		if (pageNumber > calcTotalPage(pageSize, totalSize)) {
			throw new IllegalArgumentException("pageNumber is more than available number of pages");
		}
		return (pageNumber - 1) * pageSize;
	}
    
	/**
	 * Returns the last item position in the current page.
	 * Item position is zero based.
	 * 
	 * @return the last item position in the current page, zero based
	 */
    public int getEndPosition() {
		if (totalSize == 0) 
			return 0;
		return Math.min(getStartPosition() + pageSize, (int) totalSize - 1); // harus positive
	}
	
	/**
	 * Returns the collection underlying this page.
	 * 
	 * @return the collection underlying this page
	 */
	public Iterable<T> getCollection() {
		return backingCollection;
	}
	

	@Override
	public Iterator<T> iterator() {
		return backingCollection.iterator();
	}
	
	
	/**
	 * Page number is one based.
	 */
	private final int minPageNumber = 1;
	
	/**
	 * Checks if there is a previous page.
	 * 
	 * @return {@code true} if there is a previous page, {@code false} otherwise
	 */
	public boolean hasPrev() {
		return pageNumber > getFirstPageNumber();
	}
	
	/**
	 * Checks if there is a next page.
	 * 
	 * @return {@code true} if there is a next page, {@code false} otherwise
	 */
	public boolean hasNext() {
		return pageNumber < getLastPageNumber();
	}
	
	/**
	 * Checks if current page is the first page.
	 * 
	 * @return {@code true} if current page is the first, {@code false} otherwise
	 */
	public boolean isFirst() {
		return !hasPrev();
	}
	
	/**
	 * Checks if current page is the last page.
	 * 
	 * @return {@code true} if current page is the last, {@code false} otherwise
	 */
	public boolean isLast() {
		return !hasNext();
	}
	
	/**
	 * Returns the first page number. Page number is one based.
	 * 
	 * @return the first page number
	 */
	public int getFirstPageNumber() {
		return minPageNumber;
	}
	
	/**
	 * Returns the last page number.
	 * 
	 * @return the last page number
	 */
	public int getLastPageNumber() {
		return getTotalPage() - 1 + minPageNumber;
	}
	
	/**
	 * Returns the previous page number.
	 * 
	 * @return the previous page number
	 */
	public int getPrevPageNumber() {
		return isFirst() ? pageNumber : pageNumber - 1;
	}
	
	/**
	 * Returns the current page number.
	 * 
	 * @return the current page number
	 */
	public int getCurrentPageNumber() {
		return pageNumber;
	}
	
	/**
	 * Returns the next page number.
	 * 
	 * @return the next page number
	 */
	public int getNextPageNumber() {
		return isLast() ? pageNumber : pageNumber + 1;
	}
	
	/**
	 * Returns an {@code array} containing page numbers surrounding current page.
	 * 
	 * @param numberOfPages number of pages to be returned
	 * @return page numbers surrounding current page
	 */
	public int[] getSurroundingPages(int numberOfPages) {
		
		int quota = numberOfPages;
		int startPage = getFirstPageNumber();
		int endPage = getFirstPageNumber();
		
		int[] pages;
		
		if (getTotalPage() <= quota) {
			quota = getTotalPage();
			startPage = getFirstPageNumber();
			endPage = getLastPageNumber();
			
		} else {
			
			int quotaLeft = quota;
			
			int currentPage = this.pageNumber;
			startPage = currentPage;
			endPage = currentPage;
			
			// if quota is even, reduce the back step
			int compensation = (quota % 2) == 0 ? 1 : 0;
			
			int step = (quota / 2) - compensation;
			
			// cek ke belakang, insert ke quota
			int backStep = ((currentPage - step) >= getFirstPageNumber()) ?
								step : (currentPage - getFirstPageNumber());
			
			quotaLeft -= backStep;
			startPage -= backStep;

			// current page
			quotaLeft--;
			
			step += compensation;
			
			// cek ke depan, insert ke quota
			int fwdStep = ((currentPage + step) <= getLastPageNumber()) ? 
								step : (getLastPageNumber() - currentPage);
			
			quotaLeft -= fwdStep;
			endPage += fwdStep;

			// if quota masih sisa
			// cek ke depan lagi
			if (quotaLeft > 0) {
				step = quotaLeft;
				
				fwdStep = ((endPage + step) <= getLastPageNumber()) ? 
								step : (getLastPageNumber() - endPage);
			
				quotaLeft -= fwdStep;
				endPage += fwdStep;
			}

			// if quota masih sisa
			// cek ke belakang lagi
			if (quotaLeft > 0) {
				step = quotaLeft;
				
				backStep = ((startPage - step) >= getFirstPageNumber()) ?
								step : (startPage - getFirstPageNumber());
			
				quotaLeft -= backStep;
				startPage -= backStep;
			}
			
		}
		
		pages = new int[quota];
		
		for (int i = 0; i < quota; i++) {
			pages[i] = i + startPage;
		}
		
		return pages;
	}
	

	@Override
	public String toString() {
		return "Page{" + 
				"pageNumber=" + pageNumber + 
				", pageSize=" + pageSize + 
				", totalSize=" + totalSize + 
				", backingCollection=" + backingCollection + '}';
	}
	
}
