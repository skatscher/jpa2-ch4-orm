package pro.jpa2.model;

import javax.persistence.Embeddable;

/**
 * An Embeddable is not a complete Entity -it needs an entity to be identified.
 * The fields of an Embeddable will be persisted in the same table along with
 * the embedding entity
 *
 * @author kostja
 *
 */
@Embeddable
public class Address {

	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Address [city=" + city + "]";
	}
}