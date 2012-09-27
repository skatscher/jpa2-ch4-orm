package pro.jpa2.data;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.ejb.EJBException;
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

/**
 * Test template, using a yml dataset. This seems to work for a single dataset
 * and a single test
 *
 * @author kostja
 *
 */
@RunWith(Arquillian.class)
//@UsingDataSet("employeeTestData.yml")
public class EmployeeTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				// .addClasses(Employee.class, GenericDao.class, Ordering.class,
				// Resources.class)
				.addPackages(true, "pro.jpa2")
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				//a safer way to seed with Hibernate - the @UsingDataSet breaks
				.addAsResource("import.sql", "import.sql")
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
		log.warn("------------------------------------------------------------------");
		log.warn("started findAll test");

		Collection<Employee> allEmployees = dao.findAll();

		log.info("found {} employees", allEmployees.size());
		for (Employee e : allEmployees) {
			log.info("found employee: {}", e);
		}
	}

	@Test
	public void testCreateNewWithExistingId() {
		log.warn("------------------------------------------------------------------");
		log.warn("started test: creating employee with existing id ");

		Employee e = new Employee();
		e.setName("noname");

		// there is already an employee with id 1
		log.info("employee id before persisting : {}", e.getId());
		assertEquals(0, e.getId());
		// assertFalse(dao.create(e, 1));
		dao.create(e, e.getId());
		log.info("employee id after persisting : {}", e.getId());
		assertThat(e.getId(), not(0));
	}

	@Test
	public void testCreateNewWithGeneratedId() {
		log.warn("------------------------------------------------------------------");
		log.warn("started test: creating a new employee without setting an id explicitly");

		Employee e = new Employee();
		e.setName("noname");
		log.info("employee id before persisting : {}", e.getId());
		assertEquals(0, e.getId());
		// assertTrue(dao.create(e, 99));
		dao.create(e, e.getId());
		log.info("employee id after persisting : {}", e.getId());
		assertThat(e.getId(), not(99));
	}

	@Test(expected = EJBException.class)
	public void testCreateNewWithSetId() {
		log.warn("------------------------------------------------------------------");
		log.warn("started test: creating a new employee and setting an id explicitly");

		Employee e = new Employee();
		e.setName("noname");
		log.info("employee id before persisting : {}", e.getId());
		assertEquals(0, e.getId());
		e.setId(99);
		// assertTrue(dao.create(e, 99));
		dao.create(e, 99);
		log.info("employee id after persisting : {}", e.getId());
		assertThat(e.getId(), not(99));
	}

	// TODO : check for creation of entities with preset ids for not generated
	// entities
}
