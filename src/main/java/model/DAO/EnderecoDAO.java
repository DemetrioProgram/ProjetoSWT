package model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.entity.Endereco;

public class EnderecoDAO implements BaseDAO<Endereco> {

	public Endereco salvar(Endereco endereco) {
		Connection conexao = Banco.getConnection();

		String sql = " INSERT INTO ENDERECO (cep, estado, cidade, rua, bairro, numero) "
				+ " VALUES ( ?, ?, ?, ?, ?, ?)";

		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		try {
			stmt.setString(1, endereco.getCep());
			stmt.setString(2, endereco.getEstado());
			stmt.setString(3, endereco.getCidade());
			stmt.setString(4, endereco.getRua());
			stmt.setString(5, endereco.getBairro());
			stmt.setString(6, endereco.getNumero());
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultado = stmt.getGeneratedKeys();

			if (resultado.next()) {
				endereco.setId(resultado.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println(" Erro ao salvar enderešo. Causa: " + e.getMessage());
		}
		return endereco;
	}

	public boolean atualizar(Endereco endereco) {
		Connection conexao = Banco.getConnection();
		String sql = " UPDATE ENDERECO SET cep=?, estado=?, cidade=?, rua=?, bairro=?, numero=? WHERE id = ?";
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		int registrosAlterados = 0;

		try {
			stmt.setString(1, endereco.getCep());
			stmt.setString(2, endereco.getEstado());
			stmt.setString(3, endereco.getCidade());
			stmt.setString(4, endereco.getRua());
			stmt.setString(5, endereco.getBairro());
			stmt.setString(6, endereco.getNumero());
			stmt.setInt(7, endereco.getId());

			registrosAlterados = stmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Erro ao atualizar enderešo.");
			System.out.println("Erro: " + e.getMessage());
		}

		return registrosAlterados > 0;
	}

	public boolean excluir(int id) {
		String sql = " DELETE FROM ENDERECO WHERE id = ?";

		Connection conexao = Banco.getConnection();
		PreparedStatement preparedStatement = Banco.getPreparedStatement(conexao, sql);
		boolean excluiu = false;
		try {
			preparedStatement.setInt(1, id);
			int codigoRetornoUpdate = preparedStatement.executeUpdate();

			excluiu = (codigoRetornoUpdate == Banco.CODIGO_RETORNO_SUCESSO_EXCLUSAO);
		} catch (SQLException ex) {
			System.out.println(" Erro ao excluir enderešo. Id: " + id + " .Causa: " + ex.getMessage());
		}
		return excluiu;
	}

	public Endereco consultarPorId(int id) {
		String sql = " SELECT * FROM ENDERECO WHERE id = ?";

		Connection conexao = Banco.getConnection();
		PreparedStatement preparedStatement = Banco.getPreparedStatement(conexao, sql);
		Endereco enderecoConsultado = null;
		try {
			preparedStatement.setInt(1, id);
			ResultSet conjuntoResultante = preparedStatement.executeQuery();

			if (conjuntoResultante.next()) {
				enderecoConsultado = construirEnderecoDoResultSet(conjuntoResultante);
			}
		} catch (SQLException ex) {
			System.out.println(" Erro ao consultar enderešo. Id: " + id + " .Causa: " + ex.getMessage());
		}
		return enderecoConsultado;
	}

	private Endereco construirEnderecoDoResultSet(ResultSet conjuntoResultante) {
		Endereco e = new Endereco();
		try {
			e.setId(conjuntoResultante.getInt("id"));
			e.setCep(conjuntoResultante.getString("cep"));
			e.setBairro(conjuntoResultante.getString("bairro"));
			e.setCidade(conjuntoResultante.getString("cidade"));
			e.setEstado(conjuntoResultante.getString("estado"));
			e.setRua(conjuntoResultante.getString("rua"));
			e.setNumero(conjuntoResultante.getString("numero"));
		} catch (SQLException ex) {
			System.out.println(" Erro ao construir enderešo a partir do ResultSet. Causa: " + ex.getMessage());
		}
		return e;
	}

	public ArrayList<Endereco> listarTodos() {
		String sql = " SELECT * FROM ENDERECO ";

		Connection conexao = Banco.getConnection();
		PreparedStatement preparedStatement = Banco.getPreparedStatement(conexao, sql);
		ArrayList<Endereco> enderecos = new ArrayList<Endereco>();
		try {
			ResultSet conjuntoResultante = preparedStatement.executeQuery();

			while (conjuntoResultante.next()) {
				Endereco enderecoConsultado = construirEnderecoDoResultSet(conjuntoResultante);
				enderecos.add(enderecoConsultado);
			}
		} catch (SQLException ex) {
			System.out.println(" Erro ao consultar enderešos. Causa: " + ex.getMessage());
		}
		return enderecos;
	}

}
