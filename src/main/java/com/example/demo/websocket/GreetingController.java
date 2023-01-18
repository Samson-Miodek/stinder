package com.example.demo.websocket;

import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class GreetingController {


	/*
		@SendTo("/topic/greetings")
		stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });

        @MessageMapping("/app/hello")
        stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
	* */

	@Autowired
	private UserRepository userRepository;

	@MessageMapping("/app/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(Principal principal, HelloMessage message) throws Exception {

		if (principal == null)
			return null;

//		var user = userRepository.findByUsername(principal.getName());
//		System.out.println(user);

		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}

}
