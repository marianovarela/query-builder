package ar.com.qbuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ar.com.qbuilder.domain.ResultSet;
import ar.com.qbuilder.helper.ResultBuilder;

class ResultBuilderTest {

	@Test
	void buildSuccessShouldReturnStatusTrueWithoutParameter() {
		ResultSet origin = ResultBuilder.buildSuccess();
		assertFalse(origin.isError());
	}
	
	@Test
	void buildSuccessShouldReturnStatusTrueWithParameter() {
		ResultSet origin = ResultBuilder.buildSuccess(null);
		assertFalse(origin.isError());
	}
	
	@Test
	void buildErrorShouldReturnStatusFalse() {
		String message = "error";
		ResultSet origin = ResultBuilder.buildError(message);
		assertTrue(origin.isError());
		assertEquals(origin.getMessage(), message);
	}

}
