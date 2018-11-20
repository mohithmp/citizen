package daos;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import exceptions.MyException;
import models.Record;
import utils.MongoConnection;

public class RecordDAO {

	private static Datastore ds = MongoConnection.getDS();

	public Record add(String observationId, HashMap<String, Object> data) {
		Record newRecord = new Record();
		newRecord.setRecordId(UUID.randomUUID().toString());
		newRecord.setObservationId(observationId);
		newRecord.setData(data);
		newRecord.setCreatedTime(new Date().getTime());
		newRecord.setUpdatedTime(new Date().getTime());
		ds.save(newRecord);
		return newRecord;
	}

	public boolean isRecordExists(String observationId) throws MyException {
		long count = ds.find(Record.class).filter("observationId", observationId).countAll();
		return count != 0;
	}

	public List<Record> find(String observationId, int page, int limit) throws MyException {
		return ds.find(Record.class).filter("observationId", observationId).order("-updatedTime").offset(page * limit)
				.limit(limit).asList();
	}
	
	public Query<Record> getBasicQuery() throws MyException {
		return ds.find(Record.class);
	}

}
