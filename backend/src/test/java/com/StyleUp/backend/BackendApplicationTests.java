package com.StyleUp.backend;

import com.StyleUp.backend.controllers.AuthControllerTests;
import com.StyleUp.backend.services.AuthServiceTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
		AuthControllerTests.class,
		AuthServiceTests.class,
})
class BackendApplicationTests {

}
