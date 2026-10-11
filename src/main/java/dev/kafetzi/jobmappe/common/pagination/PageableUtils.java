package dev.kafetzi.jobmappe.common.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtils {
    private PageableUtils() {}

    public static Pageable withStableSort(Pageable pageable) {
        Sort sort = pageable.getSort();
        Sort.Order order = sort.getOrderFor("id");

        if (order == null) {
            sort = sort.and(Sort.by("id").ascending());
        }

        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );
    }
}
