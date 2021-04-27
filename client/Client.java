package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	static String prompt = "Ecrire un message : ";
	static String username;
	static StringBuilder buffer = new StringBuilder();
	static String ttyConfig;

	public static void main(String[] args) {
		String host;
		int port;
		if (args.length >= 2) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			host = "localhost";
			port = 8000;
		}
		try (var s = new Socket(host, port);
				var dis = new DataInputStream(s.getInputStream());
				var dos = new DataOutputStream(s.getOutputStream());
				var in = new Scanner(System.in)) {
			setTerminalToCBreak();
			System.out.print(
					"Entrez votre username pour commencer la discussion: ");
			// username = in.nextLine();
			read();
			username = buffer.toString();
			buffer.delete(0, buffer.length());
			dos.writeUTF(username);
			var listen = new ListenThread(dis);
			listen.start();
			var chat = new MessageThread(dos, in);
			chat.start();
			listen.join();
			System.out.println("Bye");
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		} finally {
			try {
				stty(ttyConfig.trim());
			} catch (Exception e) {
				System.err.println("Exception restoring tty config");
			}
		}
	}

	static void read() throws IOException, InterruptedException {
		boolean loop = true;
		while (loop) {
			if (System.in.available() > 0) {
				int c = System.in.read();
				// System.out.print(c);
				switch (c) {
					case 27:
						stty(ttyConfig.trim());
						System.exit(0);
						break;
					case 127:
						if (buffer.length() > 0) {
							System.out.print("\b \b");
							buffer.deleteCharAt(buffer.length() - 1);
						}
						break;
					case 10:
						loop = false;
						System.out.println();
						break;
					default:
						buffer.append((char)c);
						System.out.print((char)c);
				}
			}
		}
	}

	static void setTerminalToCBreak() throws IOException, InterruptedException {

		ttyConfig = stty("-g");

		// set the console to be character-buffered instead of line-buffered
		stty("-icanon min 1");

		// disable character echoing
		stty("-echo");
	}

	/**
	 *  Execute the stty command with the specified arguments
	 *  against the current active terminal.
	 */
	static String stty(final String args)
			throws IOException, InterruptedException {
			String cmd = "stty " + args + " < /dev/tty";

			return exec(new String[] {"sh", "-c", cmd});
	}

	/**
	 *  Execute the specified command and return the output
	 *  (both stdout and stderr).
	 */
	private static String exec(final String[] cmd)
			throws IOException, InterruptedException {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();

			Process p = Runtime.getRuntime().exec(cmd);
			int c;
			InputStream in = p.getInputStream();

			while ((c = in.read()) != -1) {
				bout.write(c);
			}

			in = p.getErrorStream();

			while ((c = in.read()) != -1) {
				bout.write(c);
			}

			p.waitFor();

			String result = new String(bout.toByteArray());
			return result;
	}
}
