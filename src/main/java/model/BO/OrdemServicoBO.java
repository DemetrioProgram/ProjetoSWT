package model.BO;

import java.util.ArrayList;

import model.DAO.OrdemServicoDAO;
import model.details.Agenda;
import model.entity.OrdemServico;
import model.seletor.AgendaSeletor;
import model.seletor.OrdemServicoSeletor;

public class OrdemServicoBO {
	
	private OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
	private String msg = "";
	
	public String cadastrarOS(OrdemServico ordemServico) {
		OrdemServico os = new OrdemServico();
		os = ordemServicoDAO.salvar(ordemServico);
		if (os != null) {
			 msg= " Ordem de servi�o salva com sucesso. ";
		} else {
			 msg= " Ocorreu um erro ao tentar salvar a ordem de servi�o. ";
		}
		return msg;
	}

	public ArrayList<OrdemServico> listarClientes(OrdemServicoSeletor seletor) {
		
		if (seletor.getCatgoria() == null && seletor.getCliente() == null && !seletor.getNumeroOS().trim().isEmpty()) {
			return ordemServicoDAO.listarPorTodosPorNumeroOS(seletor.getNumeroOS());
		}
		
		if (seletor.getCatgoria() != null && seletor.getCliente() != null && !seletor.getNumeroOS().trim().isEmpty()) {
			System.out.println(seletor);
			return ordemServicoDAO.listarPorSeletor(seletor);			
		} else {
			return ordemServicoDAO.listarTodos();
		}
	}

	public ArrayList<OrdemServico> listarTodos() {
		return ordemServicoDAO.listarTodos();
	}

	public String atualizar(OrdemServico os) {
		boolean atualizado = ordemServicoDAO.atualizar(os);
		if (atualizado) {
			msg = " Ordem de Servi�o atualizado! ";
		} else if (atualizado == false) {
			msg = " Ordem de Servi�o n�o atualizado! ";
		} else {
			msg = " Ocorreu um erro ao atualizar Ordem de Servi�o ";
		}
		return msg;
	}

	public String excluir(OrdemServico os) {
		boolean excluido =  ordemServicoDAO.excluir(os);
		if (excluido) {
			msg = " Ordem de Servi�o excluida! ";
		} else if (excluido == false) {
			msg = " Ordem de Servi�o n�o excluida! ";
		} else {
			msg = " Ocorreu um erro ao excluir Ordem de Servi�o ";
		}
		return msg;

	}

	public boolean jaTemNumeroOS(String numOS) {
		return ordemServicoDAO.jaTemNumeroOS(numOS);
	}

}
