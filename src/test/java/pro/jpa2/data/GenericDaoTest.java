package pro.jpa2.data;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import pro.jpa2.model.Employee;
import pro.jpa2.util.Resources;

@RunWith(Arquillian.class)
@UsingDataSet("emps.yml")
@ShouldMatchDataSet("emps.yml")
public class GenericDaoTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(Employee.class, GenericDao.class, Ordering.class,
						Resources.class)
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

		log.warn("started new arquilllian test");
		Collection<Employee> allEmployees = dao.findAll();

		for (Employee e : allEmployees) {
			log.info("found: {}", e);
		}
	}

	@Test
	public void testFindPaged() throws Exception {

		log.warn("started new arquilllian test");
		Collection<Employee> foundEmployees = dao.find(0, 1);

		assertEquals(1, foundEmployees.size());
	}

	@Test
	public void testFindById() throws Exception {

		log.warn("started new arquilllian test");
		Map<String, String> predicates = new HashMap<String, String>();
		predicates.put("2", "id");
		List<Employee> foundEmployees = dao.find(0, 1);

		assertEquals(1, foundEmployees.size());
		assertEquals(2, foundEmployees.get(0).getId());
	}
}
