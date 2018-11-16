package daos;

import java.util.Date;
import java.util.UUID;

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
	
	public Observation add(Observation newObservation) {
		newObservation.setObservationId(UUID.randomUUID().toString());
		newObservation.setCreatedTime(new Date().getTime());
		newObservation.setUpdatedTime(new Date().getTime());
		ds.save(newObservation);
		return newObservation;
		
	}

	public Observation findAccountObservation(String accountId, String observationId) {
		 return ds.find(Observation.class).filter("isDeleted", false).filter("accountId", accountId)
				.filter("observationId", observationId).get();
	}

}
