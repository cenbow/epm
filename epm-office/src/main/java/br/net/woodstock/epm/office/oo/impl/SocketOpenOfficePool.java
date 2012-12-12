package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.OpenOfficeServer;

public class SocketOpenOfficePool implements OpenOfficeServer {

	private static final int	MAX_TRIES	= 20;

	private OpenOfficeServer[]	servers;

	private int[]				ports;

	private boolean				running;

	public SocketOpenOfficePool(final int[] ports) {
		super();
		this.ports = ports;
		this.running = false;
		this.servers = new OpenOfficeServer[ports.length];
	}

	@Override
	public synchronized void start() {
		for (int i = 0; i < this.ports.length; i++) {
			this.servers[i] = new PoolableOpenOfficeServer(new SocketOpenOfficeServer(this.ports[i]));
			this.servers[i].start();
		}
		this.running = false;
	}

	@Override
	public synchronized void stop() {
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
		this.running = false;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public synchronized OpenOfficeConnection getConnection() {
		for (int t = 0; t < SocketOpenOfficePool.MAX_TRIES; t++) {
			for (int i = 0; i < this.servers.length; i++) {
				try {
					if ((this.servers[i] != null) && (this.servers[i].isRunning())) {
						PoolableOpenOfficeConnection connection = (PoolableOpenOfficeConnection) this.servers[i].getConnection();
						if (!connection.isConnected()) {
							connection.connect();
							OfficeLog.getLogger().info("Returning connection from server[" + i + "]");
							return connection;
						}
					} else {
						if (this.servers[i] != null) {
							this.servers[i].stop();
						}
						this.servers[i] = new PoolableOpenOfficeServer(new SocketOpenOfficeServer(this.ports[i]));
						this.servers[i].start();
					}
				} catch (Exception e) {
					OfficeLog.getLogger().info(e.getMessage(), e);
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				OfficeLog.getLogger().warn(e.getMessage(), e);
			}
		}
		throw new OpenOfficeException("Cold not get connection");
	}

	public int[] getPorts() {
		return this.ports;
	}

}
