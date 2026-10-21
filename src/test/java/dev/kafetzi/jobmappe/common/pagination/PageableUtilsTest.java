package dev.kafetzi.jobmappe.common.pagination;

import dev.kafetzi.jobmappe.exception.InvalidSortPropertyException;
import dev.kafetzi.jobmappe.tracking.company.Company;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageableUtilsTest {

    @Test
    void withStableSort_sortWithoutId_appendsIdAscending() {
        Pageable input = PageRequest.of(0, 20, Sort.by("name"));
        Pageable result = PageableUtils.withStableSort(input, Company.class);

        assertThat(result.getSort())
                .containsExactly(Sort.Order.asc("name"), Sort.Order.asc("id"));
    }

    @Test
    void withStableSort_unsorted_sortsByIdAscending() {
        Pageable input = PageRequest.of(0, 20, Sort.unsorted());
        Pageable result = PageableUtils.withStableSort(input, Company.class);

        assertThat(result.getSort())
                .containsExactly(Sort.Order.asc("id"));
    }

    @Test
    void withStableSort_idAlreadyDescending_keepsDescending() {
        Pageable input = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));
        Pageable result = PageableUtils.withStableSort(input, Company.class);

        assertThat(result.getSort())
                .containsExactly(Sort.Order.desc("id"));
    }

    @Test
    void withStableSort_unknownSortProperty_throwsInvalidSortProperty() {
        Pageable input = PageRequest.of(0, 20, Sort.by("age"));

        assertThatThrownBy(() -> PageableUtils.withStableSort(input, Company.class))
                .isInstanceOf(InvalidSortPropertyException.class)
                .hasMessageContaining("Invalid sort property: age");
    }

    @Test
    void withStableSort_anySort_keepsPageNumberAndSize() {
        Pageable input = PageRequest.of(3, 8, Sort.by("name"));
        Pageable result = PageableUtils.withStableSort(input, Company.class);

        assertThat(result.getPageNumber()).isEqualTo(3);
        assertThat(result.getPageSize()).isEqualTo(8);
    }
}