/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
