package info.silin.jpa2.data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class Dao<T> {

	@Inject
	private EntityManager em;

	private Class<T> klazz;

	public T get(long id) {
		return em.find(klazz, id);
	}

	//TODO : just a stub for tests - watch out for merge issues!
	public void save(T e){
		em.persist(e);
	}

	public Class<T> getKlazz() {
		return klazz;
	}

	/**
	 * As there is ono way to get the class instance of T, even knowing what T
	 * is, this needs to be set
	 *
	 * @param klazz
	 */
	// TODO : PersistenceTools
	public void setKlazz(Class<T> klazz) {
		this.klazz = klazz;
	}

}
