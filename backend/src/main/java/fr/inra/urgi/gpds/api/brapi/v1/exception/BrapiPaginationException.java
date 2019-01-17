package fr.inra.urgi.gpds.api.brapi.v1.exception;

import fr.inra.urgi.gpds.api.BadRequestException;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiPagination;

/**
 * @author gcornut
 *
 *
 */
public class BrapiPaginationException extends BadRequestException implements BrapiException {

	private BrapiPagination pagination;

	public BrapiPaginationException(String message, BrapiPagination pagination) {
		super(message);
		this.pagination = pagination;
	}

	public BrapiPagination getPagination() {
		return pagination;
	}
}
