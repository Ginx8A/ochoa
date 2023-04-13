package com.example.ochoa.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ochoa.service.HttpService;

/**
 * <p>
 * Controlador general de carrera
 * </p>
 * 
 * @author ochoa
 * @since 13/04/2023
 *
 */
@Controller
public class CarreraController {

	@Autowired
	private HttpService service;

	@Value("${base.url.career}")
	private String baseUrl;

	public static final String API_URL_CAREER = "/api/v1/careers/";

	/**
	 * <p>
	 * Lista todos las carreras
	 * </p>
	 *
	 * @author ochoa
	 * @since 13/04/2023
	 * 
	 * @return
	 */
	@GetMapping(path = "getCareers")
	@ResponseBody
	public ResponseEntity<?> listCareers() {
		ResponseEntity<?> result;
		try {
			String url = baseUrl.concat(API_URL_CAREER);
			result = service.getDataRequest(url, HttpMethod.GET, null);
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	/**
	 * <p>
	 * Muestra los datos de una carrera
	 * </p>
	 *
	 * @author ochoa
	 * @since 13/04/2023
	 *
	 * @return
	 */
	@GetMapping(path = "getCareer")
	@ResponseBody
	public ResponseEntity<?> getCareer(@RequestParam String id) {
		ResponseEntity<?> result;
		try {
			String url = baseUrl.concat(API_URL_CAREER).concat(id);
			result = service.getDataRequest(url, HttpMethod.GET, null);
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	/**
	 * <p>
	 * Metodo para eliminar una carrera
	 * </p>
	 * 
	 * @author ochoa
	 * @since 12/04/2023
	 * 
	 * @param id
	 * @param career
	 * @return
	 */
	@PutMapping(path = "deleteCareer/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteCareer(@PathVariable(value = "id") String id) {
		ResponseEntity<?> result = null;
		try {
			String url = baseUrl.concat(API_URL_CAREER) + id;
			result = service.getDataRequest(url, HttpMethod.DELETE, null);
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	/**
	 * <p>
	 * Metodo para actualizar una carrera
	 * </p>
	 * 
	 * @author ochoa
	 * @since 12/04/2023
	 * 
	 * @param id
	 * @param career
	 * @return
	 */
	@PutMapping(path = "putCareer/{id}")
	@ResponseBody
	public ResponseEntity<?> putCareer(@PathVariable(value = "id") String id, @RequestBody String career) {
		ResponseEntity<?> result = null;
		try {
			String url = baseUrl.concat(API_URL_CAREER) + id;
			JSONObject body = createCareer(career);
			result = service.getDataRequest(url, HttpMethod.PUT, body.toString());
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	/**
	 * <p>
	 * Metodo para crear una carrera
	 * </p>
	 * 
	 * @author ochoa
	 * @since 12/04/2023
	 * 
	 * @param id
	 * @param career
	 * @return
	 */
	@PostMapping(path = "postCareer")
	@ResponseBody
	public ResponseEntity<?> postCareer(@RequestBody String career) {
		ResponseEntity<?> result = null;
		try {
			String url = baseUrl.concat(API_URL_CAREER);
			JSONObject body = createCareer(career);
			result = service.getDataRequest(url, HttpMethod.PUT, body.toString());
		} catch (IOException e) {
			e.printStackTrace();
			result = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		return result;
	}

	private JSONObject createCareer(String career) {
		String actual = String.valueOf(LocalDateTime.now());
		JSONObject father = new JSONObject();
		JSONObject jsonCareer = new JSONObject(career);
		if (StringUtils.hasLength(jsonCareer.getString("id"))) {
			father.put("id", jsonCareer.get("id"));
			father.put("fechaAlta", jsonCareer.get("fechaAlta"));
			father.put("usuarioAlta", jsonCareer.get("usuarioAlta"));
			father.put("fechaModificacion", actual);
			father.put("usuarioModificacion", jsonCareer.get("user"));
		} else {
			father.put("fechaAlta", actual);
			father.put("usuarioAlta", jsonCareer.get("user"));
		}
		JSONObject newComment = new JSONObject();
		father.put("nombre", jsonCareer.get("nombre"));
		father.put("objetivo", jsonCareer.get("objetivo"));
		father.put("modalidad", jsonCareer.get("modalidad"));
		JSONObject children = new JSONObject();
		children.put("duracion", jsonCareer.get("duracion"));
		children.put("unidadTiempoDuracion", jsonCareer.get("unidadTiempoDuracion"));
		children.put("costo", jsonCareer.get("costo"));
		children.put("vigente", true);
		father.put("detalle", children);
		father.put("estado", jsonCareer.get("estado"));
		father.put("imagen", jsonCareer.get("imagen"));
		newComment.put("career", father);
		return newComment;
	}

}
