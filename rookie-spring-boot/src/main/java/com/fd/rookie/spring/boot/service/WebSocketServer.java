package com.fd.rookie.spring.boot.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/websocket/{loginName}")
public class WebSocketServer {
    // 接口路径 ws://127.0.0.1:8500/websocket/loginName;
    private Session session;

    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketUtils = new CopyOnWriteArraySet<>();
    // 用来存在线连接数
    private static Map<String, Session> sessionPool = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "loginName") String loginName) {
        try {
            this.session = session;
            webSocketUtils.add(this);
            sessionPool.put(loginName, session);
            log.info("【websocket消息】有新的连接，总数为: {}", webSocketUtils.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(@PathParam(value = "loginName") String loginName) {
        try {
            webSocketUtils.remove(this);
            sessionPool.remove(loginName);
            log.info("【websocket消息】连接断开，总数为: {}", webSocketUtils.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        log.debug("【websocket消息】收到客户端消息: {}", message);
        JSONObject obj = new JSONObject();
        session.getAsyncRemote().sendText(obj.toJSONString());
    }


    @OnError
    public void OnError(Session session, @PathParam(value = "loginName") String loginName, Throwable t) {
        try {
            if (session.isOpen()) {
                session.close();
            }
            webSocketUtils.remove(this);
            sessionPool.remove(loginName);
            log.info("【websocket消息】连接[错误]断开，总数为: {}, 错误：{}", webSocketUtils.size(), t.getMessage());
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        log.info("【websocket消息】广播消息:" + message);
        for (WebSocketServer webSocket : webSocketUtils) {
            try {
                if (webSocket.session.isOpen()) {
                    webSocket.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String loginName, String message) {
        Session session = sessionPool.get(loginName);
        if (session != null && session.isOpen()) {
            try {
                log.info("【websocket消息】 单点消息:" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] loginNameList, String message) {
        for (String loginName : loginNameList) {
            Session session = sessionPool.get(loginName);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【websocket消息】 单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

    }

}
