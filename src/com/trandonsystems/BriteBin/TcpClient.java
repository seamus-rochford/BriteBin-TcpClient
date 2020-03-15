package com.trandonsystems.BriteBin;

import java.net.*;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.trandonsystems.BriteBin.database.UnitDAL;

import java.io.*;

public class TcpClient {

	// initialize socket and input output streams
	private Socket socket = null;
	private OutputStream output = null;
	private InputStream input = null;

	static Logger log = Logger.getLogger(TcpClient.class);

	// constructor to put ip address and port
	public TcpClient(String address, int port) {
		// establish a connection
		try {
			socket = new Socket(address, port);
			if (socket.isConnected()) {
				System.out.println("Connected");
			}

			// sends output to the socket
			output = new DataOutputStream(socket.getOutputStream());
			//takes input from socket
			input = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException ex) {
			System.out.println(ex);
		} catch (IOException ioEx) {
			System.out.println(ioEx);
		}

		// send message
		try {
			byte[] data = {(byte)0x01, // Message Type
					(byte)0x28, // Bin Level
					(byte)0x48, // Bin Level BC
					(byte)0x00, // Flap openings 
					(byte)0x55,  
					(byte)0x44, // Battery Voltage
					(byte)0x66, // Temp.
					(byte)0x34, // NoCompactions
					(byte)0x5A, // Flags All set
					(byte)0x76, // Signal Strengths
					(byte)0x00, 
					(byte)0x00, 
					(byte)0x04, // Serial No length
					(byte)0xac, // Serial Number
					(byte)0x2b, 
					(byte)0xe7, 
					(byte)0xb6, 
					(byte)0x00, (byte)0x00, 
					(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};

			// sending data to server
			output.write(data);

			String req = Arrays.toString(data);
			//printing request to console
			System.out.println("Sent to server : " + req);

			// Receiving reply from server
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024];
			int noBytes = input.read(buffer);
			if (noBytes > 0) {
				// If reply from server
				baos.write(buffer, 0, noBytes);
				
				byte result[] = baos.toByteArray();

				String res = Arrays.toString(result);

				// printing reply to console
				System.out.println("Recieved from server : " + res);
			}
			
		} catch (IOException ex) {
			System.out.println(ex);
		}
		// }

		// close the connection
		try {
			// input.close();
			input.close();
			output.close();
			socket.close();
		} catch (IOException ex) {
			System.out.println(ex);
		}
		
		System.out.println("Connection terminated");		
	}
	
	public static void SaveMsg() {
		
		// This was to put messages into the database so when server was running it could send these back to the unit
		byte[] msg = {(byte)0x02, 
				(byte)0x4C, (byte)0x50, (byte)0x64, (byte)0x80, (byte)0x94, (byte)0xF0, (byte)0x78};
		
		int unitId = 1;
		int userId = 1;
		
		try {
			UnitDAL.saveMessage(unitId, msg, userId);			
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		
	}

	public static void main(String[] args) {
		
//		SaveMsg();
		
//		new TcpClient("127.0.0.1", 48999);
		new TcpClient("209.97.189.162", 48999);
		
	}

}
