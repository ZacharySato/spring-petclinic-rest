package org.springframework.samples.petclinic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PetClinicApiTests {

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
		public void shoulGetVisitWhenVisitIsFount(){}

		@Test
		@DisplayName("Получение отсутствующей запис12иjj")
		public void shoulGetVisitWhenVisitIsNotFount(){}


	}
	@Nested
	@DisplayName("Owner Tests")
	public class ownerTest{


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
