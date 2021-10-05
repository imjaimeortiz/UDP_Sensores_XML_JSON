package practica3;

import java.io.File;
import java.util.Scanner;

public class MultithreadCommunication {
	
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.println("Indique el formato de los mensajes (XML/JSON)");
		String format = sc.nextLine();
		File file = new File("./data/broadcastMessages1.txt");
		Server s1 = new Server(format, file);
		File file2 = new File("./data/broadcastMessages2.txt");
		Server s2 = new Server(format, file2);
		File file3 = new File("./data/controlMmessages.txt");
		Client c1 = new Client(format, file3);
		
		s1.start();
		s2.start();
		c1.start();
		
		/*
		 * USO :
		 * Al ejecutarse se verá el ID del servidor.
		 * Para usarlo, hay que escribir el número de ID del servidor y hay 2 funcionalidades :
		 * 		1 - detener el envío de datos : ID stop
		 * 		2 - asignar nueva frecuencia de envío : ID frecuencia
		 * 		3 - cambiar JSON-XML
		 */

	}

}
