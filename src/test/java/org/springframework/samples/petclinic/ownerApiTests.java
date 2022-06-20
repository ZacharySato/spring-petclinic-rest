package org.springframework.samples.petclinic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class ownerApiTests {
	private static Connection connection;
	private Integer ownerId;
	private PreparedStatement sql;
	private ResultSet queryResult;

	@BeforeAll
	public static void connect() throws SQLException {
		connection = DriverManager.getConnection(
			"jdbc:postgresql://localhost/petclinic",
			"petclinic",
			"petclinic"
		);
	}

	@AfterAll
	public static void disconnect() throws SQLException {
		connection.close();
	}

	@Test
	void ownerCreate() throws SQLException {
		var ownerFirstName = "John";
		var ownerSecondName = "Cena";
		var ownerAddress = "1241, East Main Street";
		var city = "Stamford";
		var telephone = "6085551023";
		sql = connection.prepareStatement(
			"INSERT INTO owners(first_name, last_name, address, city, telephone) VALUES(?,?,?,?,?)",
			Statement.RETURN_GENERATED_KEYS
		);
		sql.setString(1, ownerFirstName);
		sql.setString(2, ownerSecondName);
		sql.setString(3, ownerAddress);
		sql.setString(4, city);
		sql.setString(5, telephone);

		sql.executeUpdate();
		queryResult = sql.getGeneratedKeys();
		queryResult.next();
		ownerId = queryResult.getInt("id");
	}

	@Test
	void ownerDelete() throws SQLException {
		PreparedStatement sql = connection.prepareStatement(
			"DELETE FROM owners WHERE id = ?"
		);
		sql.setInt(1, 11);
		sql.executeUpdate();
	}

	@Test
	void ownerFindQuery() throws SQLException {
		sql = connection.prepareStatement("SELECT * FROM owners WHERE id = ?");
		sql.setInt(1, 1);
		queryResult = sql.executeQuery();
		/*queryResult.next();
		System.out.println(queryResult.getString("first_name"));*/
	}
}
