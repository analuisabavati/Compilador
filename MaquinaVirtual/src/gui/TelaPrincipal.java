package gui;

import static main.MaquinaVirtual.*;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TelaPrincipal extends JFrame {

	/*
	 * TODO: getJanelaEntrada(); BreakPoints
	 */

	private static final long serialVersionUID = 8206910973434962454L;
	private static JTable tableInstrucoes = new JTable();
	private static JTable tableDados = new JTable();
	private static TelaPrincipal frame = new TelaPrincipal();
	private JPanel contentPane;
	private JLabel lblJanelaEntrada;
	private JLabel lblJanelaDeSaida;
	private JLabel lblBreakPoints;
	private JLabel lblInstruesASeremExecutadas;
	private JLabel lblContedoDaPilha;
	private static JScrollPane scrollJanelaSaida;
	private JScrollPane scrollTableDados;
	private JScrollPane scrollJanelaBreakPoints;
	private JScrollPane scrollTableInstrucoes;
	private JScrollPane scrollJanelaEntrada;
	private static JTextArea janelaSaida;
	private static JTextArea janelaEntrada;
	private static JTextArea janelaBreakPoints;

	/**
	 * Launch the application.
	 */
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
		setBounds(100, 100, 708, 626);

		// Jscroll
		scrollTableInstrucoes = new JScrollPane();
		scrollTableDados = new JScrollPane();
		scrollJanelaEntrada = new JScrollPane();
		scrollJanelaSaida = new JScrollPane();
		scrollJanelaBreakPoints = new JScrollPane();

		// Labels
		lblInstruesASeremExecutadas = new JLabel("Instru\u00E7\u00F5es a serem executadas pela VM");
		lblJanelaEntrada = new JLabel("Janela de Entrada");
		lblJanelaDeSaida = new JLabel("Janela de Saida");
		lblBreakPoints = new JLabel("Break Points");
		lblContedoDaPilha = new JLabel("Conte\u00FAdo da Pilha");

		inicializaContentPane();
		inicializaBarraMenu(scrollTableInstrucoes);
		inicializaJanelaEntrada(scrollJanelaEntrada);
		inicializaTabelaDados(scrollTableDados);
		inicializaTabelaInstrucoes(scrollTableInstrucoes);
		inicializaJanelaSaida(scrollJanelaSaida);
		inicializaJanelaBreakPoints(scrollJanelaBreakPoints);

		inicializaLayout(scrollTableInstrucoes, scrollTableDados, scrollJanelaEntrada, scrollJanelaSaida,
				scrollJanelaBreakPoints, lblInstruesASeremExecutadas, lblJanelaEntrada, lblJanelaDeSaida,
				lblBreakPoints, lblContedoDaPilha);
		janelaBreakPoints = new JTextArea();
		scrollJanelaBreakPoints.setViewportView(janelaBreakPoints);
		janelaBreakPoints.setEditable(false);

	}

	private void inicializaContentPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	}

	private void inicializaBarraMenu(JScrollPane scrollTableInstrucoes) {
		JMenuBar menuBar = montaBarraMenu();
		inicializaBotaoAbrirArquivo(scrollTableInstrucoes, menuBar);
		inicializaBotaoExecutar(menuBar);
		inicializaBotaoContinuar(menuBar);
	}

	private void inicializaBotaoContinuar(JMenuBar menuBar) {
		JMenuItem menuContinuar = new JMenuItem("Continuar");
		menuBar.add(menuContinuar);
		menuContinuar.addActionListener(event -> setBotaoContinuarPressionado(true));
	}

	private void inicializaBotaoExecutar(JMenuBar menuBar) {
		JMenuItem menuExecutar = new JMenuItem("Executar");
		menuBar.add(menuExecutar);
		menuExecutar.addActionListener(event -> chamaExecutaInstrucoes(scrollTableDados));
	}

	private static void chamaExecutaInstrucoes(JScrollPane scrollTableDados) {
		try {
			janelaEntrada.setText("");
			janelaSaida.setText("");
			tableDados.setModel(executaInstrucoes());
			scrollTableDados.setViewportView(tableDados);
			frame.getContentPane().add(scrollTableDados);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static String getValorEntrada() {

		String valor = null;
		while (valor == null || valor.equals("")) {
			valor = JOptionPane.showInputDialog("Digite um valor de entrada: ");
			if (valor == null || valor.equals("")) {
				valor = JOptionPane.showInputDialog("Digite um valor de entrada: ");
			}
		}
		
		janelaEntrada.setText(valor+"\n"+janelaEntrada.getText());

		return valor;
	}

	private void inicializaJanelaEntrada(JScrollPane scrollJanelaEntrada) {
		janelaEntrada = new JTextArea();

		scrollJanelaEntrada.setViewportView(janelaEntrada);
	}

	private void inicializaJanelaSaida(JScrollPane scrollJanelaSaida) {
		janelaSaida = new JTextArea();
		janelaSaida.setEditable(false);
		scrollJanelaSaida.setViewportView(janelaSaida);
	}

	private void inicializaJanelaBreakPoints(JScrollPane scrollBreakPoints) {
	}

	private JMenuBar montaBarraMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		return menuBar;
	}

	private void inicializaBotaoAbrirArquivo(JScrollPane scrollTableInstrucoes, JMenuBar menuBar) {
		JMenuItem menuAbrirArquivo = new JMenuItem("Abrir Arquivo...");
		menuBar.add(menuAbrirArquivo);
		menuAbrirArquivo.addActionListener(event -> {
			procurarArquivo(frame);
			try {
				preencheTabelaInstrucoes(scrollTableInstrucoes);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
	}

	private void inicializaTabelaInstrucoes(JScrollPane scrollTableInstrucoes) {
		tableInstrucoes.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Linha", "Label", "Mnemonico", "Parametro 1", "Parametro 2" }));
		scrollTableInstrucoes.setViewportView(tableInstrucoes);
	}

	private void inicializaTabelaDados(JScrollPane scrollTableDados) {
		tableDados.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Endereço ", "Valor" }));
		scrollTableDados.setViewportView(tableDados);
	}

	private void inicializaLayout(JScrollPane scrollTableInstrucoes, JScrollPane scrollTableDados,
			JScrollPane scrollJanelaEntrada, JScrollPane scrollJanelaSaida, JScrollPane scrollBreakPoints,
			JLabel lblInstruesASerem, JLabel lblJanelaDeEntrada, JLabel lblJanelaDeSaida, JLabel lblBreakPoints,
			JLabel lblContedoDaPilha) {
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblJanelaEntrada)
										.addComponent(scrollJanelaEntrada, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
									.addGap(18)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblJanelaDeSaida)
										.addComponent(scrollJanelaSaida, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblBreakPoints, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
										.addComponent(scrollJanelaBreakPoints, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)))
								.addComponent(lblInstruesASeremExecutadas))
							.addGap(58))
						.addComponent(scrollTableInstrucoes, GroupLayout.PREFERRED_SIZE, 440, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollTableDados, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblContedoDaPilha, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblInstruesASeremExecutadas)
						.addComponent(lblContedoDaPilha))
					.addGap(7)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollTableDados, GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollTableInstrucoes, GroupLayout.PREFERRED_SIZE, 373, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblJanelaDeSaida)
								.addComponent(lblJanelaEntrada)
								.addComponent(lblBreakPoints))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(scrollJanelaEntrada, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
								.addComponent(scrollJanelaSaida)
								.addComponent(scrollJanelaBreakPoints))))
					.addContainerGap())
		);

		contentPane.setLayout(gl_contentPane);
	}

	private static void preencheTabelaInstrucoes(JScrollPane scrollTableInstrucoes) throws Exception {
		DefaultTableModel modelTable = montaDadosTabelaInstrucao();
		tableInstrucoes.setModel(modelTable);
		scrollTableInstrucoes.setViewportView(tableInstrucoes);
		frame.getContentPane().add(scrollTableInstrucoes);

		tableInstrucoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInstrucoes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				if (evt.getValueIsAdjusting())
					pegaLinhaClicada(modelTable);
			}
		});
	}

	public static void pegaLinhaClicada(DefaultTableModel modelTable) {
		verificaBreakPoint(Integer.parseInt(modelTable.getValueAt(tableInstrucoes.getSelectedRow(), 0).toString()));
	}

	public static void printJanelaSaida(Integer valor) {
		janelaSaida.append(valor.toString() + "\n");
		scrollJanelaSaida.setViewportView(janelaSaida);
		frame.getContentPane().add(scrollJanelaSaida);
	}

	public static void printJanelaBreakPoints() {
		janelaBreakPoints.setText(null);
		// Collections.sort(breakPoints);
		for (Integer integer : getBreakPoints()) {
			janelaBreakPoints.setText(janelaBreakPoints.getText() + integer + "\n");
		}
		janelaBreakPoints.revalidate();
	}

	public static String getJanelaEntrada() {
		janelaEntrada.setEditable(false);
		janelaEntrada.enableInputMethods(false);
		return getValorEntrada();
	}

	public static void verificaBreakPoint(Integer numeroLinha) {
		if (isLinhaBreakPoint(numeroLinha)) {
			removeBreakPoint(numeroLinha);
			// volta linha cor original
		} else {
			insereBreakPoint(numeroLinha);
			// pinta linha
		}
		printJanelaBreakPoints();
	}

}