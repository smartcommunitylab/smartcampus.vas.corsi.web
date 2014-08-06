package eu.trentorise.smartcampus.corsi.gcmservice;

import java.io.IOException;
import java.util.ArrayList;
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
	private List<String> androidTargets = new ArrayList<String>();

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

	@RequestMapping(method = RequestMethod.POST, value = "/rest/gcm/message/gds/{gds_id}/text/{text}")
	public @ResponseBody
	boolean postTextChatCommunicator(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("gds_id") Long gds_id,
			@PathVariable("text") String text)

	throws IOException {
		try {

			// String token = getToken(request);
			// BasicProfileService service = new BasicProfileService(
			// profileaddress);
			// BasicProfile profile = service.getBasicProfile(token);
			// Long userId = Long.valueOf(profile.getUserId());

			Long userId = (long) 87;

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
					.valueOf(87));

			androidTargets.add(regId_students.get(0).getRegId());

			// Instance of com.android.gcm.server.Sender, that does the
			// transmission of a Message to the Google Cloud Messaging service.
			Sender sender = new Sender(SENDER_ID);

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
					.collapseKey("Test-gcm").timeToLive(30)
					.delayWhileIdle(true).addData("message", text).build();

			// use this for multicast messages. The second parameter
			// of sender.send() will need to be an array of register ids.
			MulticastResult result = sender.send(message, androidTargets, 1);

			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId == 0) {
					return true;
				}
			} else {
				int error = result.getFailure();
				System.out.println("Broadcast failure: " + error);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	private boolean sendMessagesToGcm(List<RegistrationId> regId_students,
			String text) {
		// TODO Auto-generated method stub

		// HttpClient httpClient = new DefaultHttpClient();
		//
		// try {
		// HttpPost request = new HttpPost(URL_GCM_SERVER);
		// StringEntity params = new StringEntity(
		// "{{\"registration_ids\" : [\""
		// + regId_students.get(0)
		// + "\"]},\"data\" : { \"score\": \"5x1\",\"time\": \"15:10\" }}\"");
		// request.addHeader("Authorization", "key=" + SENDER_ID);
		// request.addHeader("Content-Type", "application/json");
		// request.setEntity(params);
		// HttpResponse response = httpClient.execute(request);
		//
		// if (response.getStatusLine().getStatusCode() == 200) {
		// return true;
		// } else {
		// return false;
		// }
		// } catch (Exception ex) {
		// // handle exception here
		// } finally {
		// httpClient.getConnectionManager().shutdown();
		// }
		// return false;

		// Instance of com.android.gcm.server.Sender, that does the
		// transmission of a Message to the Google Cloud Messaging service.
		Sender sender = new Sender(SENDER_ID);

		// This Message object will hold the data that is being transmitted
		// to the Android client devices. For this demo, it is a simple text
		// string, but could certainly be a JSON object.
		Message message = new Message.Builder()

				// If multiple messages are sent using the same .collapseKey()
				// the android target device, if it was offline during earlier
				// message
				// transmissions, will only receive the latest message for that
				// key when
				// it goes back on-line.
				.collapseKey(KEY_GCM_MESSAGE).timeToLive(30)
				.delayWhileIdle(true).addData("message", text).build();

		try {
			// use this for multicast messages. The second parameter
			// of sender.send() will need to be an array of register ids.
			MulticastResult result = sender.send(message, androidTargets, 1);

			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId != 0) {
					return true;
				}
			} else {
				int error = result.getFailure();
				System.out.println("Broadcast failure: " + error);
			}

		} catch (Exception e) {
			e.printStackTrace();
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
