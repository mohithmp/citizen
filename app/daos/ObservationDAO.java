package daos;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

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

	public void update(Observation observation, String title, String description, List<String> tags) {

		UpdateOperations<Observation> ops = ds.createUpdateOperations(Observation.class);

		if (title != null) {
			ops.set("title", title);
		}
		if (description != null) {
			ops.set("description", description);
		}
		if (tags != null) {
			ops.set("tags", tags);
		}
		ops.set("updatedTime", new Date().getTime());
		ds.update(observation, ops);

	}

}
