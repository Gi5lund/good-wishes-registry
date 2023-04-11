package dk.kea.dat22b.goodwishesregistry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController
	{
		@PostMapping("/login")
		public String login(@RequestParam("username") String username,@RequestParam("pwd") String password){
			if (username.equals("Morten") && password.equals("123")){
				return "testretur";

			}
			return "login";
		}
	}
