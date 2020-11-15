/*
 * Copyright (C) 2020 io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package publicfeeds.application.dto.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Converter for Jackson deserializer.
 * Converts a String with spaces to a List of Strings.
 * For Tags List in PublicFeedItemJson.
 *
 * @author io
 */
public class TagsListConverter extends StdConverter<String, List<String>> {

	@Override
	public List<String> convert(String value) {
		String tags = value;
		
		if (tags == null) {
			return null;
		}
		
		if (tags.length() < 1) {
			return new ArrayList<>();
		}

		String[] splits = tags.split(" ");
		if (splits.length > 0) {
			List<String> tagList = new ArrayList<>();
			tagList.addAll(Arrays.asList(splits));
			return tagList;

		} else {
			return new ArrayList<>();
		}
	}
	
}
