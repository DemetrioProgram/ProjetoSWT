package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import com.github.lgooddatepicker.components.DatePicker;

import model.BO.AgendaBO;
import model.details.Agenda;
import model.seletor.AgendaSeletor;

public class AgendaController {

	private AgendaBO bo = new AgendaBO();

	public String validarCamposObrigatorios(LocalDate inicio, LocalDate fim) {
		String msg = "";

		if (inicio == null) {
			msg += "Inserir a data de in�cio do per�do. \n";
		}
		if (fim == null) {
			msg += "Inserir a data de fim do per�odo.";
		}
		if (inicio != null && fim != null) {
			if (inicio.isAfter(fim)) {
				msg += "Data de in�cio do per�odo n�o pode ser maior que a data de t�rmino .";
			}
		}

		return msg;
	}

	public ArrayList<Agenda> listarOSAgenda(AgendaSeletor seletor) {
		return bo.listarOSAgenda(seletor);
	}

}
