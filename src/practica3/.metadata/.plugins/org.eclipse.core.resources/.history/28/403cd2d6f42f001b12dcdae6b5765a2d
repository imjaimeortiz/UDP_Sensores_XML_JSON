package practica3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class Client extends Thread {

	private static final int PORT_DEFAULT = 6666;
	private DatagramSocket socket;
	private InetAddress serverAddress;
	private boolean running;
	private HashMap<Integer, Integer> servidores;
	private SchemaValidation schValidation;
	private Translator tBroadcast;
	private Translator tControl;
	private Message controlMsg;
	private String format;
	private BufferedWriter bw;

	public Client(String format, File file) {
		try {
			this.socket = new DatagramSocket(PORT_DEFAULT);
			new ReadCommands(this).start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		this.servidores = new HashMap<Integer, Integer>();
		this.schValidation = new SchemaValidation();
		this.tBroadcast = new TranslatorBroadcast();
		this.tControl = new TranslatorControl();
		this.format = format;
	}

	// Se recibe lo que el servidor envía y se guardan los datos de los servidores
	// para el posterior envío
	// Se recibe lo que el servidor envía y se guardan los datos de los servidores para el posterior envío
		@Override
		public void run() {
			running = true;
			while (running) {
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				try {
					socket.receive(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String received = new String(packet.getData(), 0, packet.getLength());
				serverAddress = packet.getAddress();
				// Deserialización
				if (received.contains("<")) {
					if (schValidation.validateBroadcastXML(received)) {
						BroadcastMessage bMsg = (BroadcastMessage) tBroadcast.XMLtoJava(received);
						bMsg.showMessage("XML");
						this.servidores.put(bMsg.getId(), packet.getPort());
					}
				} else {
					BroadcastMessage bMsg = (BroadcastMessage) tBroadcast.JsonToJava(received);
					bMsg.showMessage("JSON");
					this.servidores.put(bMsg.getId(), packet.getPort());
				}
			}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket.close();
		}

	// Procesamiento anterior al envío de la trama unicast en función de los
	// distintos mensajes que puede enviar el emisor
	public void processUnicast(String line) {
		String[] lines = line.split(" ");
		if (lines[0].equals("client")) {
			if (lines[1].equals("XML") || lines[1].equals("JSON")) {
				this.format = lines[1];
				System.out.println("Cambiando el formato de envío del cliente a " + lines[1]);	
			}
			else {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				running = false;
			}
		}
		else {
			int id = 0;
			try {
				id = Integer.parseInt(lines[0]);
			} catch (Exception e) {
				System.out.println("<Cliente> Operación no soportada");
			}
			String op = lines[1];
			// Esto no tiene gran funcionalidad, es para dar información al usuario sobre la
			// aplicación
			if (servidores.keySet().contains(id)) {
				if (op.equals("stop"))
					System.out.println("<Cliente><" + "Deteniendo el servidor " + id + ">");
				else if (op.equals("XML") || op.equals("JSON"))
					System.out.println("<Cliente><" + "Cambiando a formato " + op + " el envío del servidor " + id + ">");
				else if (isNumeric(op))
					System.out.println("<Cliente><" + "Cambiando a " + op + "Hz la frecuencia de envío del servidor " + id + ">");
				this.controlMsg = new ControlMessage(op);
				String control = new String();
				// Serialización
				if (format.equals("XML"))
					control = tControl.javaToXML(controlMsg);
				else 
					control = tControl.javaToJson(controlMsg);
				try {
					bw.write(control + "\n\n");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.send(id, control.getBytes());
			} else
				System.out.println("<Cliente>No se pudo conectar con el ServidorId=" + id);

		}
	}

	// Envío de la trama unicast
	private void send(int id, byte[] buf) {
		DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, servidores.get(id));
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isNumeric(String cadena) {
		boolean resultado;
		try {
			Integer.parseInt(cadena);
			resultado = true;
		} catch (NumberFormatException excepcion) {
			resultado = false;
		}
		return resultado;
	}
}
