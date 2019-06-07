package com.harystolho.adexchange.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.harystolho.adexchange.notifications.Notification;
import com.harystolho.adexchange.services.NotificationService;
import com.harystolho.adexchange.services.ServiceResponse;
import com.harystolho.adexchange.services.ServiceResponse.ServiceResponseType;
import com.harystolho.adexchange.utils.AEUtils;
import com.harystolho.adexchange.utils.JsonResponse;

@RestController
@CrossOrigin(origins = AEUtils.corsOrigin)
public class NotificationController {

	private NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping("/api/v1/notifications/me")
	public ResponseEntity<Object> getNotificationsForUser(@RequestAttribute("ae.accountId") String accountId) {
		ServiceResponse<List<Notification>> response = notificationService.getNotificationsForUser(accountId);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK).body(response.getReponse());
		default:
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response.getFullMessage());
		}
	}

	@GetMapping("/api/v1/notifications/me/status")
	public ResponseEntity<Object> getUserNotificationsStatus(@RequestAttribute("ae.accountId") String accountId) {

		ServiceResponse<Boolean> response = notificationService.getNotificationStatusForUser(accountId);

		switch (response.getErrorType()) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK)
					.body(JsonResponse.of("notifyNewNotifications", response.getReponse()).build());
		default:
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response.getFullMessage());
		}
	}

	@PostMapping("/api/v1/notifications/me/status")
	public ResponseEntity<Object> setUserNotificationsStatus(@RequestAttribute("ae.accountId") String accountId,
			boolean notifyNewNotifications) {

		ServiceResponseType response = notificationService.setNotificationStatusForUser(accountId,
				notifyNewNotifications);

		switch (response) {
		case OK:
			return ResponseEntity.status(HttpStatus.OK).body(null);
		default:
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
		}
	}

}
