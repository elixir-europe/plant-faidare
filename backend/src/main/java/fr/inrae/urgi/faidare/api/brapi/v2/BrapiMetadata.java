package fr.inrae.urgi.faidare.api.brapi.v2;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

public class BrapiMetadata {

    private List<Map<String, String>> status;
    private List<Map<String, String>> datafiles;
    private BrapiPagination pagination;

    public BrapiMetadata() {
        this.pagination = new BrapiPagination();
        this.datafiles = new ArrayList<Map<String, String>>();
        this.status = new ArrayList<Map<String, String>>();
    }


    static <T> void setPagination(long totalHits, Pageable pageable, BrapiResponse<T> brapiResponse) {
        brapiResponse.getMetadata().getPagination().setCurrentPage(pageable.getPageNumber());
        brapiResponse.getMetadata().getPagination().setPageSize(pageable.getPageSize());
        brapiResponse.getMetadata().getPagination().setTotalCount(totalHits);
        brapiResponse.getMetadata().getPagination().setTotalPages(totalHits / pageable.getPageSize());
    }
    public List<Map<String, String>> getDatafiles() {
        return datafiles;
    }

    public void setDatafiles(List<Map<String, String>> datafiles) {
        this.datafiles = datafiles;
    }

    public BrapiPagination getPagination() {
        return pagination;
    }

    public void setPagination(BrapiPagination pagination) {
        this.pagination = pagination;
    }

    public List<Map<String, String>> getStatus() {
        return status;
    }

    public void setStatus(List<Map<String, String>> status) {
        this.status = status;
    }


    public class BrapiPagination {
        private long totalCount;
        private long pageSize;
        private long totalPages;
        private long currentPage;

        public long getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(long currentPage) {
            this.currentPage = currentPage;
        }

        public long getPageSize() {
            return pageSize;
        }

        public void setPageSize(long pageSize) {
            this.pageSize = pageSize;
        }

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public long getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(long totalPages) {
            this.totalPages = totalPages;
        }
    }
}
