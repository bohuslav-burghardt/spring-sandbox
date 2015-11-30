package handler_interceptors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class UndeclaredRequestParamsHandlerInterceptorTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void testGetWithMissingRequestParam() throws Exception {
		mockMvc.perform(get("/test/required")
			.accept(MediaType.ALL))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetWithUnsupportedRequestParam() throws Exception {
		mockMvc.perform(get("/test/optional")
			.param("foo", "foo")
			.accept(MediaType.ALL))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void testGetWithSupportedRequestParams() throws Exception {
		mockMvc.perform(get("/test/optional")
			.param("p", "p")
			.accept(MediaType.ALL))
			.andDo(print())
			.andExpect(status().isOk());
	}

}
