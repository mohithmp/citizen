package filters;

import javax.inject.Inject;

import play.filters.cors.CORSFilter;
import play.http.DefaultHttpFilters;

public class Filters extends DefaultHttpFilters {
	@Inject
	public Filters(LoggingFilter logging, CORSFilter corsFilter) {
		super(logging, corsFilter);
	}
}