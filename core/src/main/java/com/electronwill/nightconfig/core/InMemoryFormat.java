package com.electronwill.nightconfig.core;

import com.electronwill.nightconfig.core.io.ConfigParser;
import com.electronwill.nightconfig.core.io.ConfigWriter;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author TheElectronWill
 */
public final class InMemoryFormat implements ConfigFormat<SimpleConfig, Config, Config> {
	static final Predicate<Class<?>> DEFAULT_PREDICATE = type -> type.isPrimitive()
																 || type == Integer.class
																 || type == Long.class
																 || type == Float.class
																 || type == Double.class
																 || type == Boolean.class
																 || type == String.class
																 || Collection.class.isAssignableFrom(type)
																 || Config.class.isAssignableFrom(type);

	private static final InMemoryFormat DEFAULT_INSTANCE = new InMemoryFormat(DEFAULT_PREDICATE);
	private static final InMemoryFormat UNIVERSAL_INSTANCE = new InMemoryFormat(t -> true);

	public static InMemoryFormat defaultInstance() {
		return DEFAULT_INSTANCE;
	}

	public static InMemoryFormat withSupport(Predicate<Class<?>> supportPredicate) {
		return new InMemoryFormat(supportPredicate);
	}

	public static InMemoryFormat withUniversalSupport() {
		return UNIVERSAL_INSTANCE;
	}

	private final Predicate<Class<?>> supportPredicate;

	private InMemoryFormat(Predicate<Class<?>> supportPredicate) {
		this.supportPredicate = supportPredicate;
	}

	@Override
	public ConfigWriter<Config> createWriter() {
		throw new UnsupportedOperationException(
				"In memory configurations aren't mean to be " + "written.");
	}

	@Override
	public ConfigParser<SimpleConfig, Config> createParser() {
		throw new UnsupportedOperationException(
				"In memory configurations aren't mean to be " + "parsed.");
	}

	@Override
	public SimpleConfig createConfig() {
		return new SimpleConfig();
	}

	@Override
	public boolean supportsComments() {
		return false;
	}

	@Override
	public boolean supportsType(Class<?> type) {
		return supportPredicate.test(type);
	}

	@Override
	public boolean isInMemory() {
		return true;
	}
}