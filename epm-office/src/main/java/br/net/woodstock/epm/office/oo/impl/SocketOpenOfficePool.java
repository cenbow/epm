package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeServer;

public class SocketOpenOfficePool implements OpenOfficeServer {

	private OpenOfficeServer[]	servers;

	private int[]				ports;

	public SocketOpenOfficePool(final int[] ports) {
		super();
		this.ports = ports;
	}

	@Override
	public void start() {
		for (int i = 0; i < this.ports.length; i++) {
			this.servers[i] = new SocketOpenOfficeServer(this.ports[i]);
			try {
				this.servers[i].start();
			} catch (Exception e) {
				this.servers[i] = null;
			}
		}
	}

	@Override
	public void stop() {
		for (int i = 0; i < this.servers.length; i++) {
			try {
				if (this.servers[i] != null) {
					this.servers[i].stop();
					this.servers[i] = null;
				}
			} catch (Exception e) {
				OfficeLog.getLogger().info(e.getMessage(), e);
			}
		}
	}

	@Override
	public boolean isRunning() {
		for (int i = 0; i < this.servers.length; i++) {
			try {
				if ((this.servers[i] != null) && (this.servers[i].isRunning())) {
					return true;
				}
			} catch (Exception e) {
				OfficeLog.getLogger().info(e.getMessage(), e);
			}
		}
		return false;
	}

	@Override
	public OpenOfficeConnection getConnection() {
		for (int i = 0; i < this.servers.length; i++) {
			try {
				if ((this.servers[i] != null) && (this.servers[i].isRunning())) {
					return this.servers[i].getConnection();
				}
			} catch (Exception e) {
				OfficeLog.getLogger().info(e.getMessage(), e);
			}
		}
		return null;
	}

	public int[] getPorts() {
		return this.ports;
	}

}
