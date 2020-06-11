package zzu.chatroom.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import zzu.chatroom.common.Entity.Message;
import zzu.chatroom.common.Entity.RedisName;
import zzu.chatroom.common.Entity.initList;
import zzu.chatroom.ws.Service.RedisService;
import zzu.chatroom.ws.config.EnviromentConfig;
import zzu.chatroom.ws.untils.CRApplicationContextAware;
import zzu.chatroom.common.Untils.JsonUtil;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@DependsOn("enviromentConfig")
@ServerEndpoint("/webSocket/{uid}")
@Component
public class WebSocketServer {
    public static ConcurrentHashMap<Long,WebSocketServer> wsMap=new ConcurrentHashMap<>();
    public static Environment evn=((EnviromentConfig)CRApplicationContextAware.getBean(EnviromentConfig.class)).getEnvironment();
    private  String serverPort =evn.getProperty("server.port");

    private  String msgToAll= evn.getProperty("redis.channel.msgToAll");

    private static RedisService redisService;
    @Autowired
    public void setRedisService(RedisService redisService){
        WebSocketServer.redisService=redisService;
    }

    private Session session;
    private Long uid;

    @OnOpen
    public void onOpen(Session session, @PathParam("uid") Long uid){
        if (uid == null) return;
        this.session=session;
        this.uid=uid;
        wsMap.put(uid,this);
        this.sendMsg(redisService.initList(this.uid));
        log.info("WSport:"+serverPort+"用户:"+this.uid+"已上线！");
    }

    @OnError
    public void onError(Session session,Throwable error){
        error.getStackTrace();
    }

    @OnClose
    public void onClose(){
        wsMap.remove(this.uid);
        log.info("WSport:"+serverPort+",用户:"+this.uid+"已下线！");
        redisService.whenClose(this.uid);
    }

    @OnMessage
    public void onMessage(String msg){
        String type=JsonUtil.parseJsonToObj(msg,Message.class).getType();
        if("msg".equals(type)){
            log.info("WSport:"+serverPort+"的用户"+this.uid+"发送信息:"+msg);
            redisService.sendToPub(msg);
        }else if("img".equals(type)){
            log.info("WSport:"+serverPort+"的用户"+this.uid+"发送信息:"+msg);
            redisService.sendToPub(msg);
        }else if("file".equals(type)){
            log.info("WSport:"+serverPort+"的用户"+this.uid+"发送信息:"+msg);
            redisService.sendToPub(msg);
        }
        else if("init".equals(type)){
            this.sendMsg(redisService.initList(this.uid));
        }
    }

    public void sendMsg(String msg)  {
      try {
          log.info("WSport:"+serverPort+",发送消息"+msg+"给用户"+this.uid);
          this.session.getAsyncRemote().sendText(msg);
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    public Session getSession(){return this.session;}
    public Long getUid(){return this.uid;}
}
