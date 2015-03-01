package org.cee.store;

/*
 * #%L
 * Content Extraction Engine - News Store API
 * %%
 * Copyright (C) 2013 - 2015 Andreas Behnke
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
