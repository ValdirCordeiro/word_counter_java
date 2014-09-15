package Classes;
public class Palavra implements Comparable<Palavra>{
	public String palavra;
	public int tamanho;
	public int vezesNoTexto = 1;
	public double valor = 1;
	
	public Palavra(String palavras) {
		palavra = palavras;
		tamanho = palavra.length();
	}

	@Override
	public int compareTo(Palavra o) { // Fun��o para permitir a a ordena��o da lista pelo valor da palavra
		if(this.valor > o.valor){
            return -1;
        } else if(this.valor < o.valor){
            return 1;
        }
      return 0;
	}

}