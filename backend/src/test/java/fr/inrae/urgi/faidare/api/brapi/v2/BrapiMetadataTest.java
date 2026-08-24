package fr.inrae.urgi.faidare.api.brapi.v2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

class BrapiMetadataTest {

    @Test
    void setPagination_should_not_create_an_extra_page_when_results_fill_exactly_one_page() {
        Pageable pageable = mock(Pageable.class);
        when(pageable.getPageNumber()).thenReturn(0);
        when(pageable.getPageSize()).thenReturn(10);

        BrapiMetadata metadata = new BrapiMetadata();
        BrapiResponse<Object> response = mock(BrapiResponse.class);
        when(response.getMetadata()).thenReturn(metadata);

        BrapiMetadata.setPagination(20, pageable, response);

        assertThat(metadata.getPagination().getCurrentPage()).isEqualTo(0);
        assertThat(metadata.getPagination().getPageSize()).isEqualTo(10);
        assertThat(metadata.getPagination().getTotalCount()).isEqualTo(20);
        assertThat(metadata.getPagination().getTotalPages()).isEqualTo(2);
    }

    @Test
    void setPagination_should_keep_the_total_pages_for_a_partial_last_page() {
        Pageable pageable = mock(Pageable.class);
        when(pageable.getPageNumber()).thenReturn(2);
        when(pageable.getPageSize()).thenReturn(10);

        BrapiMetadata metadata = new BrapiMetadata();
        BrapiResponse<Object> response = mock(BrapiResponse.class);
        when(response.getMetadata()).thenReturn(metadata);

        BrapiMetadata.setPagination(25, pageable, response);

        assertThat(metadata.getPagination().getCurrentPage()).isEqualTo(2);
        assertThat(metadata.getPagination().getPageSize()).isEqualTo(10);
        assertThat(metadata.getPagination().getTotalCount()).isEqualTo(25);
        assertThat(metadata.getPagination().getTotalPages()).isEqualTo(3);
    }

    @Test
    void setPagination_should_work_with_thousands_of_pages() {
        Pageable pageable = mock(Pageable.class);
        when(pageable.getPageNumber()).thenReturn(3);
        when(pageable.getPageSize()).thenReturn(1000);

        BrapiMetadata metadata = new BrapiMetadata();
        BrapiResponse<Object> response = mock(BrapiResponse.class);
        when(response.getMetadata()).thenReturn(metadata);

        BrapiMetadata.setPagination(3980, pageable, response);

        assertThat(metadata.getPagination().getCurrentPage()).isEqualTo(3);
        assertThat(metadata.getPagination().getPageSize()).isEqualTo(1000);
        assertThat(metadata.getPagination().getTotalCount()).isEqualTo(3980);
        assertThat(metadata.getPagination().getTotalPages()).isEqualTo(4);
    }
}
