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
