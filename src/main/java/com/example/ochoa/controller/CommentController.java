package com.example.ochoa.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ochoa.service.HttpService;

/**
 * 
 * @author ochoa
 * @since 12/04/2023
 *
 */
@Controller
public class CommentController {

	@Value("${base.url.zendesk}")
	private String baseUrlZendesk;

	public static final String API_URL_TICKETS = "/api/v2/tickets/";
	public static final String API_URL_COMMENTS = "/comments/";
	public static final String UPDATED_STAMP = "updated_stamp";

	@Autowired
	public HttpService service;

	/**
	 * <p> Metodo que trae los comentarios de un ticket especifico</p>
	 * @author ochoa
	 * @since 12/04/2023
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(path = "getComments")
	@ResponseBody
	public ResponseEntity<?> getComments(@RequestParam String id) {
		ResponseEntity<?> result;
		try {
			String url = baseUrlZendesk.concat(API_URL_TICKETS).concat(id).concat(API_URL_COMMENTS);
			result = service.getDataRequest(url, HttpMethod.GET, null);
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	/**
	 * <p> Metodo que trae los tickets</p>
	 * @author ochoa
	 * @since 12/04/2023
	 * 
	 * @return
	 */
	@GetMapping(path = "getTickets")
	@ResponseBody
	public ResponseEntity<?> getTickets() {
		ResponseEntity<?> result;
		try {
			String url = baseUrlZendesk.concat(API_URL_TICKETS);
			result = service.getDataRequest(url, HttpMethod.GET, null);
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	/**
	 * <p> Metodo que agrega un comentario en un ticket especifico</p>
	 * @author ochoa
	 * @since 12/04/2023
	 * 
	 * @param id
	 * @param comments
	 * @return
	 */
	@PutMapping(path = "putComments/{id}")
	@ResponseBody
	public ResponseEntity<?> putComments(@PathVariable(value = "id") String id, @RequestBody String comments) {
		ResponseEntity<?> result = null;
		try {
			String url = baseUrlZendesk.concat(API_URL_TICKETS) + id;
			ResponseEntity<?> ticket = service.getDataRequest(url, HttpMethod.GET, null);
			if (ticket.getStatusCode().value() == HttpStatus.OK.value()) {
				JSONObject body = createComment(ticket, comments);
				result = service.getDataRequest(url, HttpMethod.PUT, body.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	/**
	 * <p> Metodo que crea un comentario con fecha actual y con los datos del ticket</p>
	 * 
	 * @param ticket
	 * @param comments
	 * @return
	 */
	private JSONObject createComment(ResponseEntity<?> ticket, String comments) {
		JSONObject newComment = new JSONObject();
		if (ticket != null && ticket.getBody() != null) {
			String actual = String.valueOf(LocalDateTime.now());
			JSONObject jsonObject = new JSONObject(ticket.getBody().toString());
			JSONObject jsonComment = new JSONObject(comments);
			JSONObject jsonTicket = (JSONObject) jsonObject.get("ticket");
			JSONObject jsonVia = (JSONObject) jsonTicket.get("via");
			JSONObject children = new JSONObject();
			children.put("channel", jsonVia.get("channel"));
			JSONObject father = new JSONObject();
			father.put("via", children);
			father.put(UPDATED_STAMP, actual.formatted(DateTimeFormatter.ISO_DATE_TIME));
			father.put("ticket_form_id", jsonTicket.get("ticket_form_id"));
			father.put("tags", jsonTicket.get("tags"));
			father.put("subject", jsonTicket.get("subject"));
			father.put("status", jsonTicket.get("status"));
			father.put("safe_update", true);
			father.put("requester_id", jsonTicket.get("requester_id"));
			father.put("problem_id", jsonTicket.get("problem_id"));
			father.put("organization_id", jsonTicket.get("organization_id"));
			father.put("group_id", jsonTicket.get("group_id"));
			father.put("followers", jsonTicket.get("follower_ids"));
			father.put("external_id", jsonTicket.get("external_id"));
			father.put("email_ccs", jsonTicket.get("email_cc_ids"));
			father.put("custom_status_id", jsonTicket.get("custom_status_id"));
			father.put("custom_fields", jsonTicket.get("custom_fields"));
			children = new JSONObject();
			children.put("uploads", new ArrayList<>());
			children.put("public", jsonTicket.get("is_public"));
			children.put("html_body", jsonComment.get("comment"));
			children.put("channel_back", jsonTicket.get("allow_channelback"));
			children.put("add_short_url", "0");
			father.put("comment", children);
			father.put("brand_id", jsonTicket.get("brand_id"));
			father.put("assignee_id", jsonTicket.get("assignee_id"));
			newComment.put("ticket", father);
		}
		return newComment;
	}

}
