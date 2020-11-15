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
package publicfeeds.interfaces.json.dto;

import com.fasterxml.jackson.databind.util.StdConverter;
import publicfeeds.domain.Item;

/**
 * Converter for Jackson serializer.
 * Converts an Item object to string of its Id.
 * For class ItemLike and ItemComment.
 *
 * @author io
 */
public class ItemToIdConverter extends StdConverter<Item, String> {

	@Override
	public String convert(Item value) {
		if (value == null) {
			return "";
		} else {
			return value.getId();
		}
	}
	
}
