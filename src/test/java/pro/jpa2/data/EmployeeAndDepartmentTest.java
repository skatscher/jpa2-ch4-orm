package pro.jpa2.data;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import pro.jpa2.model.Employee;

@RunWith(Arquillian.class)
//@UsingDataSet("employeeTestData.yml")
public class EmployeeAndDepartmentTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "empDTest.war")
//				.addClasses(Employee.class, Department.class, GenericDao.class,
//						Ordering.class, Resources.class)
				.addPackages(true, "pro.jpa2")
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	GenericDao<Employee> dao;

	@Inject
	Logger log;

	@Before
	public void before() {
		dao.setKlazz(Employee.class);
	}

	@Test
	public void testFindAll() throws Exception {

		log.warn("started find all employees test");

		Collection<Employee> allEmployees = dao.findAll();

		assertEquals(2, allEmployees.size());
		for (Employee e : allEmployees) {
			log.info("found employee: {}", e);
		}
	}

}
