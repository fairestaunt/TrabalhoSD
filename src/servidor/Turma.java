package servidor;

import java.util.ArrayList;

public class Turma
	{
		private String numeroTurma;
		private ArrayList<String> listaAlunos;

		public Turma(String turma)
		{
			this.numeroTurma = turma;
			listaAlunos = new ArrayList<String>();
		}

		public String getnumeroTurma()
		{
			return numeroTurma;
		}

		public void setnumeroTurma(String numTurma)
		{
			this.numeroTurma = numTurma;
		}

		public ArrayList<String> getAlunos()
		{
			return listaAlunos;
		}

		public void addAluno(String aluno)
		{
			listaAlunos.add(aluno);
		}
		public void removeAluno(String aluno)
		{
			listaAlunos.remove(aluno);
		}

	}
