package com.elipcero.masteringmvc;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {MasteringMvcApplication.class})
@WebAppConfiguration
public class SearchControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	// Chequear elementos
	//	.andExpect(model().attribute("tweets",
	//	hasItems(
	//			hasProperty("image", is("aa")),
	//			hasProperty("image", is("aa"))
	//			)
	//		)
	//	);
	
	@Test
	public void should_search() throws Exception {
		
		this.mockMvc.perform(get("/search/mixed;keywords=spring"))
			.andExpect(status().isOk())
			.andExpect(view().name("resultPage"))
			.andExpect(model().attribute("tweets", hasSize(2)));
	}
}
