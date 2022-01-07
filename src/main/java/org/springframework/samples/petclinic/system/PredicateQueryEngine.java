package org.springframework.samples.petclinic.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.keyvalue.core.QueryEngine;
import org.springframework.data.map.MapKeyValueAdapter;

public class PredicateQueryEngine<T> extends QueryEngine<MapKeyValueAdapter, TypedPredicate<T>, Comparator<T>> {

	@SuppressWarnings("unchecked")
	public PredicateQueryEngine() {
		super(query -> (TypedPredicate<T>) query.getCriteria(), null);
	}

	@Override
	public Collection<?> execute(TypedPredicate<T> criteria, Comparator<T> sort, long offset, int rows,
			String keyspace) {
		List<T> result = new ArrayList<>();
		AtomicLong count = new AtomicLong();
		getRequiredAdapter().getAllOf(keyspace, criteria == null ? null : criteria.getType()).iterator()
				.forEachRemaining(value -> {
					if ((offset < 0 || count.get() >= offset && count.get() < offset + rows)
							&& (criteria == null || criteria.applies(value))) {
						result.add(value);
					}
					count.incrementAndGet();
				});
		return result;
	}

	@Override
	public long count(TypedPredicate<T> criteria, String keyspace) {
		return execute(criteria, null, 0, Integer.MAX_VALUE, keyspace).size();
	}

}
