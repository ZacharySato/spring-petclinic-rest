package org.springframework.samples.petclinic;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.samples.petclinic.owner.Owner;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class PetClinicApiTests {
	private static Connection connection;
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

	@DisplayName("Enable logging")
	@BeforeAll
	public static void setUpErrorLogging() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	@Nested
	@DisplayName("Visit Tests")
	public class visitTests {
		private final Integer VISIT_PET_ID = 1;
		private final Integer VISIT_OWNER_ID = 1;
		private Integer visitId;

		@BeforeEach
		void visitCreate() throws SQLException {
			sql = connection.prepareStatement(
				"INSERT INTO visits(pet_id, visit_date, description) VALUES(?,?,?)",
				Statement.RETURN_GENERATED_KEYS
			);
			sql.setInt(1, VISIT_PET_ID);
			sql.setDate(2, Date.valueOf(LocalDate.now()));
			sql.setString(3, "Test description");

			sql.executeUpdate();
			queryResult = sql.getGeneratedKeys();
			queryResult.next();
			visitId = queryResult.getInt("id");
		}

		@AfterEach
		void visitDelete() throws SQLException {
			PreparedStatement sql = connection.prepareStatement(
				"DELETE FROM visits WHERE id = ?"
			);
			sql.setInt(1, visitId);
			sql.executeUpdate();
		}

		void visitQuery() throws SQLException {
			sql = connection.prepareStatement("SELECT * FROM visits WHERE id = ?");
			sql.setInt(1, visitId);
			queryResult = sql.executeQuery();
		}

		@Test
		@DisplayName("Get existed visit")
		public void shouldGetVisitWhenVisitIsFount() {
			when()
				.get("/owners/{ownerId}/pets/{petId}/visits", VISIT_OWNER_ID, VISIT_PET_ID)
				.then()
				.statusCode(200)
				.body(
					"id", is(notNullValue()),
					"description", is(notNullValue()),
					"date", is(notNullValue())
				);
		}

		@Test
		@DisplayName("Get nonexistent visit")
		public void shouldGetVisitWhenVisitIsNotFount() {
			when()
				.get("/owners/{ownerId}/pets/{petId}/visits", VISIT_OWNER_ID, VISIT_PET_ID)
				.then()
				.statusCode(404);
		}
	}

	@Nested
	@DisplayName("Owner Tests")
	public class ownerTests {
		private final Owner testOwner = new Owner();
		private int nextIdAfterGenerated;

		@BeforeEach
		void ownerCreate() throws SQLException {
			testOwner.setFirstName("John");
			testOwner.setLastName("Cena");
			testOwner.setAddress("1241, East Main Street");
			testOwner.setCity("Stamford");
			testOwner.setTelephone("6085551023");

			sql = connection.prepareStatement(
				"INSERT INTO owners(first_name, last_name, address, city, telephone) VALUES(?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS
			);
			sql.setString(1, testOwner.getFirstName());
			sql.setString(2, testOwner.getLastName());
			sql.setString(3, testOwner.getAddress());
			sql.setString(4, testOwner.getCity());
			sql.setString(5, testOwner.getTelephone());

			sql.executeUpdate();
			queryResult = sql.getGeneratedKeys();
			queryResult.next();
			testOwner.setId(queryResult.getInt("id"));
			nextIdAfterGenerated = testOwner.getId() + 1;
		}

		@AfterEach
		void ownerDelete() throws SQLException {
			PreparedStatement sql = connection.prepareStatement(
				"DELETE FROM owners WHERE id = ?"
			);
			sql.setInt(1, testOwner.getId());
			sql.executeUpdate();
		}

		void ownerFindQuery() throws SQLException {
			sql = connection.prepareStatement("SELECT * FROM owners WHERE id = ?");
			sql.setInt(1, testOwner.getId());
			queryResult = sql.executeQuery();
		}

		@Test
		@DisplayName("Create owner")
		public void shouldCreateOwnerWhenNotExists() throws SQLException {
			ownerDelete();
			String newOwnerFirstName = UUID.randomUUID().toString().substring(4);
			testOwner.setFirstName(newOwnerFirstName);
			testOwner.setId(nextIdAfterGenerated);
			given()
				.contentType(ContentType.JSON)
				.body(testOwner)
				.when()
				.post("/owners")
				.then()
				.statusCode(201)
				.body("id", not(empty()))
				.body("firstName", is(newOwnerFirstName));
			ownerFindQuery();
			queryResult.next();
			assertThat(queryResult.getString("first_name"), is(newOwnerFirstName));
		}

		@Test
		@DisplayName("Get existed owner")
		public void shouldGetOwnerWhenExists() {
			when()
				.get("/owners/{ownerId}", testOwner.getId())
				.then()
				.statusCode(200)
				.body("firstName", is(testOwner.getFirstName()))
				.body("lastName", is(testOwner.getLastName()))
				.body("telephone", is(testOwner.getTelephone()));
		}

		@Test
		@DisplayName("Get nonexistent owner")
		public void shouldNotGetOwnerWhenNotExists() {
			when()
				.get("/owners/{ownerId}", nextIdAfterGenerated)
				.then()
				.statusCode(404);
		}
	}
}
