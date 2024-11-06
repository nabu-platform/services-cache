/*
* Copyright (C) 2016 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.libs.services.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Map;

import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.types.ComplexContentWrapperFactory;
import be.nabu.libs.types.binding.api.Window;
import be.nabu.libs.types.binding.json.JSONBinding;
import be.nabu.libs.types.map.MapContent;
import be.nabu.libs.types.map.MapType;
import be.nabu.libs.types.map.MapTypeGenerator;

@SuppressWarnings("rawtypes")
public class MapSerializer implements DataSerializer<Map> {

	private JSONBinding binding;
	
	public MapSerializer() {
		this.binding = new JSONBinding(new MapType(), Charset.forName("UTF-8"));
		binding.setAddDynamicElementDefinitions(true);
		binding.setAllowDynamicElements(true);
		binding.setComplexTypeGenerator(new MapTypeGenerator());
		binding.setParseNumbers(true);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void serialize(Map object, OutputStream output) throws IOException {
		binding.marshal(output, ComplexContentWrapperFactory.getInstance().getWrapper().wrap(object));
	}

	@Override
	public Map deserialize(InputStream input) throws IOException {
		try {
			MapContent unmarshal = (MapContent) binding.unmarshal(input, new Window[0]);
			return unmarshal.toMap();
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<Map> getDataClass() {
		return Map.class;
	}

}
