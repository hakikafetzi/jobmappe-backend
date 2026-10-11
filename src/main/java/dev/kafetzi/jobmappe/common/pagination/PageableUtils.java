package dev.kafetzi.jobmappe.common.pagination;

import dev.kafetzi.jobmappe.exception.InvalidSortPropertyException;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtils {
    private PageableUtils() {}

    public static Pageable withStableSort(Pageable pageable, Class<?> entityClass) {
        Sort sort = pageable.getSort();
        Sort.Order order = sort.getOrderFor("id");

        for (Sort.Order o : sort) {
            String property = o.getProperty();

            try {
                PropertyPath.from(property, entityClass);
            } catch (PropertyReferenceException exception) {
                throw new InvalidSortPropertyException(
                        "Invalid sort property: " + property
                );
            }
        }

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
