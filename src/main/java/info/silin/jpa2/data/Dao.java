package info.silin.jpa2.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
public class Dao<T> {

	@Inject
	private EntityManager em;

	private Class<T> klazz;

	public T get(long id) {
		return em.find(klazz, id);
	}

	public Collection<T> findAll() {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(klazz);
		Root<T> from = q.from(klazz);
		CriteriaQuery<T> select = q.select(from);

		Set<T> results = new HashSet<T>();
		results.addAll(em.createQuery(select).getResultList());
		return results;
	}

	// TODO : just a stub for tests - watch out for merge issues!
	public void save(T e) {
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
