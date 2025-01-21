//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//@Configuration
//public class MongoConfig {
//
//    @Bean
//    public MongoTemplate mongoTemplate(MongoClient mongoClient) throws Exception {
//        return new MongoTemplate(mongoClient, "your_database_name");
//    }
//
//    @Bean
//    public Mongo mongoClient() throws Exception {
//        // Configure MongoDB connection details (host, port, credentials)
//        return new MongoClient("localhost", 27017);
//    }
//}
