package info.silin.jpa2.data;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import info.silin.jpa2.model.Department;
import info.silin.jpa2.model.Employee;
import info.silin.jpa2.util.Resources;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

@RunWith(Arquillian.class)
@UsingDataSet("emploeeTestData.yml")
public class SimpleEployeePersistenceTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(Employee.class, Department.class, Dao.class,
						Resources.class)
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	Dao<Employee> dao;

	@Inject
	Logger log;

	@Before
	public void before() {
		dao.setKlazz(Employee.class);
	}

	@Test
	public void testRegister() throws Exception {

		log.warn("started new arquilllian test");

		Collection<Employee> allEmployees = dao.findAll();

		for (Employee e : allEmployees) {
			log.info("found: {}", e);
		}

	}

}
