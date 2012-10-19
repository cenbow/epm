package br.net.woodstock.epm.document.jackrabbit;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.JackrabbitSession;

interface JackRabbitCallback {

	Object execute(JackrabbitSession session) throws RepositoryException;

}
