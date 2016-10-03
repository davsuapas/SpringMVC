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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.springframework.mock.web.MockHttpSession;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MasteringMvcApplication.class)
@WebAppConfiguration
public class HomeControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void should_redirect_to_profile() throws Exception {
		
		this.mockMvc.perform(get("/"))
			.andDo(print())
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/profile"));
	}
	
	@Test
	public void should_redirect_to_tastes() throws Exception {
		
		MockHttpSession session = new MockHttpSession();
		UserProfileSession sessionBean = new UserProfileSession();
		
		sessionBean.setTastes(Arrays.asList("spring", "groovy"));
		
		/*
		Note that the attribute representing our bean in session is prefixed by scopedTarget .
		That's because session beans are proxified by Spring. Therefore, there are actually
		two objects in the Spring context, the actual bean that we defined and its proxy that
		will end up in the session.
		 */
		
		session.setAttribute("scopedTarget.userProfileSession",	sessionBean);
		
		this.mockMvc.perform(get("/").session(session))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/search/mixed;keywords=spring,groovy"));
	}	
}