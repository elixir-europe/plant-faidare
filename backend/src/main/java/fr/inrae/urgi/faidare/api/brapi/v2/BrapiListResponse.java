package fr.inrae.urgi.faidare.api.brapi.v2;

import static fr.inrae.urgi.faidare.api.brapi.v2.BrapiMetadata.setPagination;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;

public class BrapiListResponse<T> implements BrapiResponse {
    Result result;
    private BrapiMetadata metadata;

    public BrapiListResponse(BrapiMetadata brapiMetadata, List<T> result) {
        this.metadata = brapiMetadata;
        this.result = new Result();
        this.result.data = result;
    }


    public BrapiListResponse() {
        this.metadata = new BrapiMetadata();
        this.result = new Result();
    }

    public static <T> BrapiListResponse<T> brapiResponseForPageOf(SearchHits<T> searchHits, Pageable pageable){
        BrapiListResponse<T> brapiResponse = new BrapiListResponse<T>();
        List<T> vos = (List<T>) SearchHitSupport.unwrapSearchHits(searchHits) ;
        brapiResponse.getResult().setData(vos);
        setPagination(searchHits.getTotalHits(), pageable, brapiResponse);
        return brapiResponse;
    }

    public static <T> BrapiListResponse<T> brapiResponseForPageOf(Page<T> searchPage){
        BrapiListResponse<T> brapiResponse = new BrapiListResponse<T>();
        Page<T> vos = (Page<T>) SearchHitSupport.unwrapSearchHits(searchPage) ;
        brapiResponse.getResult().setData(vos.getContent());
        setPagination(searchPage.getTotalElements(), searchPage.getPageable(), brapiResponse);
        return brapiResponse;
    }

    public static <T> BrapiListResponse<T> brapiResponseForPageOf(List<T> listOfVos){
        BrapiListResponse<T> brapiResponse = new BrapiListResponse<T>();
        brapiResponse.getResult().setData(listOfVos);
        Pageable pageable = Pageable.ofSize(listOfVos.size()).withPage(0);
        setPagination(pageable.getPageSize(), pageable, brapiResponse);
        return brapiResponse;
    }

    public static <T> BrapiListResponse<T> brapiResponseForPageOf(List<T> listOfVos, Pageable pageable, Integer totalElements){
        if (pageable == null){
            return brapiResponseForPageOf(listOfVos);
        }
        BrapiListResponse<T> brapiResponse = new BrapiListResponse<T>();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        int fromIndex = Math.min(page * size, listOfVos.size());
        int toIndex = Math.min(fromIndex + size, listOfVos.size());
        List<T> pagedList = listOfVos.subList(fromIndex, toIndex);
        brapiResponse.getResult().setData(pagedList);
        setPagination(totalElements, pageable, brapiResponse);
        return brapiResponse;
    }




    public BrapiMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BrapiMetadata metadata) {
        this.metadata = metadata;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{
        public Result(){
            this.data = List.of();
        }
        private List<T> data;

        public List<T> getData() {
            return data;
        }

        public void setData(List<T> data) {
            this.data = data;
        }
    }

}
