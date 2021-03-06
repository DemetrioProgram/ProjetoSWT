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
import model.details.CadastroOS;
import model.entity.Categoria;
import model.entity.Cliente;
import model.entity.Endereco;
import model.entity.OrdemServico;
import model.entity.Profissional;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

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

	private Endereco endereco = new Endereco();
	private JComboBox cbCategoria;
	private JComboBox cbCliente;
	private JComboBox cbProfissional;
	private JTextField txtCidade;
	private JComboBox cbEstados;
	private JTextField txtNumeroOS;
	private JCheckBox chckbxFinalizada;
	private JCheckBox chckbxMesmoEnderecoDo;
	private DatePicker dateInicial;
	private DatePicker datePrevistaFinal;
	private Categoria categoria;

	private ClienteController clienteController = new ClienteController();
	private CategoriaController categoriaController = new CategoriaController();
	private OrdemServicoController ordemServicoController = new OrdemServicoController();

	private ArrayList<Profissional> profissionaisSelecionados = new ArrayList<Profissional>();
	private ArrayList<Categoria> categoriasSelecionadas = new ArrayList<Categoria>();
	private ArrayList<Categoria> categorias = new ArrayList<Categoria>();
	private ArrayList<Cliente> clientes = new ArrayList<Cliente>();
	private ArrayList<Profissional> profsxCategoria = new ArrayList<Profissional>();

	/**
	 * Create the panel.
	 */
	public PainelCadastroOS() {

		JLabel lblNumeroOS = new JLabel("N\u00B0 Odem de Servi\u00E7o:");

		String ano = new SimpleDateFormat("/yyyy").format(dataAtual);
		final JLabel lblAno = new JLabel(ano);

		chckbxFinalizada = new JCheckBox("Finalizada");

		JLabel lblCliente = new JLabel("Cliente:");

		clientes = clienteController.listarClientes();
		cbCliente = new JComboBox(clientes.toArray());
		cbCliente.setSelectedIndex(-1);

		chckbxMesmoEnderecoDo = new JCheckBox("Mesmo endere\u00E7o do Cliente");

		chckbxMesmoEnderecoDo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxMesmoEnderecoDo.isSelected()) {
					if (cbCliente.getSelectedIndex() < 0) {
						JOptionPane.showMessageDialog(null, "Favor informe um cliente.");
						chckbxMesmoEnderecoDo.setSelected(false);
					} else {
						Endereco end = null;
						try {
							end = clienteController.consultarEnderecoCliente((Cliente) cbCliente.getSelectedItem());

						} catch (Exception er) {
							System.out.println("Erro ao buscar endere�o pelo Cliente. Causa: " + er.getMessage());
						}

						if (end != null) {
							txtRua.setText(end.getRua());
							txtNumero.setText(end.getNumero());
							txtBairro.setText(end.getBairro());
							txtCidade.setText(end.getCidade());
							txtCep.setText(end.getCep());
							cbEstados.setSelectedItem(end.getEstado());
						}
					}
				} else {
					txtRua.setText("");
					txtNumero.setText("");
					txtBairro.setText("");
					txtCidade.setText("");
					txtCep.setText("");
					cbEstados.setSelectedIndex(-1);
				}
			}
		});

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
			System.out.println("Erro na m�scara do CEP (cad OS).Causa: " + e.getMessage());
		}

		JLabel lblBairro = new JLabel("Bairro:");

		txtBairro = new JTextField();
		txtBairro.setColumns(10);

		JLabel lblDescricao = new JLabel("Descri\u00E7\u00E3o:");

		txtDescricao = new JTextArea();

		JLabel lblDataInicial = new JLabel("Data Inicial:");

		JLabel lblDataPrevistaTermino = new JLabel("Data Prevista T�rmino:");

		dateInicial = new DatePicker();
		dateInicial.getComponentToggleCalendarButton().setText("");
		dateInicial.getComponentToggleCalendarButton()
				.setIcon(new ImageIcon(PainelCadastroOS.class.getResource("/icones/calendar-icon.png")));

		datePrevistaFinal = new DatePicker();
		datePrevistaFinal.getComponentToggleCalendarButton()
				.setIcon(new ImageIcon(PainelCadastroOS.class.getResource("/icones/calendar-icon.png")));
		datePrevistaFinal.getComponentToggleCalendarButton().setText("");

		Estados estado = new Estados();
		ArrayList<String> estados = (ArrayList<String>) estado.consultarEstados();
		cbEstados = new JComboBox(estados.toArray());
		cbEstados.setSelectedIndex(-1);

		JLabel lblCidade = new JLabel("Cidade:");

		txtCidade = new JTextField();
		txtCidade.setColumns(10);

		JLabel lblCategoria = new JLabel("Categoria:");

		categorias = categoriaController.listarCategorias();
		cbCategoria = new JComboBox(categorias.toArray());
		cbCategoria.setSelectedIndex(-1);
		cbProfissional = new JComboBox<Profissional>();

		cbCategoria.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				cbProfissional.removeAllItems();
				categoria = (Categoria) cbCategoria.getSelectedItem();
				ProfissionalController profcontrol = new ProfissionalController();

				profsxCategoria = profcontrol.listarProfissionaisPorCategoria(categoria.getId());

				if (profsxCategoria.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Nenhum profissional dispon�vel para a categoria " + categoria);
				} else {

					cbProfissional.addItem("SELECIONE PROFISSIONAL");
					for (int i = 0; i < profsxCategoria.size(); i++) {
						cbProfissional.addItem(profsxCategoria.get(i));
					}
				}

			}
		});

		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Profissional profSelecionado = (Profissional) cbProfissional.getSelectedItem();
				int check = 0;
				for (int i = 0; i < profissionaisSelecionados.size(); i++) {
					if (profissionaisSelecionados.get(i).getId() == profSelecionado.getId()) {
						check++;
					}
				}
				if (check != 0) {
					JOptionPane.showMessageDialog(null, "Profissional j� foi selecionado");
				} else {
					profissionaisSelecionados.add(profSelecionado);
					int check2 = 0;
					for (int i = 0; i < categoriasSelecionadas.size(); i++) {
						if (categoriasSelecionadas.get(i).getId() == categoria.getId()) {
							check2++;
						}
					}
					if (check2 == 0) {
						categoriasSelecionadas.add(categoria);
					}
				}
			}
		});

		btnAdd.setIcon(new ImageIcon(PainelCadastroOS.class.getResource("/icones/Button-Add-icon.png")));

		JLabel lblProfissionais = new JLabel("Profissionais:");

		JButton btnVisualizar = new JButton("Visualizar");
		CadastroOS cadOS = new CadastroOS();

		btnVisualizar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String ano = new SimpleDateFormat("/yyyy").format(dataAtual);
				String numOs = txtNumeroOS.getText() + ano;
				Endereco e = new Endereco();
				if (ordemServicoController.jaTemNumeroOS(numOs)) {
					JOptionPane.showMessageDialog(null,
							"N�mero de ordem de servi�o j� existe, favor informar outro n�mero.");
					txtNumeroOS.setText("");
				} else {
					cadOS.setNumeroOS(txtNumeroOS.getText());
					cadOS.setCliente((Cliente) cbCliente.getSelectedItem());
					if (chckbxMesmoEnderecoDo.isSelected()) {
						Cliente c = (Cliente) cbCliente.getSelectedItem();
						e = c.getEndereco();
					} else {
						e = new Endereco(txtRua.getText(), txtNumero.getText(), txtBairro.getText(),
								txtCidade.getText(), (String) cbEstados.getSelectedItem(), txtCep.getText());
					}
					cadOS.setEndereco(e);
					cadOS.setCategorias(categoriasSelecionadas);
					cadOS.setProfissionais(profissionaisSelecionados);
					cadOS.setDataInicio(dateInicial.getDate());
					cadOS.setDataPrevistaFim(datePrevistaFinal.getDate());
					cadOS.setFinalizada(chckbxFinalizada.isSelected());
					cadOS.setDescricao(txtDescricao.getText());

					String msg = ordemServicoController.validarCampos(cadOS);

					if (!msg.isEmpty()) {

						JOptionPane.showMessageDialog(null, msg);
						msg = "";
					} else {
						TelaPDF telaPDF = new TelaPDF();

						telaPDF.enviarDados(cadOS);

						telaPDF.setVisible(true);

						telaPDF.setLocationRelativeTo(null);
					}

				}
			}
		});

		JLabel lblCadastroOrdemDeServio = new JLabel("Cadastro Ordem de Servi\u00E7o");
		lblCadastroOrdemDeServio.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel lblEndereo = new JLabel("Endere\u00E7o");
		lblEndereo.setFont(new Font("Tahoma", Font.BOLD, 12));

		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparCampos();

			}
		});

		JLabel lblEstado = new JLabel("Estado:");

		MaskFormatter formatoNumero;

		try {
			formatoNumero = new MaskFormatter("#####");
			txtNumeroOS = new JFormattedTextField(formatoNumero);
			txtNumeroOS.setColumns(10);
		} catch (ParseException e) {
			System.out.println("Erro m�scara N�mero OS.Causa: " + e.getMessage());
		}

		JButton btnBuscarCep = new JButton("Buscar");
		btnBuscarCep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cep = txtCep.getText().trim().replace("-", "");
				if (cep != null && !cep.trim().isEmpty()) {
					try {
					endereco = ordemServicoController.buscarEnderecoPorCep(txtCep.getText());
					txtCidade.setText(endereco.getCidade().replace("ó", "�").replace("ç", "�").replace("í", "�"));
					txtBairro.setText(endereco.getBairro().replace("ó", "�").replace("ç", "�").replace("í", "�"));
					txtRua.setText(endereco.getRua());
					cbEstados.setSelectedItem(endereco.getEstado());
					}catch (Exception eCEP) {
						System.out.println("CEP INV�LIDO!. Erro: "+eCEP.getMessage());
						JOptionPane.showMessageDialog(null, "CEP inv�lido!");
						txtCep.setText("");
					}
				} else {
					String msg = " Digite o CEP. ";
					JOptionPane.showMessageDialog(null, msg);
				}
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(51).addComponent(lblCategoria).addGap(12)
								.addComponent(cbCategoria, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)
								.addGap(4).addComponent(lblProfissionais).addGap(5)
								.addComponent(cbProfissional, GroupLayout.PREFERRED_SIZE, 187,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(btnAdd))
						.addGroup(groupLayout.createSequentialGroup().addGap(151)
								.addComponent(btnLimpar, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
								.addGap(249).addComponent(btnVisualizar)))
						.addGroup(groupLayout.createSequentialGroup().addGap(51).addComponent(lblCadastroOrdemDeServio))
						.addGroup(groupLayout.createSequentialGroup().addGap(51).addComponent(lblNumeroOS).addGap(12)
								.addComponent(txtNumeroOS, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
								.addGap(12).addComponent(lblAno).addGap(262).addComponent(chckbxFinalizada,
										GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(51).addComponent(lblCliente).addGap(5)
								.addComponent(cbCliente, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(53).addComponent(lblEndereo))
						.addGroup(groupLayout.createSequentialGroup().addGap(53).addComponent(chckbxMesmoEnderecoDo))
						.addGroup(groupLayout.createSequentialGroup().addGap(53).addComponent(lblCep).addGap(5)
								.addComponent(txtCep, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
								.addGap(12)
								.addComponent(btnBuscarCep, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
								.addGap(5)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblEstado, GroupLayout.PREFERRED_SIZE, 56,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup().addGap(51).addComponent(cbEstados,
												GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)))
								.addGap(9)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblCidade, GroupLayout.PREFERRED_SIZE, 56,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup().addGap(50).addComponent(txtCidade,
												GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup().addGap(53).addComponent(lblRua).addGap(5)
								.addComponent(txtRua, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
								.addGap(12).addComponent(lblBairro).addGap(5)
								.addComponent(txtBairro, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
								.addGap(14).addComponent(lblNumero).addGap(12)
								.addComponent(txtNumero, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(51).addComponent(lblDescricao).addGap(12)
								.addComponent(txtDescricao, GroupLayout.PREFERRED_SIZE, 527,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(51).addComponent(lblDataInicial).addGap(5)
								.addComponent(dateInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(12)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addGap(140).addComponent(
												datePrevistaFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
										.addComponent(lblDataPrevistaTermino, GroupLayout.PREFERRED_SIZE, 144,
												GroupLayout.PREFERRED_SIZE))))
				.addContainerGap(51, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(13).addComponent(lblCadastroOrdemDeServio).addGap(10)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(4).addComponent(lblNumeroOS))
						.addGroup(groupLayout.createSequentialGroup().addGap(4).addComponent(txtNumeroOS,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(7).addComponent(lblAno))
						.addComponent(chckbxFinalizada))
				.addGap(29)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblCliente))
						.addComponent(cbCliente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(18).addComponent(lblEndereo).addGap(11).addComponent(chckbxMesmoEnderecoDo).addGap(27)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(4).addComponent(lblCep))
						.addGroup(groupLayout.createSequentialGroup().addGap(1).addComponent(txtCep,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnBuscarCep)
						.addGroup(groupLayout.createSequentialGroup().addGap(1)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblEstado))
										.addComponent(cbEstados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup().addGap(1)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblCidade))
										.addComponent(txtCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))))
				.addGap(25)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblRua))
						.addComponent(txtRua, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblBairro))
						.addComponent(txtBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblNumero))
						.addComponent(txtNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblDescricao))
						.addComponent(txtDescricao, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
				.addGap(58)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(2).addComponent(lblDataInicial))
						.addComponent(dateInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup().addGap(2)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(datePrevistaFinal, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(groupLayout.createSequentialGroup().addGap(2)
												.addComponent(lblDataPrevistaTermino)))))
				.addGap(37)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(6).addComponent(lblCategoria))
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(cbCategoria,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblProfissionais))
						.addComponent(cbProfissional, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAdd))
				.addGap(52).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(btnLimpar)
						.addComponent(btnVisualizar))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		setLayout(groupLayout);

	}

	private void limparCampos() {
		this.txtNumeroOS.setText("");
		this.txtCep.setText("");
		this.txtBairro.setText("");
		this.txtRua.setText("");
		this.txtNumero.setText("");
		this.txtCidade.setText("");
		this.txtDescricao.setText("");
		this.cbEstados.setSelectedIndex(-1);
		this.cbCliente.setSelectedIndex(-1);
		this.chckbxFinalizada.setSelected(false);
		this.chckbxMesmoEnderecoDo.setSelected(false);
		this.cbProfissional.setSelectedIndex(-1);
		this.dateInicial.setText("");
		this.datePrevistaFinal.setText("");
		this.categoriasSelecionadas = new ArrayList<Categoria>();
		this.profissionaisSelecionados = new ArrayList<Profissional>();
	}
	
}
