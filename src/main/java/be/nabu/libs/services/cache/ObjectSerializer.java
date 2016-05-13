package be.nabu.libs.services.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import be.nabu.libs.cache.api.DataSerializer;
import be.nabu.libs.types.ComplexContentWrapperFactory;
import be.nabu.libs.types.api.ComplexContent;
import be.nabu.libs.types.api.WrappedComplexContent;

public class ObjectSerializer implements DataSerializer<Object> {

	private ComplexContentSerializer complexContentSerializer = new ComplexContentSerializer();
	
	@SuppressWarnings("unchecked")
	@Override
	public void serialize(Object object, OutputStream output) throws IOException {
		ComplexContent content = object instanceof ComplexContent ? (ComplexContent) object : ComplexContentWrapperFactory.getInstance().getWrapper().wrap(object);
		if (object != null && content == null) {
			throw new IllegalArgumentException("Can not convert to complex content: " + object.getClass());
		}
		complexContentSerializer.serialize(content, output);
	}

	@Override
	public Object deserialize(InputStream input) throws IOException {
		ComplexContent deserialize = complexContentSerializer.deserialize(input);
		if (deserialize instanceof WrappedComplexContent) {
			return ((WrappedComplexContent<?>) deserialize).getUnwrapped();
		}
		return deserialize;
	}

	@Override
	public Class<Object> getDataClass() {
		return Object.class;
	}

}
