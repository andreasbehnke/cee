package org.cee.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class EntityKeyUtils {

	public static List<EntityKey> fromList(List<String> keys) {
		List<EntityKey> result = new ArrayList<>();
		for (String key : keys) {
			result.add(EntityKey.get(key));
		}
		return result;
	}

	public static List<EntityKey> fromCommaSeparatedList(String commaSeparatedList) {
		return fromList(Arrays.asList(StringUtils.split(commaSeparatedList,',')));
	}

}
