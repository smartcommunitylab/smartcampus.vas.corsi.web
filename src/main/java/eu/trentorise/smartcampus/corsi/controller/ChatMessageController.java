package eu.trentorise.smartcampus.corsi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import eu.trentorise.smartcampus.corsi.repository.ChatMessageRepository;
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

	@Autowired
	private ChatMessageRepository chatMessageRepository;
	

	/**
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @param gds_id
	 * @return List<ChatMessage> of a gds
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/chat/messages/gds/{gds_id}")
	public @ResponseBody
	List<ChatMessage> getChatMessages(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("gds_id") Long gds_id)

	throws IOException {
		try {
			
			List<ChatMessage> messages = chatMessageRepository.getMessagesOfGds(gds_id);
			
			if(messages == null)
				return null;
			
			
			// sorting of messages
			Collections.sort(messages, new Comparator<ChatMessage>() {
		        @Override
		        public int compare(ChatMessage  cm1, ChatMessage  cm2)
		        {
		            return  cm1.getData().compareTo(cm2.getData());
		        }
		    });
			
			
			return messages;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
