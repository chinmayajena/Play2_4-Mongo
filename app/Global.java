import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;

import play.Application;
import play.Configuration;
import play.GlobalSettings;
import play.Play;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;


public class Global extends GlobalSettings {
  
  private MongoClient mongo = null;
  private static String MONGO_HOST = null;
  private static String MONGO_HOSTS = null;
  private static String MONGO_PORT = "0";
  private static String DB_NAME = null;
  private static String DB_USER = null;
  private static String PASSWORD = null;
  private static String RS_FLG = null;
  private static Datastore DATASTORE = null;
  private static Morphia MORPHIA = null;

  
  @Override
  public void onStart(Application app) {
      super.beforeStart(app);
      
      loadProperties();
      initializeMongo();
      
  }
  
  private void loadProperties() {
    Configuration configuration = Play.application().configuration();
    MONGO_HOST = configuration.getString("mongodb.host", "mongohost");
    MONGO_HOSTS = configuration.getString("mongodb.hosts");
    MONGO_PORT = configuration.getString("mongodb.port", "27017");
    DB_NAME = configuration.getString("mongodb.db", "test");
    DB_USER = configuration.getString("mongodb.user");
    PASSWORD = configuration.getString("mongodb.password");
    RS_FLG = configuration.getString("mongodb.rs.enabled", "false");
    
  }
  
  
  private void initializeMongo() {
    mongo = null;
    MongoCredential credential = null;
    if (!StringUtils.isBlank(DB_USER) && !StringUtils.isBlank(PASSWORD)) {
      credential = MongoCredential.createCredential(DB_USER, DB_NAME, PASSWORD.toCharArray());
    }
    if ("true".equalsIgnoreCase(RS_FLG)) {
      String[] hosts = MONGO_HOSTS.split(",");
      String[] ports = MONGO_PORT.split(",");
      List<ServerAddress> servers = new ArrayList<ServerAddress>();
      for (int i = 0; i < hosts.length; i++) {
        servers.add(new ServerAddress(hosts[i], Integer.valueOf(ports[i % ports.length])));
      }
      if (credential == null) {
        mongo = new MongoClient(servers);
      } else {
        mongo = new MongoClient(servers, Arrays.asList(credential));
      }
    } else {
      ServerAddress serverAddress = new ServerAddress(MONGO_HOST, Integer.valueOf(MONGO_PORT));
      if (credential == null) {
        mongo = new MongoClient(serverAddress);
      } else {
        mongo = new MongoClient(serverAddress, Arrays.asList(credential));
      }
    }
    mongo.setReadPreference(ReadPreference.secondaryPreferred());

    MorphiaLoggerFactory.reset();

    MORPHIA = new Morphia();
    DATASTORE = MORPHIA.createDatastore(mongo, DB_NAME);
    DATASTORE.ensureIndexes();
    DATASTORE.ensureCaps();
    DATASTORE.setDefaultWriteConcern(WriteConcern.UNACKNOWLEDGED);
  }

}
