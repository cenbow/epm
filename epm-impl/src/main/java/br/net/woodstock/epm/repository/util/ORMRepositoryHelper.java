package br.net.woodstock.epm.repository.util;

import java.util.Map;

import br.net.woodstock.rockframework.core.utils.Conditions;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMFilter;

public abstract class ORMRepositoryHelper {

	private ORMRepositoryHelper() {
		//
	}

	public static ORMFilter toORMFilter(final String sql) {
		return new ORMFilter(sql);
	}

	public static ORMFilter toORMFilter(final String sql, final Map<String, Object> parameters) {
		return ORMRepositoryHelper.toORMFilter(sql, parameters, null);
	}

	public static ORMFilter toORMFilter(final String sql, final Map<String, Object> parameters, final Map<String, Object> options) {
		ORMFilter filter = new ORMFilter(sql);
		filter.setParameters(parameters);
		filter.setOptions(options);
		return filter;
	}

	public static ORMFilter toORMFilter(final String sql, final String countSQL, final Page page, final Map<String, Object> parameters) {
		ORMFilter filter = new ORMFilter(sql);
		filter.setCountQuery(countSQL);
		filter.setPage(page);
		filter.setParameters(parameters);
		return filter;
	}

	public static ORMFilter toORMFilter(final String sql, final String countSQL, final Page page, final Map<String, Object> parameters, final Map<String, Object> options) {
		ORMFilter filter = new ORMFilter(sql);
		filter.setCountQuery(countSQL);
		filter.setPage(page);
		filter.setParameters(parameters);
		filter.setOptions(options);
		return filter;
	}

	// JPA
	public static String getLikeValue(final String value, final boolean nullIfEmpty) {
		if (Conditions.isEmpty(value)) {
			if (nullIfEmpty) {
				return null;
			}
			return "%";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("%");
		builder.append(value);
		builder.append("%");
		return builder.toString();
	}

	public static String getEqualsValue(final String value, final boolean nullIfEmpty) {
		if (Conditions.isEmpty(value)) {
			if (nullIfEmpty) {
				return null;
			}
			return "";
		}
		return value;
	}

	public static <T> T getValue(final T value, final T defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}
