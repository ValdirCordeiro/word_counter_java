import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Classes.Palavra;

@SuppressWarnings("serial")
public class telaPrincipal extends JFrame{
	JLabel tf_texto = new JLabel("Local dos Textos:");
	JTextField ta_entrada = new JTextField();
	JButton bt_selecionar = new JButton("Abrir");
	JButton bt_carregar = new JButton("Carregar");
	public List<Palavra> palavras = new ArrayList<Palavra>();
	public List<Palavra> palavrasSelecionadas = new ArrayList<Palavra>();
	public Boolean NoTexto = true;
	JTextArea todasAsPalavras = new JTextArea();
	JScrollPane scroll = new JScrollPane();
	JScrollPane scroll2 = new JScrollPane();
	String[] colunas = new String[]{"Palavras","Vezes nos textos"};
	String[][] dados = new String[][]{
	    {"",""}
	};
	DefaultTableModel model = new DefaultTableModel(dados , colunas );
	DefaultTableModel model2 = new DefaultTableModel(dados , colunas );
	JTable tabela = new JTable();
	JTable tabela2 = new JTable();
	Double MediaValor = 0.0;
	
	public telaPrincipal(){
		setLayout(new BorderLayout());
		
		carregaComponetes();
		
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void carregaComponetes(){
		bt_selecionar.addActionListener(caminhoTextosFonte);
		bt_carregar.addActionListener(carregar);
		
		JPanel painel1 = new JPanel();
		JPanel painel2 = new JPanel();
		painel1.setLayout(new GridLayout(2,1));
		painel2.setLayout(new GridLayout(1,2));
		painel1.add(tf_texto);
		painel1.add(ta_entrada);
		painel2.add(bt_selecionar);
		painel2.add(bt_carregar);
		
		JPanel painel3 = new JPanel();
		painel3.setLayout(new GridLayout(1,2));
		
		painel3.add(painel1);
		painel3.add(painel2);
		add("North",painel3);
		
		//-----------------------tabela		
		tabela.setModel(model);
		tabela2.setModel(model2);
				
		scroll.setViewportView(tabela);		
		scroll.setName("Todas as Palavras");	
		
		scroll2.setViewportView(tabela2);
		scroll2.setName("Palavras Selecionadas");	
		//-----------------------fim
		
		JPanel textos = new JPanel(new GridLayout(1,2));		
		textos.add(scroll);
		textos.add(scroll2);
		textos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Todas as Palavas -------------------------------------------------------------------- Palavras selecionadas",
				javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		
		add("West", new JPanel());
		add("East", new JPanel());
		add("Center", textos);
		add("South", new JPanel());
	}
	
	public void SeparaPalavras(String Fonte) {
		String[] vetPalavras = Fonte.replace("\n", "").split(" ");//trata o parágrafo (\n) e separa por espaços
		
		for(int i = 0; i < vetPalavras.length; i++) {
			//retira os caracteres desnecessários das palavras
			vetPalavras[i] = vetPalavras[i].replace("!", "").replace(".", "").replace("?", "").replace(";", "").replace(",","").replace("\"", "").replace("'","").replace("(","").replace(")", "").replace("/", "");
		
			if (palavras.isEmpty()) { //adiciona a primeira palavra na lista, quando a lista está vazia;
				palavras.add(new Palavra(vetPalavras[i]));
			
			} else {
				NoTexto = false;
				for(int j = 0; j < palavras.size(); j++) {  //percorre a Lista de palavras e verifica se existe a mesma palavra no texto
					if ( palavras.get(j).palavra.equalsIgnoreCase(vetPalavras[i])) {
						palavras.get(j).valor++;
						palavras.get(j).vezesNoTexto++;
						NoTexto = true;
					}
				}				
			}
			
			if(NoTexto == false) {
				palavras.add(new Palavra(vetPalavras[i]));
			}
		}
	}
	
	public void SelecionaPalavras() {
		int escolha = 0;
		double aux = 0;
		for(int i = 0; i<palavras.size() -1; i++) {
			
			if((palavras.get(i+1).valor - MediaValor) < (palavras.get(i).valor - MediaValor)) {
				
				if(aux == 0) {
					aux = palavras.get(i+1).valor - MediaValor;
					escolha = i + 1;
				} else if (aux > palavras.get(i+1).valor - MediaValor && MediaValor - palavras.get(i+1).valor < aux) {
					aux = palavras.get(i+1).valor - MediaValor;
					escolha = i + 1;
				}				
			}
		}

		int aux2 = 0;
		for(int i = 0; i<=25; i++) {
			if(i < escolha) {
				palavrasSelecionadas.add(palavras.get(escolha - i));
			} else {
				palavrasSelecionadas.add(palavras.get(escolha +25 + aux2));
				aux2++;
			}
		}
		
		for(int i = 1; i<=24; i++) {
			if(i + escolha < palavras.size()) {
				palavrasSelecionadas.add(palavras.get(escolha + i));
			} else if(escolha -25 -i < palavras.size()) {
				palavrasSelecionadas.add(palavras.get(escolha -25 -i));
			}
		}
	}
	
	public void CalculaMediaPalavras() {
		double somaTotal = 0;
		for(int i=0; i < palavras.size(); i++) {
			somaTotal = somaTotal + palavras.get(i).valor;
		}
		
		MediaValor = somaTotal/palavras.size();
	}
	
	public void separacaoEmLote() throws IOException {
		File diretorio = new File(ta_entrada.getText());  
		File[] arquivos = diretorio.listFiles();
				
		
		for(int i=0; i<arquivos.length;i++) {
			//----------------------------------------------------Abrindo texto fonte
			String caminhoArquivo = arquivos[i].getAbsolutePath();
							
			FileReader reader;
			
			reader = new FileReader(caminhoArquivo);
			
	    	BufferedReader buffer = new BufferedReader(reader);
	    	String linha;
	    	StringBuffer Sbuffer = new StringBuffer();
	    	 
	    	while((linha = buffer.readLine()) != null) {
	    		Sbuffer.append(linha).append("\n");
	    	 }
	    	reader.close();
	    	 
	    	SeparaPalavras(Sbuffer.toString());    	
	    	
		}
		
		Collections.sort(palavras);
				
		DefaultTableModel model =  (DefaultTableModel) tabela.getModel();
		
		for(int i=0; i<palavras.size(); i++){
			 
			String[] linha = new String[]{palavras.get(i).palavra, " " + palavras.get(i).vezesNoTexto};
			model.addRow(linha);
			
		}
						
	}
	
	ActionListener carregar = new ActionListener() {		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				separacaoEmLote();
				CalculaMediaPalavras();
				SelecionaPalavras();
				
				Collections.sort(palavrasSelecionadas);
				
				DefaultTableModel model =  (DefaultTableModel) tabela2.getModel();
				
				for(int i=0; i<palavrasSelecionadas.size(); i++){
					 
					String[] linha = new String[]{palavrasSelecionadas.get(i).palavra, " " + palavrasSelecionadas.get(i).vezesNoTexto};
					model.addRow(linha);					
				}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
	};
	
	ActionListener caminhoTextosFonte = new ActionListener() {		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			 JFileChooser chooser = new JFileChooser();
		     chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		     int retorno = chooser.showOpenDialog(null);
		     if (retorno == JFileChooser.APPROVE_OPTION) {
		    	 ta_entrada.setText(chooser.getSelectedFile()+"");
		     }
		}
	};
}
