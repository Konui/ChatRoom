package zzu.chatroom.ws.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import zzu.chatroom.ws.untils.CRApplicationContextAware;

@DependsOn("cRApplicationContextAware")
@Component("enviromentConfig")
public class EnviromentConfig {
    @Autowired
    private Environment environment;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
