package crm.config;

import crm.dbservice.bean.DBService;
import crm.dbservice.bean.DBServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "crm")
public class ApplicationConfigs {

    @Bean
    @Scope("singleton")
    public DBService dbService() {
        return new DBServiceImp("CRM");
    }

    @Bean
    @Scope("singleton")
    public ServerProperties serverProperties() {
        ServerProperties properties = new ServerProperties();
        try (FileReader reader = new FileReader(new File("src/main/resources/data.properties"))) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
