package fr.inrae.urgi.faidare.api.brapi.v2;

import static fr.inrae.urgi.faidare.api.brapi.v2.BrapiMetadata.setPagination;

import org.springframework.data.domain.Pageable;

public class BrapiSingleResponse<T> implements BrapiResponse<T>{


    private BrapiMetadata metadata;
    private T result;

    public BrapiSingleResponse() {
        this.metadata = new BrapiMetadata();
        this.result = null;
    }

    public static <T> BrapiSingleResponse<T> brapiResponseOf(T hit, Pageable pageable) throws Exception {
        BrapiSingleResponse<T> brapiResponse = new BrapiSingleResponse<T>();

        brapiResponse.setResult(hit);


        setPagination(hit == null ? 0 : 1, pageable, brapiResponse);
        return brapiResponse;
    }

    @Override
    public BrapiMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BrapiMetadata metadata) {
        this.metadata = metadata;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
