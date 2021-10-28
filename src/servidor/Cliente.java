package servidor;

import java.io.*;
import java.net.*;
import java.util.*;

public class Cliente {
		private static Scanner scaneador;
		private Socket socketDoClient = null;
		private BufferedReader entrada;
		private PrintWriter saida;

		public static void main(String[] args) {
			char tipoDeCliente;
			Cliente cliente = null;
			try {
				scaneador = new Scanner(System.in);
				while (true) {
					System.out.print("\r\n# Logue no cliente | Digite 'A' para Aluno ou 'P' para Professor: ");
					tipoDeCliente = scaneador.nextLine().toUpperCase().charAt(0);
					if (tipoDeCliente == 'A' || tipoDeCliente == 'P')
						break;
				}

				cliente = new Cliente();
				cliente.iniciarCliente(tipoDeCliente);
			}
			catch (Exception ex) {
				System.out.println("\r\n # Não foi possível iniciar o cliente: " + ex.getMessage() + " #");
			}
			finally {
				cliente.closeSocket();
				scaneador.close();
				System.out.println("\r\n# O cliente foi desconectado #");
			}
		}

		public void iniciarCliente(char clientType) throws UnknownHostException, IOException {

			socketDoClient = new Socket("127.0.0.1", 9696);
			System.out.printf("\r\n# Cliente conectou como: %s\n", (clientType == 'A' ? "aluno" : "professor") + " #");
			this.entrada = new BufferedReader(new InputStreamReader(socketDoClient.getInputStream()));
			this.saida = new PrintWriter(socketDoClient.getOutputStream(), true);
			saida.println(clientType);
			if (clientType == 'A') {
				clienteDoAluno();
			}
			else {
				clienteDoProfessor();
			}
		}

		public void clienteDoProfessor() throws IOException {
			System.out.print("\r\n# Por favor, digite o código da turma para abrir ou fechar a chamada: ");
			saida.println(scaneador.nextLine());
			System.out.println(entrada.readLine());
			boolean isEnd = Boolean.parseBoolean(entrada.readLine());
			if (isEnd) {
				System.out.println("\r\n# Os alunos que responderam a chamada são:#");
				int size = Integer.parseInt(entrada.readLine());
				for (int i = 0; i < size; i++) {
					System.out.println(entrada.readLine());
				}
			}
		}

		public void clienteDoAluno() throws IOException {
			System.out.print("\r\n# Digite a sua matrícula: ");
			saida.println(scaneador.nextLine());
			System.out.print("\r\n# Digite o código da sua turma: ");
			saida.println(scaneador.nextLine());

			System.out.println(entrada.readLine());
		}

		public void closeSocket() {
			if (socketDoClient != null && !socketDoClient.isClosed()) {
				try {
					socketDoClient.close();
				}
				catch (IOException ex) {
					System.out.println("\r\n# Ocorreu um erro ao fechar o socket: " + ex.getMessage() + " #");
				}
			}
		}
	}
