package eu.trentorise.smartcampus.corsi.gcmservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import eu.trentorise.smartcampus.corsi.model.GruppoDiStudio;
import eu.trentorise.smartcampus.corsi.model.RegistrationId;
import eu.trentorise.smartcampus.corsi.model.Studente;
import eu.trentorise.smartcampus.corsi.repository.DipartimentoRepository;
import eu.trentorise.smartcampus.corsi.repository.GruppoDiStudioRepository;
import eu.trentorise.smartcampus.corsi.repository.RegistrationIdRepository;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Service("GCMBroadcastChatService")
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
	//private static final String REG_ID = "APA91bE2bGNZTkNK6f0xp01_tvjfaKllEdrHot2L_6JrFaErmdBYdEzP8HalnqiXU7EpOOdCNgq8-vqlzsDWs5coXYxlhMxgP7c1jas-igy-MQMc25QEEemy2WYsxkdceivuFNQiKmQsnZ1mG1sqiRW7iwIF3FLdiw";

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

	

	@RequestMapping(method = RequestMethod.POST, value = "/rest/gcm/message/student/{id_student}/gds/{gds_id}/text/{text}")
	public @ResponseBody
	boolean postTextChat(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("id_student") Long id_student,
			@PathVariable("gds_id") Long gds_id,
			@PathVariable("text") String text)

	throws IOException {
		try {

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			if (id_student != userId) {
				return false;
			}

			GruppoDiStudio gds_chat = gruppidistudioRepository.findOne(gds_id);
			List<Studente> listStudents = gds_chat.getStudentiGruppo();

			// per ogni studente vado a prendere il relativo reg_id
			for (Studente studente : listStudents) {
				if (studente.getId() != userId) {
					RegistrationId regId_student = registrationRepository
							.findOne(studente.getId());
					androidTargets.add(regId_student.getRegId());
				}
			}

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
					.timeToLive(30)
					.delayWhileIdle(true)
					.addData(
							"text-" + id_student + "-" + gds_id + "-"
									+ System.currentTimeMillis(), text).build();

			try {
				// use this for multicast messages. The second parameter
				// of sender.send() will need to be an array of register ids.
				MulticastResult result = sender
						.send(message, androidTargets, 1);

				if (result.getResults() != null) {
					int canonicalRegId = result.getCanonicalIds();
					if (canonicalRegId != 0) {
						return true;
					}
				} else {
					int error = result.getFailure();
					logger.error("Broadcast failure: " + error);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// We'll pass the CollapseKey and Message values back to index.jsp,
			// only so
			// we can display it in our form again.
			// request.setAttribute("CollapseKey", collapseKey);
			// request.setAttribute("Message", userMessage);

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return false;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rest/gcm/student/{student_id}/reg_id/{reg_id}")
	public @ResponseBody
	boolean registerRegId(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable("student_id") Long student_id,
			@PathVariable("reg_id") String reg_id) throws IOException {

		try {

			String token = getToken(request);
			BasicProfileService service = new BasicProfileService(
					profileaddress);
			BasicProfile profile = service.getBasicProfile(token);
			Long userId = Long.valueOf(profile.getUserId());

			if (userId == student_id) {

				RegistrationId newRegIdDeviceApp = new RegistrationId();

				newRegIdDeviceApp.setRegId(reg_id);
				newRegIdDeviceApp.setStudentId(student_id);

				newRegIdDeviceApp = registrationRepository
						.save(newRegIdDeviceApp);

				if (newRegIdDeviceApp != null) {
					return true;
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
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
