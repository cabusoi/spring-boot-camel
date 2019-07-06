package integration.camel;

import java.sql.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class PersistenceService {

	@PersistenceContext
	EntityManager em;

	@Transactional
	public void persist(PersistNode in) {
		if (update(in)) {
			return;
		}
		insert(in);
	}

	private void insert(PersistNode in) {
		String columns = in.properties.keySet().stream().collect(Collectors.joining(","));
		String values = in.properties.entrySet().stream().map((Map.Entry<?, ?> e) -> formatSql(e.getValue()))
				.collect(Collectors.joining(","));
		String sqlInsert = String.format("INSERT INTO %s (%s) VALUES ( %s )", in.type, columns, values);
		Query queryInsert = em.createNativeQuery(sqlInsert);
		queryInsert.executeUpdate();
	}

	private boolean update(PersistNode in) {
		String pairs = in.properties.entrySet().stream()
				.map((Map.Entry<?, ?> e) -> String.format(" %s = %s", e.getKey(), formatSql(e.getValue())))
				.collect(Collectors.joining(","));
		String sqlUpdate = String.format("UPDATE %s SET %s", in.type, pairs);
		Query queryUpdate = em.createNativeQuery(sqlUpdate);
		boolean updated = queryUpdate.executeUpdate() > 0;
		return updated;
	}

	private String formatSql(Object e) {
		if(e==null) {
			return "null";
		}
		String value;
		switch (e.getClass().getName()) {
		case "java.lang.String":
			value = String.format("'%s'", e);
			break;
		case "java.lang.Integer":
			value = e.toString();
			break;
		case "java.lang.Double":
			value = e.toString();
			break;
		case "java.lang.Boolean":
			value = e.toString();
			break;
		case "java.sql.Date":
		case "java.util.Date":
			value = String.format("'%s'", ((Date) e).toString());
			break;
		default:
			throw new RuntimeException("unexpected type");
		}
		return value;
	}
}
