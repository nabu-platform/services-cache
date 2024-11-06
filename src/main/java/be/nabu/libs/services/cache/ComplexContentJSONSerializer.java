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

import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.types.api.ComplexContent;
import be.nabu.libs.types.api.ComplexType;
import be.nabu.libs.types.binding.api.Window;
import be.nabu.libs.types.binding.json.JSONBinding;
import be.nabu.libs.types.structure.Structure;
import be.nabu.libs.types.structure.StructureGenerator;

public class ComplexContentJSONSerializer implements DataSerializer<ComplexContent> {

	private JSONBinding binding;
	
	public ComplexContentJSONSerializer(ComplexType type) {
		this.binding = new JSONBinding(type, Charset.forName("UTF-8"));
		binding.setAddDynamicElementDefinitions(true);
		binding.setAllowDynamicElements(true);
		binding.setComplexTypeGenerator(new StructureGenerator());
		binding.setParseNumbers(true);
	}
	
	public ComplexContentJSONSerializer() {
		//this((ComplexType) BeanResolver.getInstance().resolve(Object.class));
		this(new Structure());
	}
	
	@Override
	public void serialize(ComplexContent content, OutputStream output) throws IOException {
		binding.marshal(output, content);
	}

	@Override
	public ComplexContent deserialize(InputStream input) throws IOException {
		try {
			return binding.unmarshal(input, new Window[0]);
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<ComplexContent> getDataClass() {
		return ComplexContent.class;
	}

}
