package model.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.entity.Categoria;
import model.entity.Endereco;
import model.entity.OrdemServico;
import model.entity.Profissional;

public class OrdemServicoDAO implements BaseDAO<OrdemServico> {

	public OrdemServico salvar(OrdemServico ordemServico) {
		Connection conexao = Banco.getConnection();

		/**
		 * Pega data atual para coluna: data_cadastro
		 */
		java.util.Date dataNova = new java.util.Date();
		String dataCadastro = new SimpleDateFormat("dd/MM/yyyy").format(dataNova);

		Endereco endereco = null;
		if (ordemServico.getEndereco() != null) {
			EnderecoDAO enderecoDAO = new EnderecoDAO();

			endereco = enderecoDAO.salvar(ordemServico.getEndereco());

		}

		String sql = " INSERT INTO ORDEM_SERVICO ( numero, descricao, data_cadastro, data_inicio, data_termino_previsto, data_termino, id_cliente, id_endereco) "
				+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		try {
			stmt.setString(1, ordemServico.getNumeroOS());
			stmt.setString(2, ordemServico.getDescricao());
			stmt.setString(3, dataCadastro);
			stmt.setDate(4, Date.valueOf(ordemServico.getDataInicio()));
			stmt.setDate(5, Date.valueOf(ordemServico.getDataPrevistaFim()));
			stmt.setDate(6, Date.valueOf(ordemServico.getDataTermino()));
			stmt.setInt(7, ordemServico.getCliente().getId());
			if (endereco == null) {
				stmt.setInt(8, ordemServico.getEndereco().getId());
			} else {
				stmt.setInt(8, endereco.getId());
			}
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultado = stmt.getGeneratedKeys();

			if (resultado.next()) {
				ordemServico.setId(resultado.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println(" Erro ao salvar ordem de servi�o. Causa: " + e.getMessage());
		}

		vincularOrdemServicoProfissionais(ordemServico.getId(), ordemServico.getProfissionais());
		vincularOrdemServicoCategoria(ordemServico.getId(), ordemServico.getCategorias());

		return ordemServico;
	}

	@Override
	public boolean atualizar(OrdemServico ordemServico) {
		String sql = " UPDATE ORDEM_SERVICO SET numero=?, descricao=?, data_cadastro=?, data_inicio=?, data_termino_previsto=?, data_termino=?, id_cliente=?, id_endereco=? "
				+ " WHERE id=?";

		/**
		 * Pega data atual para coluna: data_cadastro
		 */
		java.util.Date dataNova = new java.util.Date();
		String dataCadastro = new SimpleDateFormat("dd/MM/yyyy").format(dataNova);
		Connection conexao = Banco.getConnection();

		EnderecoDAO endDAO = new EnderecoDAO();
		Endereco endereco = null;

		if (!verficarOSVinculadaEndereco(ordemServico.getEndereco().getId())) {
			endDAO.atualizar(ordemServico.getEndereco());
		} else {
			endereco = endDAO.salvar(ordemServico.getEndereco());
		}
		int registrosAlterados = 0;

		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		try {
			stmt.setString(1, ordemServico.getNumeroOS());
			stmt.setString(2, ordemServico.getDescricao());
			stmt.setString(3, dataCadastro);
			stmt.setDate(4, Date.valueOf(ordemServico.getDataInicio()));
			stmt.setDate(5, Date.valueOf(ordemServico.getDataPrevistaFim()));
			stmt.setDate(6, Date.valueOf(ordemServico.getDataTermino()));
			stmt.setInt(7, ordemServico.getCliente().getId());
			if (endereco == null) {
				stmt.setInt(8, ordemServico.getEndereco().getId());
			} else {
				stmt.setInt(8, endereco.getId());
			}
			stmt.setInt(9, ordemServico.getId());
			registrosAlterados = stmt.executeUpdate();
			ResultSet resultado = stmt.getGeneratedKeys();

			if (resultado.next()) {
				ordemServico.setId(resultado.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println(" Erro ao salvar ordem de servi�o. Causa: " + e.getMessage());
		}

		vincularOrdemServicoProfissionais(ordemServico.getId(), ordemServico.getProfissionais());
		vincularOrdemServicoCategoria(ordemServico.getId(), ordemServico.getCategorias());

		return registrosAlterados > 0;
	}

	@Override
	public boolean excluir(int id) {

		String sql = " DELETE FROM ORDEM_SERVICO WHERE id = ?";

		Connection conexao = Banco.getConnection();
		PreparedStatement preparedStatement = Banco.getPreparedStatement(conexao, sql);
		boolean excluiu = false;
		try {
			preparedStatement.setInt(1, id);
			int codigoRetornoUpdate = preparedStatement.executeUpdate();

			excluiu = (codigoRetornoUpdate == Banco.CODIGO_RETORNO_SUCESSO_EXCLUSAO);
		} catch (SQLException ex) {
			System.out.println(" Erro ao excluir Ordem de Servi�o. Id: " + id + " .Causa: " + ex.getMessage());
		}
		return excluiu;
	}

	@Override
	public OrdemServico consultarPorId(int id) {
		String sql = " SELECT * FROM ORDEM_SERVICO WHERE id = " + id;

		Connection conexao = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);

		OrdemServico ordemServico = null;

		try {
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				ordemServico = construirResultSet(rs);
			}

		} catch (SQLException e) {
			System.out.println("Erro ao consultar Ordem de servi�o por id = " + id);
			System.out.println("Erro: " + e.getMessage());
		}

		return ordemServico;
	}

	@Override
	public ArrayList<OrdemServico> listarTodos() {

		String sql = " SELECT * FROM ORDEM_SERVICO ";

		Connection conexao = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);

		ArrayList<OrdemServico> ordemServicos = new ArrayList<OrdemServico>();
		try {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				OrdemServico ordemServico = construirResultSet(rs);
				ordemServicos.add(ordemServico);
			}

		} catch (SQLException e) {
			System.out.println("Erro ao consultar todas as Ordens de Servi�o.");
			System.out.println("Erro: " + e.getMessage());
		}
		return ordemServicos;
	}

	private OrdemServico construirResultSet(ResultSet rs) {
		OrdemServico ordemServico = new OrdemServico();

		try {
			ordemServico.setId(rs.getInt("id"));
			ordemServico.setNumeroOS(rs.getString("numero_os"));
			ordemServico.setDescricao(rs.getString("descricao"));
			ordemServico.setDataInicio(rs.getDate("data_inicio").toLocalDate());
			ordemServico.setDataPrevistaFim(rs.getDate("data_termino_previsto").toLocalDate());
			ordemServico.setDataTermino(rs.getDate("data_termino").toLocalDate());

			EnderecoDAO enderecoDAO = new EnderecoDAO();
			Endereco end = enderecoDAO.consultarPorId(rs.getInt("id_endereco"));
			ordemServico.setEndereco(end);

			CategoriaDAO categoriaDAO = new CategoriaDAO();
			ArrayList<Categoria> categorias = categoriaDAO.consultarCategoriasPorOrdemServico(rs.getInt("id"));
			ordemServico.setCategorias(categorias);

		} catch (Exception e) {
			System.out.println("Erro ao construir resultSet Ordem de Servi�o. Causa:" + e.getMessage());
		}
		return ordemServico;

	}

	/**
	 * M�todos para tabelas associativas ou valida��es de dados
	 */
	private void vincularOrdemServicoProfissionais(int idOrdemServico, ArrayList<Profissional> profissionais) {

		for (int i = 0; i < profissionais.size(); i++) {

			String sql = "INSERT INTO  ORDEM_SERVICO_PROFISSIONAL (id_ordem_servico, id_profissional)" + "VALUES (?,?)";

			Connection conexao = Banco.getConnection();

			PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql, PreparedStatement.RETURN_GENERATED_KEYS);
			try {
				stmt.setInt(1, idOrdemServico);
				stmt.setInt(2, profissionais.get(i).getId());

			} catch (Exception e) {
				System.out
						.println(" Erro ao salvar vinculo Ordem de Servi�o com Profissional. Causa: " + e.getMessage());
			}

		}
	}

	private void vincularOrdemServicoCategoria(int idOrdemServico, ArrayList<Categoria> categorias) {

		for (int i = 0; i < categorias.size(); i++) {

			String sql = "INSERT INTO  ORDEM_SERVICO_CATEGORIA (id_ordem_servico, id_categoria)" + "VALUES (?,?)";

			Connection conexao = Banco.getConnection();

			PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql, PreparedStatement.RETURN_GENERATED_KEYS);
			try {
				stmt.setInt(1, idOrdemServico);
				stmt.setInt(2, categorias.get(i).getId());

			} catch (Exception e) {
				System.out.println(" Erro ao salvar vinculo Ordem de Servi�o com Categoria. Causa: " + e.getMessage());
			}

		}
	}

	private boolean verficarOSVinculadaEndereco(int idEndereco) {

		String sql = " SELECT id FROM ORDEM_SERVICO OS " + " WHERE OS.id_endereco = " + idEndereco;

		Connection conexao = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);

		boolean enderecoVinculado = false;

		try {
			ResultSet rs = stmt.executeQuery();
			enderecoVinculado = rs.next();
		} catch (SQLException e) {
			System.out.println("Erro ao verificar se Endere�o est� vinculado a alguma Ordem de Servi�o. Causa: " + e.getMessage());
		}

		return enderecoVinculado;
	}

	private boolean verficarOSVinculadaCliente(int idCLiente) {

		String sql = " SELECT id FROM ORDEM_SERVICO OS " + " WHERE OS.id_cliente = " + idCLiente;

		Connection conexao = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);

		boolean clienteVinculado = false;

		try {
			ResultSet rs = stmt.executeQuery();
			clienteVinculado = rs.next();
		} catch (SQLException e) {
			System.out.println("Erro ao verificar se Cliente est� vinculado a alguma Ordem de Servi�o. Causa: " + e.getMessage());
		}

		return clienteVinculado;
	}

}