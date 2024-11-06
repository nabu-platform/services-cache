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
