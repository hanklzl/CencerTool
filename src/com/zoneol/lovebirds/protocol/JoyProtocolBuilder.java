package com.zoneol.lovebirds.protocol;


import com.zoneol.lovebirds.protocol.JoyProtocol.Protocol;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpControlAnswer;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpControlAsk;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpLoginReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpPickMatchReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpQueryMatcherStatusReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpQueryUserInfoReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpQueryUserStatusReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpReleaseMatchReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpSendControl;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpSendMessage;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpSendPresent;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpUpdateUserInfoReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpUpdateUserPswReq;
import com.zoneol.lovebirds.protocol.JoyProtocol.TcpUpdateUserStatusReq;

public class JoyProtocolBuilder {
    //Protocol Field Number Constants
	public static final int TCPLOGINREQ_FIELD_NUMBER = 2;
	public static final int TCPLOGINRES_FIELD_NUMBER = 3;
	public static final int TARGETUSERRELEASEMACH_FIELD_NUMBER = 4;
	public static final int MATCHERONOFFLINE_FIELD_NUMBER = 5;
	public static final int TCPPICKMATCHREQ_FIELD_NUMBER = 6;
	public static final int TCPPICKMATCHRES_FIELD_NUMBER = 7;
	public static final int TCPRELEASEMATCHREQ_FIELD_NUMBER = 8;
	public static final int TCPSENDMESSAGE_FIELD_NUMBER = 10;  
	public static final int TCPSENDCONTROL_FIELD_NUMBER = 11;
	public static final int TCPSYSMESSAGE_FIELD_NUMBER = 12;
	public static final int TCPQUERYMATCHERSTATUSREQ_FIELD_NUMBER = 13;
	public static final int TCPQUERYMATCHERSTATUSRES_FIELD_NUMBER = 14;
	public static final int HEARTBEATREQ_FIELD_NUMBER = 15;
	public static final int TCPCONTROLASK_FIELD_NUMBER = 17;
	public static final int TCPCONTROLANSWER_FIELD_NUMBER = 18;
	public static final int TCPUPDATEUSERINFOREQ_FIELD_NUMBER = 19;
	public static final int TCPUPDATEUSERINFORES_FIELD_NUMBER = 20;
	public static final int TCPQUERYUSERINFOREQ_FIELD_NUMBER = 22;
	public static final int TCPQUERYUSERINFORES_FIELD_NUMBER = 23;
	public static final int TCPUPDATEUSERSTATUSREQ_FIELD_NUMBER = 24;
	public static final int TCPQUERYUSERSTATUSREQ_FIELD_NUMBER = 25;
	public static final int TCPQUERYUSERSTATUSRES_FIELD_NUMBER = 26;
	public static final int TCPUPDATEUSERPSWRES_FIELD_NUMBER = 28;
	public static final int TCPSENDPRESENT_FIELD_NUMBER = 29 ;

	public static JoyProtocol.Protocol buildTcpLoginReq(long id, long key) {
		TcpLoginReq req = new TcpLoginReq();
		Protocol proc = new Protocol();

		req.uid = id;
		req.sessionKey = key;
		
		proc.fieldNum = TCPLOGINREQ_FIELD_NUMBER;
		proc.tcpLoginReq = req;

		return proc;
	}
	
	//public static JoyProtocol.Protocol buildTcpPickMatchReq(JoyProtocol.TcpPickMatchReq.TargetType type) {
	public static JoyProtocol.Protocol buildTcpPickMatchReq(int type) {
		TcpPickMatchReq req = new TcpPickMatchReq();
		Protocol proc = new Protocol();
		
		req.targetType = type;
		proc.fieldNum = TCPPICKMATCHREQ_FIELD_NUMBER;
		proc.tcpPickMatchReq = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpSendMessageText(long from, 
			long to, long msgId, String text , int type) {
		TcpSendMessage req = new TcpSendMessage();
		Protocol proc = new Protocol();
		
		req.uidFrom = from;
		req.uidTo = to;
		req.msgType = TcpSendMessage.MsgType.TEXT;
		req.setMsgText(text);
		req.setMsgId(msgId);
		req.setFileUrl(type + "") ;
		
		proc.fieldNum = TCPSENDMESSAGE_FIELD_NUMBER;
		proc.tcpSendMessage = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpSendMessageAck(long from, long to, long msgId, 
			int ack) {
		TcpSendMessage req = new TcpSendMessage();
		Protocol proc = new Protocol();
		
		req.uidFrom = from;
		req.uidTo = to;
		req.msgType = TcpSendMessage.MsgType.ACK;
		req.setMsgId(msgId);
		req.setAckType(ack);

		proc.fieldNum = TCPSENDMESSAGE_FIELD_NUMBER;
		proc.tcpSendMessage = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpSendMessageAudio(long from, long to, 
			long msgId, String url, int time) {
		
		TcpSendMessage req = new TcpSendMessage();
		Protocol proc = new Protocol();
		req.uidFrom = from;
		req.uidTo = to;
		req.setMsgId(msgId);
		req.msgType = TcpSendMessage.MsgType.VOICE;
		req.setFileUrl(url);
		req.setMsgText(Integer.toString(time));
		
		proc.fieldNum = TCPSENDMESSAGE_FIELD_NUMBER;
		proc.tcpSendMessage = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpSendMessageImage(long from, long to, 
			long msgId, String url) {
		TcpSendMessage req = new TcpSendMessage();
		Protocol proc = new Protocol();
		
		req.uidFrom = from;
		req.uidTo = to;
		req.setMsgId(msgId);
		req.msgType = TcpSendMessage.MsgType.PICTURE;
		req.setFileUrl(url);
		
		proc.fieldNum = TCPSENDMESSAGE_FIELD_NUMBER;
		proc.tcpSendMessage = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpSendControl(long from, long to, byte[] data) {
		
		TcpSendControl req = new TcpSendControl();
		Protocol proc = new Protocol();
		
		req.uidFrom = from;
		req.uidTo = to;
		req.setData(data);
		
		proc.fieldNum = TCPSENDCONTROL_FIELD_NUMBER;
		proc.tcpSendControl = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpReleaseMatchReq(long paire) {
		Protocol proc = new Protocol();
		TcpReleaseMatchReq req = new TcpReleaseMatchReq();
		
		req.targetUid = paire;
		proc.fieldNum = TCPRELEASEMATCHREQ_FIELD_NUMBER;
		proc.tcpReleaseMatchReq = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpHeartBeat() {
		Protocol proc = new Protocol();
		proc.fieldNum = HEARTBEATREQ_FIELD_NUMBER;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpQueryMatcherStatusReq(long id, long key) {
		TcpQueryMatcherStatusReq req = new TcpQueryMatcherStatusReq();
		Protocol proc = new Protocol();
		
		req.userId = id;
		req.sessionKey = key;
		proc.fieldNum = TCPQUERYMATCHERSTATUSREQ_FIELD_NUMBER;
		proc.tcpQueryMatcherStatusReq = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpUpdateUserInfoReq(JoyProtocol.User user) {
		TcpUpdateUserInfoReq req = new TcpUpdateUserInfoReq();
		Protocol proc = new Protocol();
		
		req.newUserInfo = user;
		proc.fieldNum = TCPUPDATEUSERINFOREQ_FIELD_NUMBER;
		proc.tcpUpdateUserInfoReq = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpControlAsk(long from, long to, int cmd, int gameIdx) {
		TcpControlAsk req = new TcpControlAsk();
		Protocol proc = new Protocol();
		
		req.uidAsker = from;
		req.uidTarget = to;
		req.setCmdCode(cmd);
		req.setDeviceId(gameIdx);
		
		proc.fieldNum = TCPCONTROLASK_FIELD_NUMBER;
		proc.tcpControlAsk = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpControlAnswer(long from, long to, int gameIdx, 
			int result) {
		TcpControlAnswer req = new TcpControlAnswer();
		Protocol proc = new Protocol();
		
		req.uidResponse = from;
		req.uidAsker = to;
		req.resultCode = result;
		
		proc.fieldNum = TCPCONTROLANSWER_FIELD_NUMBER;
		proc.tcpControlAnswer = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpQueryUserInfoReq(long id , long update) {
		TcpQueryUserInfoReq req = new TcpQueryUserInfoReq();
		Protocol proc = new Protocol();

		req.setUidTarget(id);
		req.setLastUpdate(update);
		proc.fieldNum = TCPQUERYUSERINFOREQ_FIELD_NUMBER;
		proc.tcpQueryUserInfoReq = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpQueryUserStatusReq(long id){
		TcpQueryUserStatusReq req = new TcpQueryUserStatusReq();
		Protocol proc = new Protocol();
		
		req.uidTarget = id;
		
		proc.fieldNum = TCPQUERYUSERSTATUSREQ_FIELD_NUMBER;
		proc.tcpQueryUserStatusReq = req;
		
		return proc;
	}
	
	public static JoyProtocol.Protocol buildTcpUpdateUserStatusReq(long userStatus , long deviceStatus){
		TcpUpdateUserStatusReq req = new TcpUpdateUserStatusReq();
		Protocol proc = new Protocol();
		
		req.deviceStatus = deviceStatus;
		req.userStatus = userStatus;
		proc.fieldNum = TCPUPDATEUSERSTATUSREQ_FIELD_NUMBER;
		proc.tcpUpdateUserStatusReq = req;
		
		return proc;
	}

	public static Protocol buildTcpUpdateUserPswReq(long userId, String oldPsw,
			String newPsw) {
		TcpUpdateUserPswReq req = new TcpUpdateUserPswReq();
		Protocol proc = new Protocol();
		req.uId = userId;
		req.oldPsw = oldPsw;
		req.newPsw = newPsw;
		proc.fieldNum = 27;
		proc.tcpUpdateUserPswReq = req;
		
		return proc;
	}
	
	public static Protocol buildTcpSendPresent(long uidFrom , long uidTo , int presentType , int balance , String msgText){
		TcpSendPresent req = new TcpSendPresent();
		Protocol proc = new Protocol();
		req.uidFrom = uidFrom;
		req.uidTo = uidTo;
		req.presentType = presentType;
		req.setBalance(balance) ;
		req.setMsgText(msgText) ;
		
		proc.fieldNum = TCPSENDPRESENT_FIELD_NUMBER;
		proc.tcpSendPresent = req;
		
		return proc;
	}
	
}
