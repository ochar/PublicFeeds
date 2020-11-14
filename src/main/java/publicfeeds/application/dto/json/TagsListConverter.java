/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package publicfeeds.application.dto.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author io
 */
public class TagsListConverter extends StdConverter<String, List<String>> {

	@Override
	public List<String> convert(String value) {
		String tags = value;
		
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
