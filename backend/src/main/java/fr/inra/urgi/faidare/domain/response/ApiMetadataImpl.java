package fr.inra.urgi.faidare.domain.response;

import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiPagination;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gornut
 */
class ApiMetadataImpl implements BrapiMetadata {

    private final BrapiPagination pagination;
    private final List<BrapiStatus> status;
    private final List<String> datafiles;

    ApiMetadataImpl(BrapiPagination pagination, List<BrapiStatus> status, List<String> datafiles) {
        if (pagination == null) {
            this.pagination = new BrapiPagination() {

                @Override
                public long getTotalPages() {
                    return 0;
                }

                @Override
                public long getTotalCount() {
                    return 0;
                }

                @Override
                public long getPageSize() {
                    return 0;
                }

                @Override
                public long getCurrentPage() {
                    return 0;
                }
            };
        } else {
            this.pagination = pagination;
        }

        if (status == null) {
            this.status = new ArrayList<>();
        } else {
            this.status = status;
        }
        this.datafiles = datafiles;

    }

    @Override
    public BrapiPagination getPagination() {
        return pagination;
    }

    @Override
    public List<BrapiStatus> getStatus() {
        return status;
    }

    @Override
    public List<String> getDatafiles() {
        return datafiles;
    }

}
