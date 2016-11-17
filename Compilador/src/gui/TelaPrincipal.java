package gui;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import static main.ArquivoUtil.*;
import static main.AnalisadorSintatico.*;

public class TelaPrincipal extends JFrame {

	private static TelaPrincipal frame = new TelaPrincipal();
	private static JPanel contentPane;

	private static JMenuBar menuBar;
	private static JMenuItem botaoAbrirArquivo;
	private static JMenuItem botaoSalvarAlteracoes;
	private static JMenuItem botaoCompilar;

	private static JTextArea textAreaErro;
	private static JTextArea textAreaEditor;
	private static JLabel lblErro;
	private static JLabel lblEditor;
	private static JScrollPane scrollPaneErro;
	private static JScrollPane scrollPaneEditor;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setResizable(false);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 686, 539);

		lblEditor = new JLabel("Editor:");
		lblErro = new JLabel("Console:");

		scrollPaneEditor = new JScrollPane();
		scrollPaneErro = new JScrollPane();

		inicializaBarraMenu();
		inicializaEditor();
		inicializaErro();
		inicializaLayout(lblEditor, lblErro, scrollPaneEditor, scrollPaneErro);
	}

	public static void procurarArquivo(JFrame janela) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(janela);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			pathArquivo = selectedFile.getAbsolutePath();
		}
	}

	private void inicializaEditor() {
		textAreaErro = new JTextArea();
		scrollPaneErro.setViewportView(textAreaErro);
		textAreaErro.setEditable(false);
	}

	private void inicializaErro() {
		textAreaEditor = new JTextArea();
		scrollPaneEditor.setViewportView(textAreaEditor);
	}

	private void inicializaBarraMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		inicializaMenuAbrirArquivo();
		inicializaMenuSalvarAlteracoes();
		inicializaMenuCompilador();
	}

	private void inicializaMenuAbrirArquivo() {
		botaoAbrirArquivo = new JMenuItem("Abrir arquivo...");
		menuBar.add(botaoAbrirArquivo);
		botaoAbrirArquivo.addActionListener(event -> {
			procurarArquivo(frame);
			try {
				preencheEditor(exibeArquivoFonte());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	private void preencheEditor(String texto) {
		textAreaEditor.setText(texto);
	}

	private void inicializaMenuSalvarAlteracoes() {
		botaoSalvarAlteracoes = new JMenuItem("Salvar altera\u00E7\u00F5es...");
		menuBar.add(botaoSalvarAlteracoes);
		botaoSalvarAlteracoes.addActionListener(event -> {
			try {
				salvaArquivo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void salvaArquivo() throws Exception {
		String[] conteudoVetor = textAreaEditor.getText().split("\\n");
		salvaArquivoFonte(conteudoVetor);
	}

	private void inicializaMenuCompilador() {
		botaoCompilar = new JMenuItem("Compilar");
		menuBar.add(botaoCompilar);
		botaoCompilar.addActionListener(event -> {
			try {
				analisadorSintatico();
			} catch (Exception e) {
				if (e.getMessage().equals("Chegou ao fim do arquivo. Não há mais tokens.")) {
					textAreaErro.setText(
							"O arquivo chegou ao fim e não foi encontrada a palavra 'fim' ou um ponto final após a palavra 'fim'.");
				} else {
					textAreaErro.setText(e.getMessage());
				}

			}
		});
	}

	private void inicializaLayout(JLabel lblEditor, JLabel lblErro, JScrollPane scrollPaneEditor,
			JScrollPane scrollPaneErro) {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(55)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPaneEditor, GroupLayout.PREFERRED_SIZE, 524, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPaneErro, GroupLayout.PREFERRED_SIZE, 526, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblErro).addComponent(lblEditor))
				.addContainerGap(79, Short.MAX_VALUE)));
		gl_contentPane
				.setVerticalGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(
										gl_contentPane.createSequentialGroup().addGap(14).addComponent(lblEditor)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(scrollPaneEditor, GroupLayout.DEFAULT_SIZE, 338,
														Short.MAX_VALUE)
												.addGap(18).addComponent(lblErro)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(scrollPaneErro, GroupLayout.PREFERRED_SIZE, 45,
														GroupLayout.PREFERRED_SIZE)
												.addContainerGap()));
		contentPane.setLayout(gl_contentPane);
	}
}
