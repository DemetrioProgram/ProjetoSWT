package testes.menu;

import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

public class TelaPrincipal01 extends JFrame {

	private PainelCadastroOS01 painelCadastroOS = null;
	private PainelCadastroCliente painelCadastroCliente = null;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal01 frame = new TelaPrincipal01();
					frame.setExtendedState(MAXIMIZED_BOTH);
					frame.setVisible(true);
					frame.setExtendedState(MAXIMIZED_BOTH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaPrincipal01() {
		setTitle("GeTasK: Gerencimento de Servi\u00E7os de Constru\u00E7\u00E3o Civil");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 350);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnOS = new JMenu("Ordem de Servi\u00E7o");
		mnOS.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/icons8-ordem-de-compra-50.png")));
		menuBar.add(mnOS);

		JMenuItem mnitCadOs = new JMenuItem("Cadastro");
		mnitCadOs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnitCadOs.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (painelCadastroOS == null) {
					painelCadastroOS = new PainelCadastroOS01();
				}
				abrir(painelCadastroOS);
			}
		});
		mnitCadOs.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/Button-Add-icon.png")));
		mnOS.add(mnitCadOs);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Consulta");
		mntmNewMenuItem_1.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/Search-icon.png")));
		mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		mnOS.add(mntmNewMenuItem_1);

		JMenu mnClientes = new JMenu("Clientes");
		mnClientes.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/User-Clients-icon.png")));
		menuBar.add(mnClientes);

		JMenuItem mnitCadCliente = new JMenuItem("Cadastro");
		mnitCadCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (painelCadastroCliente == null) {
					painelCadastroCliente = new PainelCadastroCliente();
				}
				abrir(painelCadastroCliente);
			}
		});
		mnitCadCliente.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/Button-Add-icon.png")));
		mnitCadCliente.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		mnClientes.add(mnitCadCliente);

		JMenuItem mnitListCliente = new JMenuItem("Consulta");
		mnitListCliente.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/Search-icon.png")));
		mnitListCliente.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		mnClientes.add(mnitListCliente);

		JMenu mnEmpresa = new JMenu("Empresa");
		mnEmpresa.setIcon(
				new ImageIcon(TelaPrincipal01.class.getResource("/icones/icons8-organiza\u00E7\u00E3o-50.png")));
		menuBar.add(mnEmpresa);

		JMenuItem mnitCategorias = new JMenuItem("Categorias");
		mnitCategorias.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/checklist-icon.png")));
		mnitCategorias.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mnEmpresa.add(mnitCategorias);

		JMenuItem mnitProfissionais = new JMenuItem("Profissionais");
		mnitProfissionais.setIcon(
				new ImageIcon(TelaPrincipal01.class.getResource("/icones/10512-man-construction-worker-icon.png")));
		mnitProfissionais.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		mnEmpresa.add(mnitProfissionais);

		JMenu mnAgenda = new JMenu("Agenda");
		mnAgenda.setIcon(new ImageIcon(TelaPrincipal01.class.getResource("/icones/icons8-calendar-50.png")));
		menuBar.add(mnAgenda);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new SpringLayout());
	}
	
	private void abrir(JPanel jPanel) {

		if (!jPanel.isShowing()) {
			contentPane.add(jPanel);
			esconderPaineis();
			jPanel.setVisible(true);
			validate();
		} else {
			JOptionPane.showMessageDialog(null, "Tela j� est� sendo mostrada.");
		}

	}

	private void esconderPaineis() {
		painelCadastroCliente.setVisible(false);
		painelCadastroOS.setVisible(false);
	}

}