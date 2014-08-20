package eu.trentorise.smartcampus.corsi.gcmservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import eu.trentorise.smartcampus.corsi.model.ChatMessage;
import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.RegistrationId;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.ChatMessageRepository;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.RegistrationIdRepository;
import eu.trentorise.smartcampus.corsi.repository.StudenteRepository;
import eu.trentorise.smartcampus.network.RemoteException;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("GCMBroadcastChatService")
@Configuration
@ComponentScan("eu.trentorise.smartcampus.corsi.gcmservice")
public class GCMBroadcastChatService {

	private static final Logger logger = Logger
			.getLogger(GCMBroadcastChatService.class);

	// The SENDER_ID here is the "Browser Key" that was generated when I
	// created the API keys for my Google APIs project.
	private static final String SENDER_ID = "AIzaSyAB0Vz4H7385yy1T_nxTg0gPnP1ao7JRxg";

	// This is a *cheat* It is a hard-coded registration ID from an Android
	// device
	// that registered itself with GCM using the same project id shown above.
	// private static final String REG_ID =
	// "APA91bE2bGNZTkNK6f0xp01_tvjfaKllEdrHot2L_6JrFaErmdBYdEzP8HalnqiXU7EpOOdCNgq8-vqlzsDWs5coXYxlhMxgP7c1jas-igy-MQMc25QEEemy2WYsxkdceivuFNQiKmQsnZ1mG1sqiRW7iwIF3FLdiw";

	// This array will hold all the registration ids used to broadcast a
	// message.
	// for this demo, it will only have the ANDROID_DEVICE id that was captured
	// when we ran the Android client app through Eclipse.
	private List<String> androidTargets;

	@Autowired
	private RegistrationIdRepository registrationRepository;

	@Autowired
	@Value("${profile.address}")
	private String profileaddress;

	@Autowired
	private GruppoDiStudioRepository gruppidistudioRepository;

	@Autowired
	private ChatMessageRepository chatMessagesRepository;

	@Autowired
	private StudenteRepository studenteRepository;

	private static final String URL_GCM_SERVER = "https://android.googleapis.com/gcm/send";

	private static final String KEY_GCM_MESSAGE = "gcm-message-gds";

	private static final String GCM_KEY_GDS = "message-gds-gcm";

	@RequestMapping(method = RequestMethod.POST, value = "/rest/gcm/message/gds/{gds_id}/text/{text}")
	public @ResponseBody
	boolean postTextChat(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("gds_id") Long gds_id,
			@PathVariable("text") String text)

	throws IOException {
		try {

			 String token = getToken(request);
			 BasicProfileService service = new BasicProfileService(
			 profileaddress);
			 BasicProfile profile = service.getBasicProfile(token);
			 Long userId = Long.valueOf(profile.getUserId());

			//Long userId = (long) 87;

			// save the message on db
			ChatMessage messageChat = new ChatMessage();
			messageChat.setId_studente(userId);
			messageChat.setNome_studente(studenteRepository.findOne(userId)
					.getNome());
			messageChat.setTesto(text);
			messageChat.setGds(gds_id);

			if (messageChat.getNome_studente() != null) {
				chatMessagesRepository.save(messageChat);
			} else {
				logger.info("No student found with id: " + userId.toString());
				return false;
			}

			GruppoDiStudio gds_chat = gruppidistudioRepository.findOne(gds_id);

			if (gds_chat == null) {
				logger.error("Gds with id = " + gds_id + "does not exist.");
				return false;
			}

			String listIds = gds_chat.getIdsStudenti();
			List<String> listStudents = gds_chat.convertIdsInvitedToList(
					listIds, userId);
			List<RegistrationId> regId_students = null;

			// per ogni studente vado a prendere il relativo reg_id
			for (String stringId : listStudents) {

				if (!stringId.equals(userId.toString())) {
					regId_students = registrationRepository
							.findRegIdsByStudent(Long.valueOf(stringId));

					if (regId_students != null) {

						for (RegistrationId registrationId : regId_students) {
							androidTargets.add(registrationId.getRegId());
						}

					}
				}
			}
			regId_students = registrationRepository.findRegIdsByStudent(Long
					.valueOf(userId));
			for (RegistrationId registrationId : regId_students) {
				androidTargets.add(registrationId.getRegId());
			}

			sendMessagesToGcm(regId_students, text, gds_id);

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rest/gcm/messagecommunicator/gds/{gds_id}/text/{text}")
	public @ResponseBody
	boolean postTextChatCommunicator(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("gds_id") Long gds_id,
			@PathVariable("text") String text)

	throws IOException, SecurityException, ProfileServiceException {

		try {
		 String token = getToken(request);
		 BasicProfileService service = new BasicProfileService(
		 profileaddress);
		 BasicProfile profile = service.getBasicProfile(token);
		 Long userId = Long.valueOf(profile.getUserId());

		//Long userId = (long) 87;///////////////////////////////////////////////////////////////////test
		 
		androidTargets = new ArrayList<String>();

		// save the message on db
		ChatMessage messageChat = new ChatMessage();
		messageChat.setId_studente(userId);
		messageChat.setNome_studente(studenteRepository.findOne(userId)
				.getNome());
		messageChat.setTesto(text);

		if (messageChat.getNome_studente() != null) {
			chatMessagesRepository.save(messageChat);
		} else {
			logger.info("No student found with id: " + userId.toString());
			return false;
		}

		GruppoDiStudio gds_chat = gruppidistudioRepository.findGdsById(gds_id.longValue());

		if (gds_chat == null) {
			logger.error("Gds with id = " + gds_id + " does not exist.");
			return false;
		}

		String listIds = gds_chat.getIdsStudenti();
		List<String> listStudents = gds_chat.convertIdsInvitedToList(listIds,
				userId);
		List<RegistrationId> regId_students = null;
		// per ogni studente vado a prendere il relativo reg_id
		for (String stringId : listStudents) {

			if (!stringId.equals(userId.toString())) {
				regId_students = registrationRepository
						.findRegIdsByStudent(Long.valueOf(stringId));

				if (regId_students != null) {

					for (RegistrationId registrationId : regId_students) {
						androidTargets.add(registrationId.getRegId());
					}

				}
			}
		}

		//regId_students = registrationRepository.findRegIdsByStudent(Long   ////////////////////////test
		//		.valueOf(87));
 
		//androidTargets.add(regId_students.get(0).getRegId());  ////////////////////test
		
		
			return sendMessagesToGcm(regId_students, text, gds_id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
		
	}

	private boolean sendMessagesToGcm(List<RegistrationId> regId_students,
			String text, long gds) throws IOException {
		// Instance of com.android.gcm.server.Sender, that does the
		// transmission of a Message to the Google Cloud Messaging service.

		Sender sender = new Sender(SENDER_ID);

		String timestampNow = String.valueOf(System.currentTimeMillis());
		
		// This Message object will hold the data that is being transmitted
		// to the Android client devices. For this demo, it is a simple text
		// string, but could certainly be a JSON object.
		Message message = new Message.Builder()

				// If multiple messages are sent using the same
				// .collapseKey()
				// the android target device, if it was offline during
				// earlier message
				// transmissions, will only receive the latest message for
				// that key when
				// it goes back on-line.
				.collapseKey(GCM_KEY_GDS).timeToLive(30).delayWhileIdle(true)
				.addData("message", text)
				.addData("gds", String.valueOf(gds))
				.addData("gds_name", gruppidistudioRepository.findOne(gds).getNome())
				.addData("date", timestampNow)
				.build();

		// use this for multicast messages. The second parameter
		// of sender.send() will need to be an array of register ids.
		MulticastResult result = sender.send(message, androidTargets, 1);

		if (result.getResults() != null) {
			int canonicalRegId = result.getSuccess();
			if (canonicalRegId == regId_students.size()) {
				return true;
			}
		} else {
			int error = result.getFailure();
			System.out.println("Broadcast failure: " + error);
		}

		return false;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rest/gcm/reg_id/{reg_id}")
	public @ResponseBody
	boolean registerRegId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("reg_id") String reg_id) throws IOException {

		try {

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			RegistrationId newRegIdDeviceApp = new RegistrationId();

			newRegIdDeviceApp.setRegId(reg_id);
			newRegIdDeviceApp.setStudentId(userId);

			newRegIdDeviceApp = registrationRepository.save(newRegIdDeviceApp);

			if (newRegIdDeviceApp != null) {
				logger.info("RegId saved");
				return true;
			} else {
				logger.info("RegId not saved");
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return false;
	}

	/**
	 * 
	 * @param request
	 * @return String
	 * 
	 *         Ottiene il token riferito alla request
	 * 
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}
