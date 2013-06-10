package br.net.woodstock.epm.orm.listener;

import br.net.woodstock.epm.util.EPMLog;
import br.net.woodstock.rockframework.domain.Entity;

public class PostLoadListener {

	public PostLoadListener() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public void postLoad(final Object obj) {
		if (obj instanceof Entity) {
			Entity e = (Entity) obj;
			EPMLog.getLogger().info("SELECT " + e.getClass().getSimpleName() + "[" + e.getId() + "]");
		}
	}
}
