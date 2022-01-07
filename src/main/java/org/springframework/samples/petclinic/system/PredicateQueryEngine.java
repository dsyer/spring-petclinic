package org.springframework.samples.petclinic.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import java.util.function.Function;import org.springframework.data.keyvalue.core.QueryEngine;
import org.springframework.data.map.MapKeyValueAdapter;

public class PredicateQueryEngine<T> extends QueryEngine<MapKeyValueAdapter, Function<T, Boolean>, Comparator<T>> {

	@SuppressWarnings("unchecked")
	public PredicateQueryEngine() {
		super(query -> (Function<T, Boolean>) query.getCriteria(), null);
	}

	@Override
	public Collection<?> execute(Function<T, Boolean> criteria, Comparator<T> sort, long offset, int rows,
			String keyspace) {
		List<Object> result = new ArrayList<>();
		AtomicLong count = new AtomicLong();
		getRequiredAdapter().getAllOf(keyspace).iterator()
				.forEachRemaining(value -> {
					@SuppressWarnings("unchecked")
					T item = (T) value;
					if ((offset < 0 || count.get() >= offset && count.get() < offset + rows)
							&& (criteria == null || criteria.apply(item))) {
						result.add(value);
					}
					count.incrementAndGet();
				});
		return result;
	}

	@Override
	public long count(Function<T, Boolean> criteria, String keyspace) {
		return execute(criteria, null, 0, Integer.MAX_VALUE, keyspace).size();
	}

}
