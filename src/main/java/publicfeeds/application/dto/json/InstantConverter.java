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
import java.time.Instant;

/**
 * Converter for Jackson deserializer. 
 * Converts String with ISO_INSTANT format to Instant.
 *
 * @author io
 */
public class InstantConverter extends StdConverter<String, Instant> {

	@Override
	public Instant convert(String value) {
		if (value == null) {
			return null;
		}
		return Instant.parse(value);
	}
	
}
