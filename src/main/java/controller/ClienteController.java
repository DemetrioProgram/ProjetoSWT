package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import model.BO.BuscarCep;
import model.BO.ClienteBO;
import model.entity.Cliente;
import model.entity.Endereco;
import model.seletor.ClienteSeletor;

public class ClienteController {

	private ClienteBO bo = new ClienteBO();

	public String salvar(String inscricao, String nome, boolean Cpf, boolean Cnpj, boolean ativo, String tel,
			String email, String cep, String rua, String numero, String bairro, String cidade, String estado) {

		String msg = "";

		if (inscricao == null || inscricao.trim().isEmpty()) {
			msg += " Digite a inscri��o. \n";
		}
		if (nome == null || nome.trim().isEmpty()) {
			msg += " Digite o nome. \n";
		}
		if (tel == null || tel.trim().isEmpty()) {
			msg += " Digite o telefone. \n";
		}
		if (email == null || email.trim().isEmpty()) {
			msg += " Digite o email. \n";
		}

		ValidarEnderecoController validarEndereco = new ValidarEnderecoController();
		msg += validarEndereco.validarEndereco(cep, rua, numero, bairro, cidade, estado);

		if (msg.isEmpty()) {

			LocalDate dtcadastro = LocalDate.now();
			boolean ehCpf = false;

			if (Cpf) {
				ehCpf = true;
			}

			Endereco e = new Endereco(rua, numero, bairro, cidade, estado, cep);
			Cliente cliente = new Cliente();
			cliente.setInscricao(inscricao);
			cliente.setNome(nome);
			cliente.setAtivo(ativo);
			cliente.setDataCadastro(dtcadastro);
			cliente.setEhCpf(ehCpf);
			cliente.setEmail(email);
			cliente.setTelefone(tel);
			cliente.setEndereco(e);

			msg = bo.salvar(cliente);

			return msg;

		} else {
			return msg;
		}
	}

	public ArrayList<Cliente> listarClientes() {
		return bo.listarClientes();
	}

	public Endereco buscarEnderecoPorCep(String cep) {
		BuscarCep buscarCep = new BuscarCep();
		return buscarCep.buscarCep(cep);
	}

	public Endereco consultarEnderecoCliente(Cliente clienteSelecionado) {
		
		return bo.consultarEnderecoCliente(clienteSelecionado);
	}

	public ArrayList<Cliente> listarClientes(ClienteSeletor seletor) {
		
		return bo.listarClientes(seletor);
	}

	public String atualizar(int id, int idEndereco, String cep, String bairro, String cidade, String rua, String numero, String nome,
			String cpf, String telefone, String email, String estado) {
		
		String msg = "";
		
		if (id == 0) {
			msg += " Selecione um cliente. ";
		}
		if (cpf == null || cpf.trim().isEmpty()) {
			msg += " Digite a inscri��o. \n";
		}
		if (nome == null || nome.trim().isEmpty()) {
			msg += " Digite o nome. \n";
		}
		if (telefone == null || telefone.trim().isEmpty()) {
			msg += " Digite o telefone. \n";
		}
		if (email == null || email.trim().isEmpty()) {
			msg += " Digite o email. \n";
		}
		
		ValidarEnderecoController validarEndereco = new ValidarEnderecoController();
		msg += validarEndereco.validarEndereco(cep, rua, numero, bairro, cidade, estado);
		
		if (msg.isEmpty()) {

			LocalDate dtcadastro = LocalDate.now();
			boolean ehCpf = false;


			Endereco e = new Endereco(idEndereco, rua, numero, bairro, cidade, estado, cep);
			Cliente cliente = new Cliente();
			cliente.setId(id);
			cliente.setInscricao(cpf);
			cliente.setNome(nome);
			cliente.setAtivo(true);
			cliente.setDataCadastro(dtcadastro);
			cliente.setEhCpf(ehCpf);
			cliente.setEmail(email);
			cliente.setTelefone(telefone);
			cliente.setEndereco(e);

			msg = bo.atualizar(cliente);

			return msg;

		} else {
			return msg;
		}
	}

	public String excluir(Cliente cliente) {
		return bo.excluir(cliente);
	}

}
