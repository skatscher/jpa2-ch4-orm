package pro.jpa2.data;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import javax.inject.Inject;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
// @UsingDataSet("employeeTestData.yml")
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
	Logger log;

	@Before
	public void before() {
		dao.setKlazz(EmployeeCustomId.class);
	}

	@Test
	public void testFindAll() throws Exception {

		log.warn("started EmployeeCustomId persistence test");

		Collection<EmployeeCustomId> allEmployees = dao.findAll();

		for (EmployeeCustomId e : allEmployees) {
			log.info("found employee: {}", e);
		}
	}

	/**
	 * If the Id is not generated and not set, but still has a default value, no
	 * exception is thrown and no id is generated. A generated id would normally
	 * start with 1, here 0 is perfectly OK
	 */
	@Test
	public void testCreateNewWithDefaultId() {

		log.warn("started test: creating a new employee without setting an id explicitly");

		EmployeeCustomId e = new EmployeeCustomId();
		e.setName("noname");
		log.info("employee id before persisting : {}", e.getId());
		assertThat(e, hasId(0));
		dao.create(e, e.getId());
		log.info("employee id after persisting : {}", e.getId());
		assertThat(e, hasId(0));
	}

	// TODO : what does merge do - what entity do I hold after the merge is
	// completed
	@Test
	public void testCreateNewWithDefaultIdTwice() {

		log.warn("started test: creating a new employee without setting an id explicitly");

		EmployeeCustomId e = new EmployeeCustomId();
		e.setName("first");
		log.info("first employee id before persisting : {}", e.getId());
		assertThat(e, hasId(0));
		dao.create(e, e.getId());
		log.info("first employee id after persisting : {}", e.getId());
		assertThat(e, hasId(0));

		e = new EmployeeCustomId();
		e.setName("second");
		log.info("second employee id before persisting : {}", e.getId());
		assertThat(e, hasId(0));
		dao.create(e, e.getId());
		log.info("second employee after persisting : {}", e.getId());
		assertThat(e, hasId(0));
	}

	/**
	 * User-defined Id should remain unchanged -> TODO : using merge and persist demands a different API for checking
	 */
	@Test
	public void testCreateNewWithSetId() {

		log.warn("started test: creating a new employee and setting an id explicitly");

		EmployeeCustomId e = new EmployeeCustomId();
		e.setName("noname");
		log.info("employee id before persisting : {}", e.getId());
		assertThat(e, hasId(0));
		e.setId(99);
		dao.create(e, 99);
		log.info("employee id after persisting : {}", e.getId());
		assertThat(e, hasId(99));
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
