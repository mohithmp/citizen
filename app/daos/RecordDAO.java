package daos;

import java.util.Date;
import java.util.UUID;

import org.mongodb.morphia.Datastore;

import dtos.request.AddRecordRequestDTO;
import models.Record;
import utils.MongoConnection;

public class RecordDAO {

	private static Datastore ds = MongoConnection.getDS();

	public Record add(AddRecordRequestDTO payload) {
		Record newRecord = new Record();
		newRecord.setRecordId(UUID.randomUUID().toString());
		newRecord.setObservationId(payload.observationId);
		newRecord.setData(payload.data);
		newRecord.setCreatedTime(new Date().getTime());
		newRecord.setUpdatedTime(new Date().getTime());
		ds.save(newRecord);
		return newRecord;
	}

}
