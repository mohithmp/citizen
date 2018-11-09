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

	private static String SERVER_URL = "ds147003.mlab.com";

	private static int PORT = 47003;

	private static String USERNAME = "citizen";

	private static String PASSWORD = "citizen@123";

	private static String DBNAME = "citizen";

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
					ServerAddress primaryServer = new ServerAddress(SERVER_URL, PORT);

					// Secondary Server
					ServerAddress secondaryServer = new ServerAddress(SERVER_URL, PORT);

					// MongoDB read/write configuration
					MongoClientOptions.Builder mongoConf = new MongoClientOptions.Builder();
					mongoConf.writeConcern(WriteConcern.JOURNAL_SAFE);
					mongoConf.readPreference(ReadPreference.nearest());

					MongoCredential mongoCred = MongoCredential.createScramSha1Credential(USERNAME, DBNAME,
							PASSWORD.toCharArray());

					// Creating MongoClient
					MongoClient mongoClient = new MongoClient(Arrays.asList(primaryServer, secondaryServer),
							Arrays.asList(mongoCred, mongoCred), mongoConf.build());

					Morphia morphia = new Morphia();
					// return dataStore;
					DS = morphia.createDatastore(mongoClient, DBNAME);
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
			ServerAddress primaryServer = new ServerAddress(SERVER_URL, PORT);

			// Secondary Server
			ServerAddress secondaryServer = new ServerAddress(SERVER_URL, PORT);

			// MongoDB read/write configuration
			MongoClientOptions.Builder mongoConf = new MongoClientOptions.Builder();
			mongoConf.writeConcern(WriteConcern.JOURNAL_SAFE);
			mongoConf.readPreference(ReadPreference.nearest());

			MongoCredential mongoCred = MongoCredential.createScramSha1Credential(USERNAME, DBNAME,
					PASSWORD.toCharArray());

			// Creating MongoClient
			@SuppressWarnings("resource")
			MongoClient mongoClient = new MongoClient(Arrays.asList(primaryServer, secondaryServer),
					Arrays.asList(mongoCred, mongoCred), mongoConf.build());

			db = mongoClient.getDatabase(DBNAME);
		}
		return db;
	}

}