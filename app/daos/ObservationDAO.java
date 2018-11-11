package daos;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import models.Observation;
import utils.MongoConnection;

public class ObservationDAO {

	private static Datastore ds = MongoConnection.getDS();

	public Query<Observation> getBasicQuery() {
		Query<Observation> query = ds.find(Observation.class).filter("isDeleted", false);
		return query;
	}

}
