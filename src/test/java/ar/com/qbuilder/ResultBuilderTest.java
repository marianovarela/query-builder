package ar.com.qbuilder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ar.com.qbuilder.domain.Result;
import ar.com.qbuilder.helper.ResultBuilder;

class ResultBuilderTest {

	@Test
	void buildSuccessShouldReturnStatusTrueWithoutParameter() {
		Result origin = ResultBuilder.buildSuccess();
		assertTrue(origin.isStatus());
	}
	
	@Test
	void buildSuccessShouldReturnStatusTrueWithParameter() {
		Result origin = ResultBuilder.buildSuccess(null);
		assertTrue(origin.isStatus());
	}
	
	@Test
	void buildErrorShouldReturnStatusFalse() {
		String message = "error";
		Result origin = ResultBuilder.buildError(message);
		assertFalse(origin.isStatus());
		assertEquals(origin.getMessage(), message);
	}

}
