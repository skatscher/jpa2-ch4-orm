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

import pro.jpa2.model.Department;
import pro.jpa2.model.Employee;

@RunWith(Arquillian.class)
public class EmployeeAndDepartmentTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "empDTest.war")
				// .addClasses(Employee.class, Department.class,
				// GenericDao.class,
				// Ordering.class, Resources.class)
				.addPackages(true, "pro.jpa2")
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsResource("testSeeds/1Employee1Dept.sql", "import.sql")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	GenericDao<Employee> empDao;

	@Inject
	GenericDao<Department> deptDao;

	@Inject
	Logger log;

	@Before
	public void before() {
		empDao.setKlazz(Employee.class);
		deptDao.setKlazz(Department.class);
	}

	@Test
	public void testFindAll() throws Exception {

		log.warn("------------------------------------------------------------------");
		log.warn("started EmployeeAndDepartment test: a single department and single employee through import");

		Collection<Employee> allEmployees = empDao.findAll();
		assertEquals(1, allEmployees.size());

		Collection<Department> allDepartments = deptDao.findAll();
		assertEquals(1, allDepartments.size());
	}

}
