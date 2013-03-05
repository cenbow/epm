package br.net.woodstock.epm.impl.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.epm.security.api.LocaleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@Transactional
@TransactionConfiguration
public class DepartmentTest {

	@Autowired(required = true)
	private LocaleService	service;

	public DepartmentTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		DepartmentSkell departmentSkell = this.service.getDepartmentSkellById(Integer.valueOf(3));
		System.out.println(departmentSkell.getParentName());
		System.out.println(departmentSkell.getFullName());
	}
}
