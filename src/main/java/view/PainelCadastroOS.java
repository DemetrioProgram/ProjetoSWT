package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.github.lgooddatepicker.components.DatePicker;

import controller.CategoriaController;
import controller.ClienteController;
import controller.OrdemServicoController;
import controller.ProfissionalController;
import helpers.Estados;
import model.entity.Categoria;
import model.entity.Cliente;
import model.entity.Endereco;
import model.entity.OrdemServico;
import model.entity.Profissional;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PainelCadastroOS extends JPanel {
	private String cep;
	private String bairro;
	private String rua;
	private String numero;
	private String descricao;
	private Date dataAtual = new Date();
	private JTextField txtRua;
	private JTextField txtNumero;
	private JTextField txtCep;
	private JTextField txtBairro;
	private JTextArea txtDescricao;
	private TelaPDF telaPDF = new TelaPDF();
	private ClienteController clienteController = new ClienteController();
	private CategoriaController categoriaController = new CategoriaController();
	private OrdemServicoController ordemServicoController = new OrdemServicoController();
	private ProfissionalController profissionalController = new ProfissionalController();
	private ArrayList<Profissional> profissionais = new ArrayList<Profissional>();
	private ArrayList<Categoria> categorias = new ArrayList<Categoria>();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private Endereco endereco = new Endereco();
	private JComboBox cbCategoria;
	private JComboBox cbCliente;
	private JComboBox cbProfissional;
	private JTextField txtCidade;
	private JComboBox cbEstados;
	private JTextField txtNumeroOS;

	/**
	 * Create the panel.
	 */
	public PainelCadastroOS() {

		JLabel lblNumeroOS = new JLabel("N\u00B0 Odem de Servi\u00E7o:");

		MaskFormatter formatoOrdemServico;

		String ano = new SimpleDateFormat("/yyyy").format(dataAtual);
		JLabel lblAno = new JLabel(ano);

		JLabel lblCliente = new JLabel("Cliente:");
		
		clientes = clienteController.listarClientes();
		cbCliente = new JComboBox(clientes.toArray());

		JCheckBox chckbxMesmoEnderecoDo = new JCheckBox("Mesmo endere\u00E7o do Cliente");

		JLabel lblRua = new JLabel("Rua:");

		txtRua = new JTextField();
		txtRua.setColumns(10);

		JLabel lblNumero = new JLabel("N\u00FAmero");

		txtNumero = new JTextField();
		txtNumero.setColumns(10);

		JLabel lblCep = new JLabel("CEP:");

		MaskFormatter formatoCep;

		try {
			formatoCep = new MaskFormatter("#####-###");
			txtCep = new JFormattedTextField(formatoCep);
			txtCep.setColumns(10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JLabel lblBairro = new JLabel("Bairro:");

		txtBairro = new JTextField();
		txtBairro.setColumns(10);

		JLabel lblDescricao = new JLabel("Descri\u00E7\u00E3o:");

		txtDescricao = new JTextArea();

		JLabel lblDataInicial = new JLabel("Data Inicial:");

		JLabel lblDataPrevistaTermino = new JLabel("Data Prevista Termino:");

		final DatePicker dateInicial = new DatePicker();
		dateInicial.getComponentToggleCalendarButton().setText("");
		dateInicial.getComponentToggleCalendarButton().setIcon(new ImageIcon(PainelCadastroOS.class.getResource("/icones/calendar-icon.png")));

		final DatePicker datePrevistaFinal = new DatePicker();
		datePrevistaFinal.getComponentToggleCalendarButton().setIcon(new ImageIcon(PainelCadastroOS.class.getResource("/icones/calendar-icon.png")));
		datePrevistaFinal.getComponentToggleCalendarButton().setText("");

		Estados estado = new Estados();
		ArrayList<String> estados = (ArrayList<String>) estado.consultarEstados();
		cbEstados = new JComboBox(estados.toArray());
		
		JLabel lblCidade = new JLabel("Cidade:");
		
		txtCidade = new JTextField();
		txtCidade.setColumns(10);

		JLabel lblCategoria = new JLabel("Categoria:");

		categorias = categoriaController.listarCategorias();
		cbCategoria = new JComboBox(categorias.toArray());

		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ComboProfissionalPorCategoria cbProf = new ComboProfissionalPorCategoria();
				int idCategoria = cbCategoria.getSelectedIndex();
				idCategoria += 1;
				categorias.add((Categoria) cbCategoria.getSelectedItem());
				cbProf.getDados(idCategoria);
				cbProf.setVisible(true);
			}
		});
		btnAdd.setIcon(new ImageIcon(PainelCadastroOS.class.getResource("/icones/Button-Add-icon.png")));

		JLabel lblProfissionais = new JLabel("Profissionais:");

		profissionais = profissionalController.listarProfissionais();
		cbProfissional = new JComboBox(profissionais.toArray());

		JButton btnVisualizar = new JButton("Visualizar");
		btnVisualizar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String ano = lblAno.getText();
				String numeroOS = txtNumeroOS.getText();
				numeroOS += ano;
				Cliente cliente = new Cliente();
				
				cliente = (Cliente) cbCliente.getSelectedItem();
				
				/*if (chckbxMesmoEnderecoDo.isSelected()) {
					//TODO
				} else {
					cep = txtCep.getText();
					bairro = txtBairro.getText();
					rua = txtRua.getText();
					numero = txtNumero.getText();
				}*/
				
				String msg = ordemServicoController.cadastrarOS( numeroOS, (String) cbEstados.getSelectedItem(), txtCidade.getText(), 
						txtNumero.getText(), txtRua.getText(), txtBairro.getText(), txtCep.getText(), dateInicial.getDate(),
						datePrevistaFinal.getDate(), txtDescricao.getText(), cliente, categorias, profissionais);
				JOptionPane.showMessageDialog(null, msg);
				/*telaPDF.getDados(numeroOS, cliente, descricao, dataInicial, dataPrevistaFinal, categoria, profissional,endereco);
				
				telaPDF.setVisible(true);
				telaPDF.setLocationRelativeTo(null);*/
			}
		});

		JLabel lblCadastroOrdemDeServio = new JLabel("Cadastro Ordem de Servi\u00E7o");
		lblCadastroOrdemDeServio.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel lblEndereo = new JLabel("Endere\u00E7o");
		lblEndereo.setFont(new Font("Tahoma", Font.BOLD, 12));

		JButton btnLimpar = new JButton("Limpar");
		
		JCheckBox chckbxFinalizada = new JCheckBox("Finalizada");
		
		JLabel lblEstado = new JLabel("Estado:");
		
		MaskFormatter formatoNumero;

		try {
			formatoNumero = new MaskFormatter("#####");
			txtNumeroOS = new JFormattedTextField(formatoNumero);
			txtNumeroOS.setColumns(10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JButton btnBurcarCep = new JButton("Burcar Cep");
		btnBurcarCep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtCep.getText() != null || !txtCep.getText().isEmpty()) {
					endereco = ordemServicoController.buscarEnderecoPorCep(txtCep.getText());
					txtCidade.setText(endereco.getCidade());
					txtBairro.setText(endereco.getBairro());
					txtRua.setText(endereco.getRua());
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(51)
					.addComponent(lblCadastroOrdemDeServio))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(51)
					.addComponent(lblNumeroOS)
					.addGap(12)
					.addComponent(txtNumeroOS, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(lblAno)
					.addGap(262)
					.addComponent(chckbxFinalizada, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(51)
					.addComponent(lblCliente)
					.addGap(5)
					.addComponent(cbCliente, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(53)
					.addComponent(lblEndereo))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(53)
					.addComponent(chckbxMesmoEnderecoDo))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(53)
					.addComponent(lblCep)
					.addGap(5)
					.addComponent(txtCep, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(btnBurcarCep, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblEstado, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(51)
							.addComponent(cbEstados, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)))
					.addGap(9)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCidade, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(50)
							.addComponent(txtCidade, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(53)
					.addComponent(lblRua)
					.addGap(5)
					.addComponent(txtRua, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(lblBairro)
					.addGap(5)
					.addComponent(txtBairro, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
					.addGap(14)
					.addComponent(lblNumero)
					.addGap(12)
					.addComponent(txtNumero, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(51)
					.addComponent(lblDescricao)
					.addGap(12)
					.addComponent(txtDescricao, GroupLayout.PREFERRED_SIZE, 527, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(51)
					.addComponent(lblDataInicial)
					.addGap(5)
					.addComponent(dateInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(140)
							.addComponent(datePrevistaFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblDataPrevistaTermino, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(51)
					.addComponent(lblCategoria)
					.addGap(12)
					.addComponent(cbCategoria, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addComponent(lblProfissionais)
					.addGap(5)
					.addComponent(cbProfissional, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnAdd))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(151)
					.addComponent(btnLimpar, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
					.addGap(249)
					.addComponent(btnVisualizar))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(13)
					.addComponent(lblCadastroOrdemDeServio)
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(lblNumeroOS))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(txtNumeroOS, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(7)
							.addComponent(lblAno))
						.addComponent(chckbxFinalizada))
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblCliente))
						.addComponent(cbCliente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblEndereo)
					.addGap(11)
					.addComponent(chckbxMesmoEnderecoDo)
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(lblCep))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addComponent(txtCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnBurcarCep)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(3)
									.addComponent(lblEstado))
								.addComponent(cbEstados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(3)
									.addComponent(lblCidade))
								.addComponent(txtCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(25)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblRua))
						.addComponent(txtRua, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblBairro))
						.addComponent(txtBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblNumero))
						.addComponent(txtNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblDescricao))
						.addComponent(txtDescricao, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
					.addGap(58)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(lblDataInicial))
						.addComponent(dateInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(datePrevistaFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(2)
									.addComponent(lblDataPrevistaTermino)))))
					.addGap(37)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(lblCategoria))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(cbCategoria, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(lblProfissionais))
						.addComponent(cbProfissional, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd))
					.addGap(52)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnLimpar)
						.addComponent(btnVisualizar)))
		);
		setLayout(groupLayout);
		

	}
}
