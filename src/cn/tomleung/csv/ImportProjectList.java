package cn.tomleung.csv; // Indica qual é o diretório que será executado a classe principal do projeto.

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.csvreader.CsvReader;

public class ImportProjectList {

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in); //Aqui abre o form para escolher o arquivo
		//Abaixo o "in" e o stream que vai ler os arquivos
		//Ele pega o stream e passa o diretorio dos arquivos como primeiro parametro para o metodo "process" que vai processar o CSV
		//O segundo parametro e o nome da tabela que irá ter os dados inseridos
		process(in.nextLine(), "AQUI_VEM_O_NOME_DA_TABELA_NO_DB"); // LINHA README.md
		//Aqui ele fecha o stream para liberar os arquivos
		in.close();
	}

	// método que processa os arquivos CSV e dispara o insert no banco
	private static void process(String csvFolderPath, String tableName) {
		//Aqui pega o path do arquivo e lê o conteúdo dele
		File folder = new File(csvFolderPath);
		//Captura o array dos arquivos que estao na pasta
		File[] files = folder.listFiles();
		//Roda um loop executando o insert para cada arquivo que estiver na pasta
		for (int i = 0; i < files.length; i++) {
			//Pega o arquivo da vez no loopp
			File file = files[i];
			try {
				//Executa o metodo de insert num try para tratar excecoes
				insert(file.getAbsolutePath(), tableName);
			} catch (Exception e) 
				//mostra o erro caso algo saia errado
				e.printStackTrace();
			}
		}
	}

	// Método para inserir na tabela do banco os arquivos CSV (um por um)
	private static void insert(String csvFile, String tableName) throws Exception {
		//Cria um objeto da classe DBConnector para manter o estado da conexo com o banco enquanto o metodo e executado
		Connection con = new DBConnector().connect();
		//Cria a string do insert com "?", cada "?" e um parametro passado, eles sao lidos na ordem de input
		String sql = "INSERT INTO " + tableName + " VALUES(?,?,?,?,?,?,?,?,?,?)";
		//Prepara a transaction no banco criando um Statement gerenciado
		PreparedStatement pstmt;  //Esse e o Preteret Stetment
		try {
			//Cria a transaction no banco
			pstmt = con.prepareStatement(sql);
			//le o arquivo CSV 
			CsvReader r = new CsvReader(csvFile, ',', Charset.forName("GBK")); //Mudar o GBK pro charset que for usar
			//Enquanto tiver linhas para ler, ele permanece no while
			while (r.readRecord()) {
				//loop para ler coluna por coluna em cada linha do CSV
				for (int i = 1; i <= 10; i++) {
					//Seta os parametros da query ("?") com valores do CSV, coluna por coluna, na ordem
					pstmt.setString(i, r.get(i));
				}
				//Executa a transaction
				pstmt.executeUpdate();
			}
			//Fecha a transaction, com commit e tudo caso tenha dado certo
			r.close();
		} catch (Exception e) {
			//Cospe o erro caso de algo errado
			System.out.println();
			e.printStackTrace();
		} finally {
			//Independente se der errado ou certo, ele fecha a conexao apos cada insert
			con.close();
		}
	}

}
