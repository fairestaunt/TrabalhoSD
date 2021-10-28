package servidor;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Servidor {
		ServerSocket escutador;
		public static Map<String, Turma> atuaisTurmasAbertas;
		private static DateTimeFormatter data;
		public static final int PORTA = 9696;

		public static void main(String[] args) {
			try {
				data = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");
				atuaisTurmasAbertas = new HashMap<String, Turma>();
				Servidor server = new Servidor();
				server.iniciatServidor();
			}
			catch (Exception e) {
				System.out.println("\r\n# Algo de estranho aconteceu:" + e.getMessage() + " #");
			}
		}

		public static String registraChamadaAluno(String turma, String matricula) {
			String turmaRegistrada = "0";
			Turma turmaAtual;
			if (atuaisTurmasAbertas.containsKey(turma)) {
				turmaAtual = atuaisTurmasAbertas.get(turma);
				turmaRegistrada = turma;
				if (!turmaAtual.getAlunos().contains(matricula))
					turmaAtual.addAluno(matricula);
				else
					return "# Não é possível confirmar presença duas vezes! #";
			}
			return (String.format("# O/A Aluno(a) %s registrou presença em: %s na turma: %s \n", matricula, data.format(LocalDateTime.now()), turmaRegistrada));
		}

		public static String recebeTurma(String turma) {
			String aux;
			if(!atuaisTurmasAbertas.containsKey(turma)) {
				atuaisTurmasAbertas.put(turma, new Turma(turma));
				aux = String.format("# Chamada da turma %s foi aberta em: %s #", turma, data.format(LocalDateTime.now()));
			}
			else {
				atuaisTurmasAbertas.remove(turma);
				aux = String.format("# Chamda da turma %s foi fechada em: %s #", turma, data.format(LocalDateTime.now()));
			}
			return aux;
		}

		public void iniciatServidor() throws IOException {
			escutador = new ServerSocket(PORTA);
			System.out.println("\r\n# O servidor foi iniciado #");
			connectionLoop();
		}

		public void connectionLoop() throws IOException {
			while (true) {
				Socket client = escutador.accept();
				System.out.println("\r\n# Cliente autenticado: " + client.getRemoteSocketAddress() + " #");
				new Thread(new Connection(client)).start();
			}
		}

	}