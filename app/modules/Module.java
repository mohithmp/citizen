package modules;

import com.google.inject.AbstractModule;

import utils.CustomObjectMapper;
import utils.CustomObjectMapperImpl;
import utils.ErrorHandler;

public class Module extends AbstractModule {
	@Override
	protected void configure() {
		bind(CustomObjectMapper.class).to(CustomObjectMapperImpl.class);
		bind(ErrorHandler.class).asEagerSingleton();
	}
}