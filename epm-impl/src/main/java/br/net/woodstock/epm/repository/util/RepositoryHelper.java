package br.net.woodstock.epm.repository.util;

import java.util.Map;

import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryMetadata;
import br.net.woodstock.rockframework.persistence.orm.impl.QueryMetadataImpl;
import br.net.woodstock.rockframework.utils.ConditionUtils;

public abstract class RepositoryHelper {

	private RepositoryHelper() {
		//
	}

	public static QueryMetadata toQueryMetadata(final String sql) {
		return RepositoryHelper.toQueryMetadata(sql, null, null);
	}

	public static QueryMetadata toQueryMetadata(final String sql, final Map<String, Object> parameters) {
		return RepositoryHelper.toQueryMetadata(sql, parameters, null);
	}

	public static QueryMetadata toQueryMetadata(final String sql, final Map<String, Object> parameters, final Map<String, Object> options) {
		return new QueryMetadataImpl(sql, parameters, options);
	}

	public static QueryMetadata toQueryMetadata(final String sql, final String countSQL, final Page page, final Map<String, Object> parameters) {
		return new QueryMetadataImpl(sql, countSQL, page, parameters, null);
	}

	public static QueryMetadata toQueryMetadata(final String sql, final String countSQL, final Page page, final Map<String, Object> parameters, final Map<String, Object> options) {
		return new QueryMetadataImpl(sql, countSQL, page, parameters, options);
	}

	// JPA

	public static String getLikeValue(final String value, final boolean nullIfEmpty) {
		if (ConditionUtils.isEmpty(value)) {
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
		if (ConditionUtils.isEmpty(value)) {
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
