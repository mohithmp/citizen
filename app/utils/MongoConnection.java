package utils;

import java.util.Arrays;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

import play.Logger;

/**
 * 
 * @author Abhilash
 *
 */
public class MongoConnection {
	private static final Object someObj = new Object();

	private volatile static Datastore DS = null;

	private volatile static MongoDatabase db = null;

	/**
	 * private constructor singleton class
	 */
	private MongoConnection() {
	};

	/**
	 * 
	 * @return mongodb connection using morphia
	 */
	public static Datastore getDS() {

		if (DS == null) {
			synchronized (someObj) {
				try {
					// Primary Server
					ServerAddress primaryServer = new ServerAddress(System.getenv("PRIMARY_SERVER"),
							Integer.parseInt(System.getenv("PORT")));

					// Secondary Server
					ServerAddress secondaryServer = new ServerAddress(System.getenv("SECONDARY_SERVER"),
							Integer.parseInt(System.getenv("PORT")));

					// MongoDB read/write configuration
					MongoClientOptions.Builder mongoConf = new MongoClientOptions.Builder();
					mongoConf.writeConcern(WriteConcern.JOURNAL_SAFE);
					mongoConf.readPreference(ReadPreference.nearest());

					MongoCredential mongoCred = MongoCredential.createScramSha1Credential(System.getenv("USERNAME"),
							System.getenv("DBNAME"), System.getenv("PASSWORD").toCharArray());

					// Creating MongoClient
					MongoClient mongoClient = new MongoClient(Arrays.asList(primaryServer, secondaryServer),
							Arrays.asList(mongoCred, mongoCred), mongoConf.build());

					Morphia morphia = new Morphia();
					// return dataStore;
					DS = morphia.createDatastore(mongoClient, System.getenv("DBNAME"));
				} catch (Exception e) {
					Logger.info(
							"===========================Error in Connecting to Mongo db==============================");
					e.printStackTrace();
				}
			}
		}
		return DS;
	}

	/**
	 * 
	 * @return mongodb connection using java driver
	 */
	public static synchronized MongoDatabase getConnection() {

		if (db == null) {
			ServerAddress primaryServer = new ServerAddress(System.getenv("PRIMARY_SERVER"),
					Integer.parseInt(System.getenv("PORT")));

			// Secondary Server
			ServerAddress secondaryServer = new ServerAddress(System.getenv("SECONDARY_SERVER"),
					Integer.parseInt(System.getenv("PORT")));

			// MongoDB read/write configuration
			MongoClientOptions.Builder mongoConf = new MongoClientOptions.Builder();
			mongoConf.writeConcern(WriteConcern.JOURNAL_SAFE);
			mongoConf.readPreference(ReadPreference.nearest());

			MongoCredential mongoCred = MongoCredential.createScramSha1Credential(System.getenv("USERNAME"),
					System.getenv("DBNAME"), System.getenv("PASSWORD").toCharArray());

			// Creating MongoClient
			@SuppressWarnings("resource")
			MongoClient mongoClient = new MongoClient(Arrays.asList(primaryServer, secondaryServer),
					Arrays.asList(mongoCred, mongoCred), mongoConf.build());

			db = mongoClient.getDatabase(System.getenv("DBNAME"));
		}
		return db;
	}

}