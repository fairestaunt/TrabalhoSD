package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable
	{
		private Socket socketPrincipal;
		private BufferedReader entrada;
		private PrintWriter saida;
		private String turma;

		public Connection(Socket cliente)
		{
			this.socketPrincipal = cliente;
		}

		@Override
		public void run()
		{
			try
			{
				this.entrada = new BufferedReader(new InputStreamReader(socketPrincipal.getInputStream()));
				this.saida = new PrintWriter(socketPrincipal.getOutputStream(), true);
				char tipoDeCliente = entrada.readLine().toUpperCase().charAt(0);
				System.out.printf("\r\n# Cliente: %s #\n", (tipoDeCliente == 'A' ? "aluno" : "professor"));

				if (tipoDeCliente == 'A')
				{
					clienteDoAluno();
				}
				else
				{
					clienteDoProfessor();
				}
			}
			catch (Exception ex)
			{
				System.out.println("\r\n# Ocorreu um erro: " + ex.getMessage() + " #");
			}
		}

		public void clienteDoProfessor() throws IOException
		{
			String turma = entrada.readLine();
			String fim = "false";
			ArrayList<String> alunos = new ArrayList<String>();

			if (Servidor.atuaisTurmasAbertas.containsKey(turma))
			{
				Turma turmaAtual = Servidor.atuaisTurmasAbertas.get(turma);
				alunos = turmaAtual.getAlunos();
				fim = "true";
			}

			String retorno = Servidor.recebeTurma(turma);
			saida.println(retorno);
			saida.println(fim);
			if (Boolean.parseBoolean(fim))
			{
				saida.println(alunos.size());
				for (String listaDeAlunos : alunos)
				{
					saida.println(listaDeAlunos);
				}
			}
		}

		public void clienteDoAluno() throws IOException
		{
			String mtr = entrada.readLine();
			turma = entrada.readLine();
			saida.println(Servidor.registraChamadaAluno(turma, mtr));
		}

	}
