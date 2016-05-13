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
import be.nabu.libs.types.binding.xml.XMLBinding;
import be.nabu.libs.types.java.BeanResolver;

public class ComplexContentSerializer implements DataSerializer<ComplexContent> {

	private XMLBinding binding;
	
	public ComplexContentSerializer(ComplexType type) {
		this.binding = new XMLBinding(type, Charset.forName("UTF-8"));
	}
	
	public ComplexContentSerializer() {
		this.binding = new XMLBinding((ComplexType) BeanResolver.getInstance().resolve(Object.class), Charset.forName("UTF-8"));
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
