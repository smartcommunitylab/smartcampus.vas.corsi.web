package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.corsi.model.ChatMessage;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.RegistrationIdRepository;

@Controller("chatMessageController")
public class ChatMessageController {

	
	@Autowired
	private RegistrationIdRepository registrationRepository;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private GruppoDiStudioRepository gruppidistudioRepository;

	

	@RequestMapping(method = RequestMethod.GET, value = "/rest/chat/messages/gds/{gds_id}")
	public @ResponseBody
	List<ChatMessage> getChatMessages(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("gds_id") Long gds_id)

	throws IOException {
		try {
			List<ChatMessage> listMessages = new ArrayList<ChatMessage>();
			
			
			return listMessages;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
