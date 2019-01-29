package be.nabu.libs.services.cache;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.libs.authentication.api.Token;
import be.nabu.libs.cache.api.CacheRefresher;
import be.nabu.libs.services.ServiceRuntime;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.services.api.ExecutionContextProvider;
import be.nabu.libs.services.api.ServiceException;
import be.nabu.libs.types.api.ComplexContent;

public class ServiceRefresher implements CacheRefresher {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private DefinedService service;
	private ExecutionContextProvider contextProvider;
	private Token token;

	public ServiceRefresher(ExecutionContextProvider contextProvider, Token token, DefinedService service) {
		this.contextProvider = contextProvider;
		this.token = token;
		this.service = service;
	}
	
	@Override
	public Object refresh(Object key) throws IOException {
		// allow runs with no input
		if (key instanceof ComplexContent || key == null) {
			ServiceRuntime runtime = new ServiceRuntime(service, contextProvider.newExecutionContext(token));
			// disable caching for the refresh
			runtime.setAllowCaching(false);
			try {
				return runtime.run((ComplexContent) key);
			}
			catch (ServiceException e) {
				logger.error("Could not refresh cache entry for service: " + service.getId(), e);
			}
		}
		return null;
	}

}
