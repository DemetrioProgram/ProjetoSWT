package model.BO;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import model.entity.Endereco;

public class BuscarCep {

	Endereco endereco = new Endereco();

	public Endereco buscarCep(String cep) {
		String json;

		try {
			URL url = new URL("http://viacep.com.br/ws/" + cep + "/json");
			URLConnection urlConnection = url.openConnection();
			InputStream is = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			StringBuilder jsonSb = new StringBuilder();

			br.lines().forEach(l -> jsonSb.append(l.trim()));
			json = jsonSb.toString();

			json = json.replaceAll("[{},:]", "");
			json = json.replaceAll("\"", "\n");
			String array[] = new String[30];
			array = json.split("\n");

			String logradouro = array[7];
			String bairro = array[15];
			String cidade = array[19];
			String uf = array[23];

			endereco.setRua(logradouro);
			endereco.setBairro(bairro);
			endereco.setCidade(cidade);
			endereco.setEstado(uf);

			return endereco;
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
