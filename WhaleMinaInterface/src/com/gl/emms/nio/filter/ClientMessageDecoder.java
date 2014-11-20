package com.gl.emms.nio.filter;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.gl.emms.nio.constant.EMMSConstant;
import com.gl.emms.nio.mutual.ServerMessage;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.ReplyBody;
import com.gl.emms.nio.mutual.RequestBody;
/**
 *  客户端消息解码
 *  @author 3979434@qq.com
 *
 */
public class ClientMessageDecoder extends CumulativeProtocolDecoder {

	private IoBuffer buff = IoBuffer.allocate(1024).setAutoExpand(true);

	@Override
	public boolean doDecode(IoSession iosession, IoBuffer iobuffer,
			ProtocolDecoderOutput out) throws Exception {
		boolean complete = false;
		while (iobuffer.hasRemaining()) {
			byte b = iobuffer.get();

			// CIMConstant.MESSAGE_SEPARATE 为消息界限
			// 当一次收到多个消息时，以此分隔解析多个消息
			
			if (b == '\b') {
				complete = true;
				break;
			} else {
				buff.put(b);
			}
		}
		if (complete) {
			Log.v("whale","complete true:");
			buff.flip();
			byte[] bytes = new byte[buff.limit()];
			buff.get(bytes);
			String message = new String(bytes, "UTF-8");
			buff.clear();
			Log.v("whale","ClientMessageDecoder:" + message);

			Object msg = mappingMessageObject(message);
			out.write(msg);
		}
		Log.v("whale","doDecode:");
		return complete;

		//return true;
	}

	private Object mappingMessageObject(String  message) throws Exception {
		Log.v("whale","mappingMessageObject:");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder = factory.newDocumentBuilder();  
		Document doc = (Document) builder.parse(new ByteArrayInputStream(message.toString().getBytes("UTF-8")));

		String name = doc.getDocumentElement().getTagName();
		Log.v("whale","name:"+name);
		if (name.equals("response")) {
			ReplyBody reply = new ReplyBody();
			reply.setType(doc.getElementsByTagName("type").item(0).getTextContent());
			reply.setSender(doc.getElementsByTagName("sender").item(0).getTextContent());
			reply.setReciever(doc.getElementsByTagName("reciever").item(0).getTextContent());
			reply.setTimestamp(Long.valueOf(doc.getElementsByTagName("timestamp").item(0).getTextContent()));
			NodeList items = doc.getElementsByTagName("data").item(0).getChildNodes();  
		     for (int i = 0; i < items.getLength(); i++) {  
		            Node node = items.item(i);  
		            reply.getData().put(node.getNodeName(), node.getTextContent());
		    }
			Log.v("whale","reply:");
			return reply;
		}
		if (name.equals("message")) {

			ServerMessage body = new ServerMessage();
			body.setType(doc.getElementsByTagName("type").item(0).getTextContent());
			//body.setContent(doc.getElementsByTagName("content").item(0).getTextContent());
			NodeList items = doc.getElementsByTagName("content").item(0).getChildNodes();  
		     for (int i = 0; i < items.getLength(); i++) {  
		            Node node = items.item(i);  
		            body.getData().put(node.getNodeName(), node.getTextContent());
		    }
			body.setSender(doc.getElementsByTagName("sender").item(0).getTextContent());
			body.setReceiver(doc.getElementsByTagName("receiver").item(0).getTextContent());
			body.setTimestamp(Long.valueOf(doc.getElementsByTagName("timestamp").item(0).getTextContent()));
			Log.v("whale","message:");
			return body;
		}
		if (name.equals("request")) {

			RequestBody body = new RequestBody();
			body.setType(doc.getElementsByTagName("type").item(0).getTextContent());
			body.setSender(doc.getElementsByTagName("sender").item(0).getTextContent());
			body.setReciever(doc.getElementsByTagName("reciever").item(0).getTextContent());
			body.setReciever(doc.getElementsByTagName("reciever").item(0).getTextContent());
			body.setTimestamp(Long.valueOf(doc.getElementsByTagName("timestamp").item(0).getTextContent()));
			Log.v("whale","request:");
			return body;
		}
		Log.v("whale","null:");

		return null;
	}


}
