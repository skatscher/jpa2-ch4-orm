package pro.jpa2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * A simple Entity, taken from the Pro JPA2 book.
 *
 * Has a OneToOne relation to the {@link Employee}
 *
 * @author kostja
 *
 */
@Entity
public class ParkingSpace {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	private long salary;

	@OneToOne
	private ParkingEmployee employee;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSalary() {
		return salary;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	public ParkingEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ParkingEmployee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "ParkingSpace [id=" + id + ", name=" + name + ", salary="
				+ salary + ", employee=" + employee + "]";
	}

}