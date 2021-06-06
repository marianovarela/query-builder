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
		assertTrue(origin.isStatus());
	}
	
	@Test
	void buildSuccessShouldReturnStatusTrueWithParameter() {
		ResultSet origin = ResultBuilder.buildSuccess(null);
		assertTrue(origin.isStatus());
	}
	
	@Test
	void buildErrorShouldReturnStatusFalse() {
		String message = "error";
		ResultSet origin = ResultBuilder.buildError(message);
		assertFalse(origin.isStatus());
		assertEquals(origin.getMessage(), message);
	}

}
