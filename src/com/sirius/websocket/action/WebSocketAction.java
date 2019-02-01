package com.sirius.websocket.action;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/broadcast")
public class WebSocketAction {
	
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	
	@OnOpen
    public void onOpen(Session session) throws IOException {
		System.out.println("Open Session");
		clients.add(session);
		session.getBasicRemote().sendText("connected:"+session.getId());
    }

    @OnMessage
    public void echo(String message, Session session) {
    		try {
        			System.out.println("Message received:"+message);
        			synchronized(clients){
        				// Iterate over the connected sessions
        				// and broadcast the received message
        				for(Session client : clients){        					
        						client.getBasicRemote().sendText(message);        					
        				}
        			}
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
    		clients.remove(session);
    		System.out.println("Close Session");
    }
}
