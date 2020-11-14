/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.interfaces.support;

import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author io
 */
public class Page<T> implements Iterable<T> {
	
	private final long totalSize;
	
	private final int pageNumber; // page number is one based
	private final int pageSize;
	
	// underlying collection
	private final Iterable<T> backingCollection;
	
	
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
	
	
	public long getTotalSize() {
		return totalSize;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public int getTotalPage() {
		return calcTotalPage(pageSize, totalSize);
	}
	
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
	
	// position is zero based
	public int getStartPosition() {
		return calcStartPosition(pageSize, pageNumber, totalSize);
	}
	
	// position is zero based
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
    
    public int getEndPosition() {
		if (totalSize == 0) 
			return 0;
		return Math.min(getStartPosition() + pageSize, (int) totalSize - 1); // harus positive
	}

	public Iterable<T> getCollection() {
		return backingCollection;
	}
	

	@Override
	public Iterator<T> iterator() {
		return backingCollection.iterator();
	}
	
	

	private final int minPageNumber = 1;
	
	public boolean hasPrev() {
		return pageNumber > getFirstPageNumber();
	}

	public boolean hasNext() {
		return pageNumber < getLastPageNumber();
	}
	
	public boolean isFirst() {
		return !hasPrev();
	}
	
	public boolean isLast() {
		return !hasNext();
	}
	
	public int getFirstPageNumber() {
		return minPageNumber;
	}
	
	public int getLastPageNumber() {
		return getTotalPage() - 1 + minPageNumber;
	}
	
	public int getPrevPageNumber() {
		return isFirst() ? pageNumber : pageNumber - 1;
	}
	
	public int getCurrentPageNumber() {
		return pageNumber;
	}
	
	public int getNextPageNumber() {
		return isLast() ? pageNumber : pageNumber + 1;
	}
	
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
