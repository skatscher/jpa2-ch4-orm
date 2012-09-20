package pro.jpa2.data;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hamcrest.Description;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import pro.jpa2.model.EmployeeCustomId;

@RunWith(Arquillian.class)
//@UsingDataSet("employeeTestData.yml")
public class EmployeeCustomIdTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				// .addClasses(Employee.class, GenericDao.class, Ordering.class,
				// Resources.class)
				.addPackages(true, "pro.jpa2")
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	GenericDao<EmployeeCustomId> dao;

	@Inject
	EntityManager em;

	@Inject
	Logger log;

	@Before
	public void before() {
		dao.setKlazz(EmployeeCustomId.class);

		log.info("the injected em and the one in the dao are same: {}",
				em == dao.getEm());
		log.info("the injected em: {}", em);
		log.info("the em  in the dao: {}", dao.getEm());
	}

	@Test
	public void testFindAll() throws Exception {
		log.warn("------------------------------------------------------------------");
		log.warn("started EmployeeCustomId persistence test: empty db");

		Collection<EmployeeCustomId> allEmployees = dao.findAll();
		assertTrue(allEmployees.isEmpty());
	}

	/**
	 * If the Id is not generated and not set, but still has a default value, no
	 * exception is thrown and no id is generated. A generated id would normally
	 * start with 1, here 0 is perfectly OK
	 */
	@Test
	public void testCreateNewWithDefaultId() {
		log.warn("------------------------------------------------------------------");
		log.warn("started test: creating a new employee without setting an id explicitly");

		EmployeeCustomId e = new EmployeeCustomId();
		e.setName("noname");
		log.info("employee id before persisting : {}", e.getId());
		assertThat(e, hasId(0));

		log.info("the persistence ctx contains the entity : {}", em.contains(e));

		dao.create(e, e.getId());
		log.info("employee id after persisting : {}", e.getId());
		assertThat(e, hasId(0));
		log.info("the persistence ctx contains the entity : {}", em.contains(e));
	}

	// TODO : what does merge do - what entity do I hold after the merge is
	// completed
	@Test
	public void testCreateNewWithDefaultIdTwice() {
		log.warn("------------------------------------------------------------------");
		log.warn("started test: creating a new employee wit the same id twice");

		EmployeeCustomId e = new EmployeeCustomId();
		e.setName("first");
		log.info("first employee id before persisting : {}", e.getId());
		assertThat(e, hasId(0));
		e.setId(23);
		log.info("the persistence ctx contains the entity : {}", em.contains(e));

		dao.create(e, e.getId());
		log.info("first employee id after persisting : {}", e.getId());
		assertThat(e, hasId(23));

		log.info("the persistence ctx contains the entity : {}", em.contains(e));

		// expected to merge, effectively replacing the data of the first entity
		// with the second one
		e = new EmployeeCustomId();
		e.setName("second");
		log.info("second employee id before persisting : {}", e.getId());
		assertThat(e, hasId(0));
		e.setId(23);
		log.info("the persistence ctx contains the entity : {}", em.contains(e));

		dao.create(e, e.getId());
		log.info("second employee after persisting : {}", e.getId());
		assertThat(e, hasId(23));

		log.info("the persistence ctx contains the entity : {}", em.contains(e));

		// now retrieve the entity and check the data:
	}

	/**
	 * User-defined Id should remain unchanged -> TODO : using merge and persist
	 * demands a different API for checking
	 */
	@Test
	public void testCreateNewWithSetId() {

		log.warn("------------------------------------------------------------------");
		log.warn("started test: creating a new employee and setting an id explicitly");

		EmployeeCustomId e = new EmployeeCustomId();
		e.setName("noname");
		log.info("employee id before persisting : {}", e.getId());
		log.info("the persistence ctx contains the entity : {}", em.contains(e));

		assertThat(e, hasId(0));
		e.setId(99);
		dao.create(e, 99);
		log.info("employee id after persisting : {}", e.getId());
		assertThat(e, hasId(99));
		log.info("the persistence ctx contains the entity : {}", em.contains(e));

	}

	@Test
	public void testCDIAndEmScoping() {
		log.warn("------------------------------------------------------------------");
		log.warn("started test: testCDIAndEmScoping");
		EmployeeCustomId e = new EmployeeCustomId();
		e.setId(42);
		log.info("the persistence ctx contains the entity : {}", em.contains(e));
		dao.simplePersist(e);
		log.info("the persistence ctx contains the entity : {}", em.contains(e));
	}

	// Using hamcrest as a DSL provider for the tests
	public static TypeSafeMatcher<EmployeeCustomId> hasId(final int expectedId) {

		return new TypeSafeMatcher<EmployeeCustomId>() {

			protected int expected = expectedId;

			@Override
			public void describeTo(Description description) {
				description.appendText(Integer.toString(expectedId));
			}

			@Override
			public boolean matchesSafely(EmployeeCustomId e) {
				return e.getId() == expected;
			}
		};
	}
}
