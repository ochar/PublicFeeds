/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
