package data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SPLCoachMessage {
	public static final int SPL_COACH_MESSAGE_STRUCT_VERSION = 1;
	public static final int SPL_COACH_MESSAGE_SIZE = 20;
	public static final long SPL_COACH_MESSAGE_RECEIVE_INTERVALL = 10000; //in ms
	public static final long SPL_COACH_MESSAGE_MIN_SEND_INTERVALL = 8000; //in ms
	public static final long SPL_COACH_MESSAGE_MAX_SEND_INTERVALL = 12000; //in ms
	public static final String SPL_COACH_PACKAGE_HEADER = "SPLC";
	public static final int SIZE = 4 //header size 
								+ 1 //byte for the team version
								+ 1 //team number
								+ SPL_COACH_MESSAGE_SIZE;
	
	public String header;          // header to identify the structure
    public byte version;            // version of the data structure
    public byte team;      // unique team number
    public byte[] message;     // what the coach says
    private long timestamp;	// when the message was created
    private long sendTime; // time in ms that shows when the message should be send to the team
    
    public SPLCoachMessage() {
    	sendTime = generateSendIntervallForSPLCoachMessage();
    	timestamp = System.currentTimeMillis();
    }
    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put(header.getBytes());
        buffer.put(version);
        buffer.put(team);
        buffer.put(message);
        
        return buffer.array();
    }
    
	public boolean fromByteArray(ByteBuffer buffer) {
		try {
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			byte[] header = new byte[4];
			buffer.get(header);
			this.header = new String(header);
			if (!this.header.equals(SPL_COACH_PACKAGE_HEADER)) {
				return false;
			} else {
				version = buffer.get();
				team = buffer.get();
				message = new byte[SPLCoachMessage.SPL_COACH_MESSAGE_SIZE];
				buffer.get(message);
				return true;
			}
		} catch (RuntimeException e) {
			return false;
		}
	}
	
	public long getRemainingTimeToSend() {
		long remainingTime = (sendTime - (System.currentTimeMillis() - timestamp)); 
		return (remainingTime > 0) ? remainingTime : 0;
	}
	private long generateSendIntervallForSPLCoachMessage () {
		return (long)Math.random()*(SPLCoachMessage.SPL_COACH_MESSAGE_MAX_SEND_INTERVALL-SPLCoachMessage.SPL_COACH_MESSAGE_MIN_SEND_INTERVALL) + SPLCoachMessage.SPL_COACH_MESSAGE_MIN_SEND_INTERVALL ;
	}
}