package org.springframework.samples.petclinic;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

public class PetClinicApiTests {

	@DisplayName("Включаем логи")
	@BeforeAll
	public static void setUpErrorLogging() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	@Nested
	@DisplayName("Visit Tests")
	public class visitTest{


		@Test
		@DisplayName("Создание новой записи")
		public void shouldCreateVisitWhenItsNewVisit(){}

		@Test
		@DisplayName("Создание дублирующей записи / повторная запись") //Ожидаемый результат будет зависеть от поведения метода
		public void shouldCreateVisitWhenItsNotNewVisit(){} //Поменять название метода в зависимости от ОР

		@Test
		@DisplayName("Получение существующей записи")
		public void shoulGetVisitWhenVisitIsFount(){
			when()
				.get("/owners/{ownerId}/pets/{petId}/visits",ownerId,petId)
				.then()
				.statusCode(200)
				.body(
					"id", is(notNullValue()),
					"description", is(notNullValue()),
					"date", is(notNullValue())
				);
		}

		@Test
		@DisplayName("Получение отсутствующей записи")
		public void shoulGetVisitWhenVisitIsNotFount(){
			when()
			.get("/owners/{ownerId}/pets/{petId}/visits",ownerId,petId)
			.then()
			.statusCode(404);
/*
				.body(
					"id", is(5),
					"countryName", is("Ac"),
					"locations", not(empty())
				);
				*/
		}


	}
	@Nested
	@DisplayName("Owner Tests")
	public class ownerTest{

		@Test
		@DisplayName("Создание нового пользователя")
		public void shouldCreateOwnerWhenShouldNotExists(){
			when()
				.post("/owners/{ownerId}/pets/{petId}/visits",ownerId,petId)
				.then()
				.statusCode(404);
/*
				.body(
					"id", is(5),
					"countryName", is("Ac"),
					"locations", not(empty())
				);
				*/
		}

		@Test
		@DisplayName("Получение пользователя")
		public void shouldGetOwnerWhenShouldExists(){

		}


	}
	@Nested
	@DisplayName("Pets Tests")
	public class petsTest{


	}
	@Nested
	@DisplayName("Crash Tests")
	public class crashTest{


	}
}
